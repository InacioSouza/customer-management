package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "base_prices")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasePrices extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal userUnit;
    private BigDecimal officialWhatsAppChannel;
    private BigDecimal unofficialWhatsAppChannel;
    private BigDecimal instagramChannel;
    private BigDecimal messengerChannel;

    @Version
    private Long version;
}
