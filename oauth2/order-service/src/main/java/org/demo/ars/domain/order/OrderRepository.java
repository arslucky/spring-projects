package org.demo.ars.domain.order;

import org.springframework.data.repository.CrudRepository;

/**
 * @author arsen.ibragimov
 *
 */
public interface OrderRepository extends CrudRepository<Order, String> {

    Order findByAccountNumber( String account);
}
