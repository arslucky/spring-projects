package org.demo.ars.domain.customer;

import org.demo.ars.domain.BaseEntity;
import org.demo.ars.domain.account.Account;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * @author arsen.ibragimov
 *
 */
@Entity
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @OneToOne( cascade = CascadeType.ALL)
    private Account account;

    public Customer() {
        ;
    }

    public Customer( String firstName, String lastName, String email, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount( Account account) {
        this.account = account;
    }

}
