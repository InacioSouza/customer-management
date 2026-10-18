package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "administrators")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Administrator extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "administrator_name", nullable = false)
    private String administratorName;

    @Column(name = "administrator_phone", nullable = false)
    private String administratorPhone;

    @Version
    private Long version;
}
