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
    private BigDecimal AIModule;
    private BigDecimal officialWhatsAppChannel;
    private BigDecimal unofficialWhatsAppChannel;
    private BigDecimal instagramChannel;
    private BigDecimal messengerChannel;

    @Version
    private Long version;

    public BasePrices(
            BigDecimal userUnit,
            BigDecimal AIModule,
            BigDecimal officialWhatsAppChannel,
            BigDecimal unofficialWhatsAppChannel,
            BigDecimal instagramChannel,
            BigDecimal messengerChannel
    ) {
        this.userUnit = userUnit;
        this.AIModule = AIModule;
        this.officialWhatsAppChannel = officialWhatsAppChannel;
        this.unofficialWhatsAppChannel = unofficialWhatsAppChannel;
        this.instagramChannel = instagramChannel;
        this.messengerChannel = messengerChannel;
    }
}
