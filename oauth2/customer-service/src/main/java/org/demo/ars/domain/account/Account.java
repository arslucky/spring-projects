package org.demo.ars.domain.account;

import org.demo.ars.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @author arsen.ibragimov
 *
 */
@Entity
public class Account extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    public Account() {
        ;
    }

    public Account( String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber( String accountNumber) {
        this.accountNumber = accountNumber;
    }

}
