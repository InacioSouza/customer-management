package br.com.system.i5.customer_management.records.form;

import java.util.UUID;

public record AdministratorForm(
        String administratorName,
        String administratorPhone,
        UUID customerId
) {
}
