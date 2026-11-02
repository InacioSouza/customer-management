package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.AdministratorCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdministratorCustomerRepository extends JpaRepository<AdministratorCustomer, UUID> {

    @Query("""
                  SELECT ac
                  FROM AdministratorCustomer ac
                  WHERE ac.administrator.id = :id
            """)
    AdministratorCustomer findByAdministratorId(UUID id);

    @Query("""
                  SELECT COUNT(ac)
                  FROM AdministratorCustomer ac
                  WHERE ac.customer.id = :customerId
            """)
    int countAdministratorsForCustomerId(UUID customerId);
}