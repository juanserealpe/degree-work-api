package co.edu.unicauca.services;

import co.edu.unicauca.entities.Account;
import co.edu.unicauca.enums.Role;
import co.edu.unicauca.repositories.AccountRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class RoleManagementService {

    @Autowired
    private AccountRepository _accountRepository;


    @Transactional
    public Account addRoleToAccount(Long accountId, Role role) {
        Logger.info(getClass(), "Adding role " + role + " to account ID: " + accountId);

        Account account = _accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.hasRole(role)) {
            Logger.warn(getClass(), "Account already has role: " + role);
            throw new IllegalArgumentException("Account already has this role");
        }

        account.addRole(role);
        Account saved = _accountRepository.save(account);

        Logger.success(getClass(), "Role added successfully");
        return saved;
    }

    @Transactional
    public Account removeRoleFromAccount(Long accountId, Role role) {
        Logger.info(getClass(), "Removing role " + role + " from account ID: " + accountId);

        Account account = _accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getRoles().size() == 1) {
            throw new IllegalArgumentException("Cannot remove the last role from account");
        }

        account.removeRole(role);
        Account saved = _accountRepository.save(account);

        Logger.success(getClass(), "Role removed successfully");
        return saved;
    }

    public Set<Role> getAccountRoles(Long accountId) {
        Account account = _accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account.getRoles();
    }
}
