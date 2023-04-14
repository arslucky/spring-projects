package org.demo.ars.domain.account;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author arsen.ibragimov
 *
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRep;

    public Optional<Account> getAccount( String account) {
        return accountRep.findByAccountNumber( account);
    }
}
