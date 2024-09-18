package edu.mv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.mv.models.RocketDTO;
import edu.mv.persistence.PersistenceService;
import edu.mv.persistence.RocketNotFoundException;

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
