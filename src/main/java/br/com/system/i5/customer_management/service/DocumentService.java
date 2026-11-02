package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.Document;
import br.com.system.i5.customer_management.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document save(Document document) {

        if (document.getName() == null || document.getName().isBlank()) {
            throw new IllegalArgumentException("The name attribute cannot be null");
        }

        if (document.getType() == null) {
            throw new IllegalArgumentException("The type attribute cannot be null");
        }

        if (document.getContent() == null || document.getContent().length == 0) {
            throw new IllegalArgumentException("The document must have some content");
        }

        return documentRepository.save(document);
    }
}
