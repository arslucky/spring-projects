package org.demo.ars.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.demo.ars.domain.order.Order;
import org.demo.ars.domain.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arsen.ibragimov
 *
 */
@RestController
public class OrderServiceController {

    @Autowired
    private OrderService orderService;

    @GetMapping( path = "/getOrder/{account}")
    Order getOrder( @PathVariable String account) {
        return orderService.getOrder( account);
    }

    @PostMapping( path = "/saveOrder/{account}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Order saveOrder( @PathVariable String account) {
        return orderService.saveOrder( account);
    }
}
