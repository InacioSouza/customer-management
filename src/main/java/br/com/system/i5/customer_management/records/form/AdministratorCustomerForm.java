package br.com.system.i5.customer_management.records.form;

import java.util.UUID;

public record AdministratorCustomerForm(
        UUID customerId,
        UUID administratorID
) {

}
