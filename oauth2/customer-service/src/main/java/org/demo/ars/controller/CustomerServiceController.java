package org.demo.ars.controller;

import java.util.Optional;

import org.demo.ars.domain.account.Account;
import org.demo.ars.domain.account.AccountService;
import org.demo.ars.domain.customer.Customer;
import org.demo.ars.domain.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arsen.ibragimov
 *
 */
@RestController
public class CustomerServiceController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping( path = "/getCustomer/{account}")
    Customer getCustomer( @PathVariable String account) {
        Optional<Customer> cust = customerService.getByAccount( account);
        return cust.isPresent() ? cust.get() : null;
    }

    @GetMapping( path = "/getAccount/{account}")
    Account getAccount( @PathVariable String account) {
        Optional<Account> acct = accountService.getAccount( account);
        return acct.isPresent() ? acct.get() : null;
    }

}
