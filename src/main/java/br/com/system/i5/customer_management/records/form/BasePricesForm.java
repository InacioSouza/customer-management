package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.BasePrices;

import java.math.BigDecimal;

public record BasePricesForm(
        BigDecimal userUnit,
        BigDecimal AIModule,
        BigDecimal officialWhatsAppChannel,
        BigDecimal unofficialWhatsAppChannel,
        BigDecimal instagramChannel,
        BigDecimal messengerChannel
) {

    public BasePrices getEntity () {
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
