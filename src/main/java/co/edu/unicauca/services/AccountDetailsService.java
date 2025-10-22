package co.edu.unicauca.services;

import co.edu.unicauca.authentication.AccountDetails;
import co.edu.unicauca.entities.Account;
import co.edu.unicauca.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String prmEmail) throws UsernameNotFoundException {
        Account acc = accountRepository.findByEmail(prmEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AccountDetails(acc);
    }
}


