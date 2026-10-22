package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import br.com.system.i5.customer_management.model.enuns.CustomerStatusEnum;
import br.com.system.i5.customer_management.model.enuns.TypeCompanyEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String alias;

    private String registeredBusinessName;

    private String tradeName;

    private String cnpj;

    @Enumerated(EnumType.STRING)
    private TypeCompanyEnum typeCompany;

    @Column(nullable = false)
    private String niche;

    private Date entryDate;

    private Date departureDate;

    @Column(nullable = false)
    private Boolean hasContract;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatusEnum status;

    @Column(name = "administrator_id", nullable = false)
    private Administrator administrator;

    @Column(name = "platform_cost", nullable = false)
    private BigDecimal platformCost;

    private BigDecimal amountChargedAI;

    @Version
    private Long version;

    public Customer(@NotNull UUID customerId) {
        this.id = customerId;
    }
}