package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.Document;
import br.com.system.i5.customer_management.model.enuns.DocumentTypeEnum;

import java.util.UUID;

public record CustomerDocumentForm(
        UUID customerID,
        String name,
        String description,
        DocumentTypeEnum type,
        byte[] content
) {

    public Document getDocumentEntity() {
        var document = new Document();
        document.setName(name);
        document.setDescription(description);
        document.setType(type);
        document.setContent(content);

        return document;
    }
}
