package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {

    @Query("""
                SELECT id
                FROM Customer
                WHERE id NOT IN :ids
            """)
    List<UUID> findIdsNotIn(@Param("ids") List<UUID> ids);

}
