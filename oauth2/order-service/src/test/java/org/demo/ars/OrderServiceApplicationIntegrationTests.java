package org.demo.ars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.demo.ars.domain.invoice.Invoice;
import org.demo.ars.domain.invoice.InvoiceRepository;
import org.demo.ars.domain.invoice.InvoiceStatus;
import org.demo.ars.domain.order.Order;
import org.demo.ars.domain.order.OrderRepository;
import org.demo.ars.domain.order.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.EnabledIf;

//@formatter:off
//@EnabledIf( "#{systemProperties['group-tests'] != null "
//        + "and (systemProperties['group-tests'].toLowerCase().contains('integration-tests')"
//                + "or systemProperties['group-tests'].toLowerCase().contains('ms-integration-tests'))}")
//@formatter:on
@EnabledIfSystemProperty( named = "group-tests", matches = "integration-tests|ms-integration-tests")
@ActiveProfiles( "test")
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderServiceApplicationIntegrationTests {

    @Autowired
    private OrderRepository orderRep;

    @Autowired
    private InvoiceRepository invoiceRep;

    @BeforeEach
    @AfterEach
    public void reset() {
        orderRep.deleteAll();
        invoiceRep.deleteAll();
    }

    @Test
    public void orderTest() {
        Order order = new Order( "12345");

        order = orderRep.save( order);

        assertNotNull( order.getOrderId());
        assertNotNull( order.getCreatedAt());
        assertNotNull( order.getLastModified());

        assertEquals( order.getOrderStatus(), OrderStatus.PENDING);
        assertEquals( order.getAccountNumber(), "12345");
	}

    @Test
    public void invoiceTest() {
        Order order = new Order( "a12345");
        Order orderRef = new Order( "a12345");

        orderRef = orderRep.save( orderRef);

        assertNotNull( orderRef.getOrderId());

        Invoice invoice = new Invoice( "1");
        invoice.setCustomerId( "c12345");

        invoice.addOrder( order);
        invoice.addOrderRef( orderRef);

        invoice = invoiceRep.save( invoice);

        assertNotNull( invoice.getInvoiceId());
        assertNotNull( invoice.getCreatedAt());
        assertNotNull( invoice.getLastModified());

        assertEquals( invoice.getInvoiceStatus(), InvoiceStatus.CREATED);
        assertEquals( invoice.getOrders().size(), 1);
        assertEquals( invoice.getOrdersRef().size(), 1);
        // ------------------------------------------------------//

        invoice = invoiceRep.findByCustomerId( "c12345");

        assertEquals( invoice.getCustomerId(), "c12345");
    }

}
