package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import br.com.system.i5.customer_management.model.enuns.DocumentTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "document")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;
    private DocumentTypeEnum type;
    private String documentURL;
    private byte[] content;

    @Version
    private Long version;
}
