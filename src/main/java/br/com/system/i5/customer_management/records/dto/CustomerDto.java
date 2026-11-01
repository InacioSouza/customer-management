package br.com.system.i5.customer_management.records.dto;

import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.model.enuns.CustomerStatusEnum;
import br.com.system.i5.customer_management.model.enuns.TypeCompanyEnum;

import java.util.UUID;

public record CustomerDto(
        UUID id,
        String alias,
        TypeCompanyEnum typeCompany,
        String niche,
        CustomerStatusEnum status
) {

    public CustomerDto(Customer customerSaved) {
        this(
                customerSaved.getId(),
                customerSaved.getAlias(),
                customerSaved.getTypeCompany(),
                customerSaved.getNiche(),
                customerSaved.getStatus()
        );
    }
}
