package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.BasePrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BasePricesRepository extends JpaRepository<BasePrices, UUID> {

    @Query("""
            SELECT count(bp)
            FROM BasePrices bp             
    """)
    Long getNumberRecords();
}
