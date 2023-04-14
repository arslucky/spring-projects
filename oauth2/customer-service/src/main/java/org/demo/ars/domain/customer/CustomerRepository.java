package org.demo.ars.domain.customer;

import java.util.Optional;

import org.demo.ars.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arsen.ibragimov
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByAccount( Account account);

    Optional<Customer> findByEmail( String email);
}
