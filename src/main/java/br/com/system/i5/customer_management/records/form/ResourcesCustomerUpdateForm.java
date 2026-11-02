package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;

import java.math.BigDecimal;

public record ResourcesCustomerUpdateForm(
        ResourceTypeEnum resourceType,
        Integer totalQuantity,
        Integer totalBonus,
        BigDecimal totalAmountPaid,
        String notes,
        byte[] photo
) {
}
