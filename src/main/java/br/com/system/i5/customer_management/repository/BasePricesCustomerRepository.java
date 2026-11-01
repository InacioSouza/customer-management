package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.BasePricesCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BasePricesCustomerRepository extends JpaRepository<BasePricesCustomer, UUID> {

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM BasePricesCustomer b
            WHERE b.customer.id = :customerId
            """)
    boolean existsBasePriceByCustomerId(@Param("customerId") UUID customerId);

}
