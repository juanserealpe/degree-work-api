package co.edu.unicauca.services.auth;

import co.edu.unicauca.authentication.AccountDetails;
import co.edu.unicauca.dtos.login.AuthDTO;
import co.edu.unicauca.dtos.login.LoginResponseDTO;
import co.edu.unicauca.dtos.login.LoginRequestDTO;
import co.edu.unicauca.dtos.login.UserDTO;
import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.RefreshToken;
import co.edu.unicauca.enums.exceptions.UserErrorCode;
import co.edu.unicauca.exceptions.TokenRefreshException;
import co.edu.unicauca.exceptions.UserException;
import co.edu.unicauca.repositories.AccountRepository;
import co.edu.unicauca.utilities.JwtUtils;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager _authenticationManager;

    @Autowired
    private JwtUtils _jwtUtils;

    @Autowired
    private RefreshTokenService _refreshTokenService;

    @Autowired
    private AccountRepository _accountRepository;

    @Transactional
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Logger.info(getClass(), "Attempting login for email: " + loginRequest.email());

        Authentication auth;
        try {
            auth = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            Logger.warn(getClass(), "Invalid credentials for email: " + loginRequest.email());
            throw new UserException(UserErrorCode.INVALID_CREDENTIALS, "Invalid email or password");
        }

        AccountDetails userDetails = (AccountDetails) auth.getPrincipal();

        // Generate JWT access token
        String accessToken = _jwtUtils.generateJwtToken(userDetails);

        // Find the account entity
        Account account = _accountRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserException(
                        UserErrorCode.INVALID_CREDENTIALS,
                        "Invalid email or password"
                ));

        // Create refresh token
        RefreshToken refreshToken = _refreshTokenService.createRefreshToken(account);

        // Get roles
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Logger.success(getClass(), "Login successful for account ID: " + userDetails.getId()
                + " | Roles: " + roles);

        AuthDTO authDTO = new AuthDTO(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );

        UserDTO userDTO = new UserDTO(
                userDetails.getUsername(), // or account.getEmail()
                roles
        );

        return new LoginResponseDTO(authDTO, userDTO);
    }


    @Transactional
    public LoginResponseDTO refreshAccessToken(String requestRefreshToken) {
        Logger.info(getClass(), "Attempting to refresh access token");

        try {
            RefreshToken refreshToken = _refreshTokenService.findByToken(requestRefreshToken);
            _refreshTokenService.verifyExpiration(refreshToken);
            Account account = refreshToken.getAccount();
            AccountDetails userDetails = new AccountDetails(account);
            String newAccessToken = _jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            Logger.success(getClass(), "Access token refreshed successfully for account ID: "
                    + account.getIdAccount());

            AuthDTO authDTO = new AuthDTO(
                    newAccessToken,
                    refreshToken.getToken(),
                    "Bearer"
            );

            UserDTO userDTO = new UserDTO(
                    account.getEmail(),
                    roles
            );

            return new LoginResponseDTO(authDTO, userDTO);

        } catch (TokenRefreshException e) {
            Logger.error(getClass(), "Token refresh failed: " + e.getMessage());
            throw e;
        }
    }


    @Transactional
    public void logout(String refreshToken) {
        Logger.info(getClass(), "Processing logout");

        if (refreshToken != null && !refreshToken.isEmpty()) {
            _refreshTokenService.revokeToken(refreshToken);
            Logger.success(getClass(), "Logout successful - token revoked");
        }
    }

    @Transactional
    public void logoutAllDevices(Long accountId) {
        Logger.info(getClass(), "Logging out all devices for account ID: " + accountId);

        Account account = _accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        _refreshTokenService.revokeTokensByAccount(account);
        Logger.success(getClass(), "All devices logged out for account ID: " + accountId);
    }
}