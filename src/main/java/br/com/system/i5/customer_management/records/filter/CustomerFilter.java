package br.com.system.i5.customer_management.records.filter;

import br.com.system.i5.customer_management.model.enuns.CustomerStatusEnum;
import br.com.system.i5.customer_management.model.enuns.TypeCompanyEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CustomerFilter(

        String alias,
        String registeredBusinessName,
        String tradeName,
        String cnpj,
        TypeCompanyEnum typeCompany,
        String niche,

        Date minEntryDate,
        Date maxEntryDate,

        Date minDepartureDate,
        Date maxDepartureDate,

        Boolean hasContract,
        Boolean hasLgpdContract,
        CustomerStatusEnum status,
        List<UUID> listAdministratorId,

        BigDecimal minPlatformCost,
        BigDecimal maxPlatformCost,

        BigDecimal minAmountChargedAI,
        BigDecimal maxAmountChargedAI,

        Integer minQuantityUsers,
        Integer maxQuantityUsers,

        BigDecimal minAverageAmountChargedForUser,
        BigDecimal maxAverageAmountChargedForUser,

        Integer minQuantityOfficialWhatsappChannel,
        Integer maxQuantityOfficialWhatsappChannel,

        BigDecimal minAverageAmountChargedForOfficialWhat,
        BigDecimal maxAverageAmountChargedForOfficialWhat,

        Integer minQuantityUnofficialWhatsappChannel,
        Integer maxQuantityUnofficialWhatsappChannel,

        BigDecimal minAverageAmountChargedForUnofficialWhats,
        BigDecimal maxAverageAmountChargedForUnofficialWhats,

        Integer minQuantityMessengerChannel,
        Integer maxQuantityMessengerChannel,

        BigDecimal minAverageAmountChargedForMessengerChannel,
        BigDecimal maxAverageAmountChargedForMessengerChannel,

        Integer minQuantityInstagramChannel,
        Integer maxQuantityInstagramChannel,

        BigDecimal minAverageAmountChargedForInstagramChannel,
        BigDecimal maxAverageAmountChargedForInstagramChannel,

        BigDecimal userUnitBasePrice,
        BigDecimal AIModuleBasePrice,
        BigDecimal officialWhatsChannelBasePrice,
        BigDecimal unofficialWhatsChannelBasePrice,
        BigDecimal instagramChannelBasePrice,
        BigDecimal messengerChannelBasePrice
) {
}
