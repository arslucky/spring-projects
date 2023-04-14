package org.demo.ars.domain.order;

import org.demo.ars.domain.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author arsen.ibragimov
 *
 */
@Document
public class Order extends BaseEntity {

    @Id
    private String orderId;

    private String accountNumber;

    private OrderStatus orderStatus;

    public Order() {
        ;
    }

    public Order( String accountNumber) {
        this.accountNumber = accountNumber;
        this.orderStatus = OrderStatus.PENDING;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber( String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus( OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

}
