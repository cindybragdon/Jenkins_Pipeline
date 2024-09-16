package edu.service;


import edu.mv.db.models.Rocket;
import edu.mv.models.RocketDTO;
import edu.mv.persistence.PersistenceService;
import edu.mv.service.RocketService;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.Persistence;

@ExtendWith(MokitoExtension.class)
public class RocketServiceTests {

    @Mock
    PersistenceService mockPersistanceService;

    public void testGetRocket() {

        RocketDTO rocketDTOARetourner = new RocketDTO();
        rocketDTOARetourner.setId(1);
        rocketDTOARetourner.setName("Boris");
        rocketDTOARetourner.setType("Blabla");
        when(mockRocketRepository.getAll()).thenReturn(rocketList);

        RocketService rocketService = new RocketService(rockPersistanceService);

        RocketDTO rocketAValider = rocketService.getRocket(1);


    }
    @Test
    public void testSaveRocket() {

        RocketDTO rocketDTOARetourner = new RocketDTO();

        RocketService rocketService = new RocketService(mockPersistanceService);

        rocketService.putRocket(rocketDTOARetourner);

        verify(mockPersistanceService, times(1).save(rocketASauvegarder));
    }

    private RocketDTO creerRocketDTOTest() {
        RocketDTO rocketDTOARetourner = new RocketDTO();
        rocketDTOARetourner.setId(1);
        rocketDTOARetourner.setName("Boris");
        rocketDTOARetourner.setType("Blabla");

        return  rocketDTOARetourner;
    }

}
