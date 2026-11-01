package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResourcesCustomerRepository extends JpaRepository<ResourcesCustomer, UUID> {

    @Query("""
            SELECT r 
            FROM ResourcesCustomer r
            WHERE r.resourceType = :resourceType AND r.customer.id = :customerId
            """)
    ResourcesCustomer findResourceByTypeAndCustomer(
            @Param("resourceType") ResourceTypeEnum resourceType,
            @Param("customerId") UUID customerId
    );
}
