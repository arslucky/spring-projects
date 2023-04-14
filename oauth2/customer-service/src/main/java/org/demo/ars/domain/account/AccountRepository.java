package org.demo.ars.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arsen.ibragimov
 *
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber( String accountNumber);
}
