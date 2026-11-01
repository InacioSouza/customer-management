package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.model.enuns.CustomerStatusEnum;
import br.com.system.i5.customer_management.model.enuns.TypeCompanyEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CustomerForm(

        @NotBlank
        String alias,
        String registeredBusinessName,
        String tradeName,
        String cnpj,
        @NotNull
        TypeCompanyEnum typeCompany,
        @NotBlank
        String niche,
        Date entryDate,
        Date departureDate,
        @NotNull
        Boolean hasContract,
        @NotNull
        Boolean hasLgpdContract,
        @NotNull
        CustomerStatusEnum status,
        @NotNull @NotEmpty
        List<UUID> listAdministratorId,
        @NotNull @PositiveOrZero
        BigDecimal platformCost,
        @NotNull @PositiveOrZero
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

    public Customer getEntity() {
        var customer = new Customer();

        customer.setAlias(alias);
        customer.setRegisteredBusinessName(registeredBusinessName);
        customer.setTradeName(tradeName);
        customer.setCnpj(cnpj);
        customer.setTypeCompany(typeCompany);
        customer.setNiche(niche);
        customer.setEntryDate(entryDate);
        customer.setDepartureDate(departureDate);
        customer.setHasContract(hasContract);
        customer.setHasLgpdContract(hasLgpdContract);
        customer.setStatus(status);
        customer.setPlatformCost(platformCost);
        customer.setAmountChargedAI(amountChargedAI);

        return customer;
    }
}
