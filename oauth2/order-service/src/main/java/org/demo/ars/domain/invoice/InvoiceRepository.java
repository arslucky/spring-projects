package org.demo.ars.domain.invoice;

import org.springframework.data.repository.CrudRepository;

/**
 * @author arsen.ibragimov
 *
 */
public interface InvoiceRepository extends CrudRepository<Invoice, String> {

    Invoice findByCustomerId( String customerId);
}
