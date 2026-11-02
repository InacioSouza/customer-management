package br.com.system.i5.customer_management.repository;

import br.com.system.i5.customer_management.model.CustomerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, UUID> {

    @Query("""
            SELECT cd.document.id FROM CustomerDocument cd
            WHERE cd.id NOT IN :listId AND cd.document.id = :documentId
            """)
    List<UUID> findOthersRelationshipsExceptListId(
            @Param("listId") UUID documentId,
            @Param("listId") List<UUID> listId
    );
}
