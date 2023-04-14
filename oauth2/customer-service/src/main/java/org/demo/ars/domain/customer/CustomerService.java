package org.demo.ars.domain.customer;

import java.util.Optional;

import org.demo.ars.domain.account.Account;
import org.demo.ars.domain.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author arsen.ibragimov
 *
 */
@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRep;

    @Autowired
    AccountRepository accountRepository;

    public Optional<Customer> getByAccount( String account) {
        Optional<Account> acc = accountRepository.findByAccountNumber( account);
        if( acc.isEmpty()) {
            return Optional.empty();
        }
        return customerRep.findByAccount( acc.get());
    }
}
