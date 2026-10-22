package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.BasePrices;
import br.com.system.i5.customer_management.records.form.BasePricesForm;
import br.com.system.i5.customer_management.repository.BasePricesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasePricesService {

    private BasePricesRepository basePricesRepository;

    public BasePrices create(BasePricesForm input) {
        if (basePricesRepository.getNumberRecords() != 0) {
            throw new IllegalArgumentException("Only one BasePrices record is permitted.");
        }
        return basePricesRepository.save(input.getEntity());
    }

    public BasePrices update(UUID id, BasePricesForm input) {
        if (basePricesRepository.existsById(id)) {
            throw new IllegalArgumentException("There is no BasePrices record for the ID:" + id);
        }
        var basePrices = input.getEntity();
        basePrices.setId(id);

        return basePricesRepository.save(basePrices);
    }
}
