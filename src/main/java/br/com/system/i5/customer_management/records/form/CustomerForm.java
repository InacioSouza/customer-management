package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.enuns.CustomerStatusEnum;
import br.com.system.i5.customer_management.model.enuns.TypeCompanyEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CustomerForm(

        String alias,
        String registeredBusinessName,
        String tradeName,
        String cnpj,
        TypeCompanyEnum typeCompany,
        String niche,
        Date entryDate,
        Date departureDate,
        Boolean hasContract,
        Boolean hasLgpdContract,
        CustomerStatusEnum status,
        UUID administratorId,
        BigDecimal platformCost,
        BigDecimal amountChargedAI,

        // Preços base do cliente
        Boolean useDefaultPrices,
        BigDecimal userUnit,
        BigDecimal officialWhatsAppChannel,
        BigDecimal unofficialWhatsAppChannel,
        BigDecimal instagramChannel,
        BigDecimal messengerChannel,

        // Lista de recursos
        List<GeneralInformationCustomerResources> listGeneralResources
) {
}
