package org.demo.ars.domain.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author arsen.ibragimov
 *
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order getOrder( String account) {
        return orderRepository.findByAccountNumber( account);
    }

    public Order saveOrder( String account) {
        Order order = new Order( account);
        return orderRepository.save( order);
    }

}
