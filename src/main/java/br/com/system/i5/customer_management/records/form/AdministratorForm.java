package br.com.system.i5.customer_management.records.form;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AdministratorForm(
        @NotBlank
        String administratorName,
        @NotBlank
        String administratorPhone,
        UUID customerId
) {
}
