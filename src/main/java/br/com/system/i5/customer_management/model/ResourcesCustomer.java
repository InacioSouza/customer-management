package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "resources_customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResourcesCustomer extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private Customer customer;

    @Enumerated(value = EnumType.STRING)
    private ResourceTypeEnum resourceType;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    private BigDecimal totalAmountPaid;

    private BigDecimal averageValueTotalQuantity;

    private BigDecimal averageValueOnlyPaid;

    @Version
    private Long version;

}
