package org.demo.ars.domain.invoice;

import java.util.ArrayList;
import java.util.List;

import org.demo.ars.domain.BaseEntity;
import org.demo.ars.domain.order.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author arsen.ibragimov
 *
 */
@Document
public class Invoice extends BaseEntity {

    @Id
    private String invoiceId;

    private String customerId;

    private List<Order> orders = new ArrayList<>();

    @DBRef
    private List<Order> ordersRef = new ArrayList<>();

    private InvoiceStatus invoiceStatus = InvoiceStatus.CREATED;

    public Invoice() {
        ;
    }

    public Invoice( String customerId) {
        this.customerId = customerId;
    }

    public void addOrder( Order order) {
        this.orders.add( order);
    }

    public void addOrderRef( Order order) {
        this.ordersRef.add( order);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId( String customerId) {
        this.customerId = customerId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders( List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrdersRef() {
        return ordersRef;
    }

    public void setOrdersRef( List<Order> ordersRef) {
        this.ordersRef = ordersRef;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus( InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

}
