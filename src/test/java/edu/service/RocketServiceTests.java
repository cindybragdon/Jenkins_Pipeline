package edu.service;


import edu.mv.db.models.Rocket;
import edu.mv.models.RocketDTO;
import edu.mv.persistence.PersistenceService;
import edu.mv.repository.RocketRepository;
import edu.mv.service.RocketService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.*;

@ExtendWith(MockitoExtension.class)
public class RocketServiceTests {

    @Mock
    PersistenceService mockPersistanceService;


    RocketRepository rocketRepository;

    @Test
    public void testGetDTORocket() throws Exception {
        RocketDTO rocketDTOARetourner = new RocketDTO();
        rocketDTOARetourner.setId(1);
        rocketDTOARetourner.setName("MiniWheat");
        rocketDTOARetourner.setType("FuseeInterstellaire");

        RocketDTO rocketDTOError = new RocketDTO();
        rocketDTOError.setId(2);
        rocketDTOError.setName("Error");
        rocketDTOError.setType("ErrorType");

        when(mockPersistanceService.retrieve(1)).thenReturn(rocketDTOARetourner);

        RocketService rocketService = new RocketService(mockPersistanceService); // Inject mock here

        RocketDTO rocketAValider = rocketService.getRocket(1);
        RocketDTO rocketAValiderNull = rocketService.getRocket(0131);
        assertEquals("Same Rocket",rocketDTOARetourner,rocketAValider);
        assertEquals("Got the right name, MiniWheat ", "MiniWheat", rocketAValider.getName());
        assertNotEquals("Got the wrong name, Snoop Dogg instead of MiniWheat ", "MiniWheat", rocketAValider);
        assertNotEquals("Not same Rocket",rocketDTOARetourner,rocketDTOError);
        assertTrue("Rocket is named", rocketAValider.getName() != null && !rocketAValider.getName().isEmpty());
        assertTrue("Rocket is typed", rocketAValider.getType() != null && !rocketAValider.getType().isEmpty());
        assertNotNull("Rocket is not null", rocketAValider);
        assertNull("Rocket is null", rocketAValiderNull);
    }

    @Test
    public void testSaveDTORocket() throws Exception {


        RocketDTO rocketDTOASauvegarder = new RocketDTO();
        rocketDTOASauvegarder.setId(2);
        rocketDTOASauvegarder.setName("Boris");
        rocketDTOASauvegarder.setType("Blabla");

        RocketDTO rocketDTOAErreur = new RocketDTO();
        rocketDTOASauvegarder.setId(3);
        rocketDTOASauvegarder.setName("Erreur");
        rocketDTOASauvegarder.setType("ErreurType");

        RocketService rocketService = new RocketService(mockPersistanceService);

        rocketService.putRocket(rocketDTOASauvegarder);

        verify(mockPersistanceService).save(rocketDTOASauvegarder);
        when(mockPersistanceService.retrieve(2)).thenReturn(rocketDTOASauvegarder);
        RocketDTO rocketAValider = rocketService.getRocket(2);
        RocketDTO rocketAValiderNull = rocketService.getRocket(0131);

        assertEquals("Same Rocket",rocketDTOASauvegarder,rocketAValider);
        assertNotEquals("Not same Rocket",rocketDTOAErreur,rocketAValider);
        //assertEquals("Got the right name,MiniWheat ", "MiniWheat", rocketAValider.getName());
        assertNotEquals("Got the wrong name, Snoop Dogg instead of MiniWheat ", "MiniWheat", rocketAValider);
        assertTrue("Rocket is named", rocketAValider.getName() != null && !rocketAValider.getName().isEmpty());
        assertTrue("Rocket is typed", rocketAValider.getType() != null && !rocketAValider.getType().isEmpty());
        assertNotNull("Rocket is not null", rocketAValider);
        assertNull("Rocket is null", rocketAValiderNull);
    }

}
