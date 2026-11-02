package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.CustomerDocument;
import br.com.system.i5.customer_management.records.form.CustomerDocumentForm;
import br.com.system.i5.customer_management.repository.CustomerDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
