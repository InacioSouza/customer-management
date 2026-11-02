package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.BasePrices;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BasePricesForm(
        @NotNull @PositiveOrZero
        BigDecimal userUnit,
        @NotNull @PositiveOrZero
        BigDecimal AIModule,
        @NotNull @PositiveOrZero
        BigDecimal officialWhatsAppChannel,
        @NotNull @PositiveOrZero
        BigDecimal unofficialWhatsAppChannel,
        @NotNull @PositiveOrZero
        BigDecimal instagramChannel,
        @NotNull @PositiveOrZero
        BigDecimal messengerChannel
) {

    public BasePrices getEntity() {
        return new BasePrices(
                this.userUnit,
                this.AIModule,
                this.officialWhatsAppChannel,
                this.unofficialWhatsAppChannel,
                this.instagramChannel,
                this.messengerChannel
        );
    }
}
