package br.com.system.i5.customer_management.service.domain;

import br.com.system.i5.customer_management.model.Administrator;
import br.com.system.i5.customer_management.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public List<UUID> findIdsNotIn(List<UUID> listIds) {
        return administratorRepository.findIdsNotIn(listIds);
    }

    public Administrator findById(UUID id) {
        return administratorRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return administratorRepository.existsById(id);
    }

    public Administrator save(Administrator administrator) {
        return administratorRepository.save(administrator);
    }
}
