package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PricingMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceTypeEnum type;

    @Enumerated(EnumType.STRING)
    private OperationTypeEnum operationType;

    private BigDecimal valueAdded;
    private BigDecimal valueRemoved;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String notes;

    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    public User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    public User updatedBy;

    @Column(name = "created_at", nullable = false)

    public LocalDateTime createdAt;

    @Version
    private Long version;

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
