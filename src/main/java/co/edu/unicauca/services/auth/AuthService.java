package co.edu.unicauca.services.auth;

import co.edu.unicauca.authentication.AccountDetails;
import co.edu.unicauca.dtos.login.*;
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

    // user authentication + token creation
    @Transactional
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Logger.info(getClass(), "attempting login for email: " + loginRequest.email());

        Authentication auth;
        try {
            auth = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            Logger.warn(getClass(), "invalid credentials for email: " + loginRequest.email());
            throw new UserException(UserErrorCode.INVALID_CREDENTIALS, "invalid email or password");
        }

        AccountDetails userDetails = (AccountDetails) auth.getPrincipal();

        String accessToken = _jwtUtils.generateJwtToken(userDetails);

        Account account = _accountRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserException(
                        UserErrorCode.INVALID_CREDENTIALS,
                        "invalid email or password"
                ));

        // refresh token creation handled safely (throws TokenRefreshException if fails)
        RefreshToken refreshToken = _refreshTokenService.createRefreshToken(account);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Logger.success(getClass(), "login successful for account ID: " + userDetails.getId()
                + " | roles: " + roles);

        return new LoginResponseDTO(
                new AuthDTO(accessToken, refreshToken.getToken(), "Bearer"),
                new UserDTO(userDetails.getUsername(), roles)
        );
    }

    // refresh access token using a valid refresh token
    @Transactional
    public LoginResponseDTO refreshAccessToken(RefreshRequestDTO request) {
        Logger.info(getClass(), "attempting to refresh access token");

        try {
            RefreshToken refreshToken = _refreshTokenService.findByToken(request.getRefreshToken());
            _refreshTokenService.verifyExpiration(refreshToken);

            Account account = refreshToken.getAccount();
            AccountDetails userDetails = new AccountDetails(account);
            String newAccessToken = _jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            Logger.success(getClass(), "access token refreshed successfully for account ID: "
                    + account.getIdAccount());

            return new LoginResponseDTO(
                    new AuthDTO(newAccessToken, refreshToken.getToken(), "Bearer"),
                    new UserDTO(account.getEmail(), roles)
            );

        } catch (TokenRefreshException e) {
            // no need to rewrap; already carries enum info
            Logger.error(getClass(), "token refresh failed: " + e.getErrorCode() + " - " + e.getMessage());
            throw e;
        }
    }

    // revoke token for current session
    @Transactional
    public void logout(String refreshToken) {
        Logger.info(getClass(), "processing logout");
        _refreshTokenService.revokeToken(refreshToken);
        Logger.success(getClass(), "logout successful - token revoked");
    }

    // revoke all tokens for the user
    @Transactional
    public void logoutAllDevices(Long accountId) {
        Logger.info(getClass(), "logging out all devices for account ID: " + accountId);

        Account account = _accountRepository.findById(accountId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "id not found"));

        _refreshTokenService.revokeTokensByAccount(account);
        Logger.success(getClass(), "all devices logged out for account ID: " + accountId);
    }
}
