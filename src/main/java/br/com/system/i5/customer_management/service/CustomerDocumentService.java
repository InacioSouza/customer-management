package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.CustomerDocument;
import br.com.system.i5.customer_management.records.form.CustomerDocumentForm;
import br.com.system.i5.customer_management.repository.CustomerDocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerDocumentService {

    private final CustomerDocumentRepository customerDocumentRepository;
    private final CustomerService customerService;
    private final DocumentService documentService;

    public CustomerDocument create(CustomerDocumentForm input) {
        var customerDocument = new CustomerDocument();

        if (!customerService.existsById(input.customerID())) {
            throw new IllegalArgumentException(
                    "There is no customer record for the Id: " + input.customerID());
        }

        customerDocument.setCustomer(customerService.getCustomerByID(input.customerID()));
        customerDocument.setDocument(documentService.save(input.getDocumentEntity()));

        return customerDocumentRepository.save(customerDocument);
    }

    @Transactional
    public void delete(UUID id) {

        var customerDocument = customerDocumentRepository.findById(id);

        if (customerDocument.isEmpty()) {
            throw new IllegalArgumentException(
                    "There is no CustomerDocument for the ID:" + id);
        }

        List<UUID> listIdDocuments = customerDocumentRepository
                .findOthersRelationshipsExceptListId(
                        customerDocument.get().getDocument().getId(),
                        List.of(id)
                );
        if (listIdDocuments.isEmpty()) {
            documentService.deleteById(customerDocument.get().getDocument().getId());
        }

        customerDocumentRepository.deleteById(id);
    }
}
