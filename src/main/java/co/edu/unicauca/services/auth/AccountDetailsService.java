package co.edu.unicauca.services.auth;

import co.edu.unicauca.authentication.AccountDetails;
import co.edu.unicauca.entities.Account;
import co.edu.unicauca.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import co.edu.unicauca.utilities.Logger;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository _accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Logger.info(getClass(), "Loading user details for email: " + email);

        Account account = _accountRepository.findByEmail(email)
                .orElseThrow(() -> {
                    Logger.error(getClass(), "User not found with email: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        Logger.success(getClass(), "User details loaded successfully for email: " + email);
        return new AccountDetails(account);
    }
}
