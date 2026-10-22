package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.ResourcesCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResourcesCustomerRepository extends JpaRepository<ResourcesCustomer, UUID> {
}
