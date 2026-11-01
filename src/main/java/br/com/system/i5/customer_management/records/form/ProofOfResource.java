package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.enuns.DocumentTypeEnum;

public record ProofOfResource(
        String notes,
        byte[] file,
        DocumentTypeEnum type
) {
}
