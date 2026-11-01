package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.BasePricesCustomer;

import java.math.BigDecimal;
import java.util.UUID;

public record BasePricesCustomerForm(
        BigDecimal userUnit,
        BigDecimal officialWhatsAppChannel,
        BigDecimal unofficialWhatsAppChannel,
        BigDecimal instagramChannel,
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
