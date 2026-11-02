package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.BasePricesCustomer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record BasePricesCustomerForm(
        @NotNull @PositiveOrZero
        BigDecimal userUnit,
        @NotNull @PositiveOrZero
        BigDecimal officialWhatsAppChannel,
        @NotNull @PositiveOrZero
        BigDecimal unofficialWhatsAppChannel,
        @NotNull @PositiveOrZero
        BigDecimal instagramChannel,
        @NotNull @PositiveOrZero
        BigDecimal messengerChannel,
        UUID customerId
) {

    public BasePricesCustomer getEntity() {
        var basePricesCustomer = new BasePricesCustomer();
        basePricesCustomer.setUserUnit(this.userUnit());
        basePricesCustomer.setOfficialWhatsAppChannel(this.officialWhatsAppChannel());
        basePricesCustomer.setUnofficialWhatsAppChannel(this.unofficialWhatsAppChannel());
        basePricesCustomer.setInstagramChannel(this.instagramChannel());
        basePricesCustomer.setMessengerChannel(this.messengerChannel());
        return basePricesCustomer;
    }
}
