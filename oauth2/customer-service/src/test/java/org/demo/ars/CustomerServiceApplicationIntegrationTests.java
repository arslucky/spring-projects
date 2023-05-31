package org.demo.ars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.demo.ars.domain.account.Account;
import org.demo.ars.domain.account.AccountRepository;
import org.demo.ars.domain.customer.Customer;
import org.demo.ars.domain.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;

//@formatter:off
@EnabledIf( "#{systemProperties['group-tests'] != null "
        + "and (systemProperties['group-tests'].toLowerCase().contains('integration-tests')"
                + "or systemProperties['group-tests'].toLowerCase().contains('ms-integration-tests'))}")
//@formatter:on
@ActiveProfiles( "test")
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerServiceApplicationIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Environment env;

    @Autowired
    private TestRestTemplate template;

    private static String path = "http://localhost";

	@Test
    public void customerTest() {
        Account account = new Account( "12345");
        Customer customer = new Customer( "John", "Smith", "j.smith@test.org", account);

        customer = customerRepository.save( customer);

        Optional<Customer> persistedResult = customerRepository.findById( customer.getId());

        assertTrue( persistedResult.isPresent());

        Customer cust = persistedResult.get();

        assertNotNull( cust.getCreatedAt());
        assertNotNull( cust.getLastModified());
        assertEquals( cust.getFirstName(), "John");
        assertEquals( cust.getLastName(), "Smith");
        assertEquals( cust.getEmail(), "j.smith@test.org");

        Account accnt = cust.getAccount();

        assertNotNull( accnt);
        assertEquals( accnt.getAccountNumber(), "12345");
	}

    @Test
    public void accountTest() {
        Account account = new Account( "12345");

        account = accountRepository.save( account);

        Optional<Account> persistedResult = accountRepository.findByAccountNumber( "12345");

        assertTrue( persistedResult.isPresent());
        assertEquals( persistedResult.get().getAccountNumber(), "12345");
    }

    @Test
    public void restApi() {
        String pathRest = String.format( "%s:%s%s", path, port, env.getProperty( "spring.data.rest.base-path"));
        ResponseEntity<String> response = template.getForEntity( pathRest, String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();

        assertNotNull( body);

        assertTrue( body.contains( "_links"));
        assertTrue( body.contains( "customers"));
        assertTrue( body.contains( "profile"));
    }

}
