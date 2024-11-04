package edu.mv.mv.service;

import edu.mv.mv.models.RocketDTO;
import edu.mv.mv.persistence.PersistenceService;
import edu.mv.mv.persistence.RocketNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RocketService {
    private PersistenceService persistenceService;

    public RocketService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public RocketDTO getRocket(int id) throws RocketNotFoundException {
        return persistenceService.retrieve(id);
    }

    public void putRocket(RocketDTO rocketDTO) {
        persistenceService.save(rocketDTO);
    }
}
