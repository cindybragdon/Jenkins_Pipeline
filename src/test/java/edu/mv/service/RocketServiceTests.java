package edu.mv.service;

import edu.mv.mv.RestApiApp;
import edu.mv.mv.controller.RocketController;
import edu.mv.mv.db.models.Rocket;
import edu.mv.mv.mapping.RocketMapperImpl;
import edu.mv.mv.models.RocketDTO;
import edu.mv.mv.models.RocketResponse;
import edu.mv.mv.persistence.PersistenceService;
import edu.mv.mv.service.RocketService;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest(classes = RestApiApp.class)
@ExtendWith(MockitoExtension.class)

public class RocketServiceTests {

    @Mock
    PersistenceService mockPersistanceService;



    @Autowired
    PersistenceService persistenceService;

    @Autowired
    private RocketController rocketController;


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
        assertEquals("Same Rocket",rocketDTOARetourner,rocketAValider);
        assertNotEquals("Not same Rocket",rocketDTOARetourner,rocketDTOError);
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

        System.out.println(rocketDTOASauvegarder.getId());
        System.out.println(rocketDTOASauvegarder.getName());
        System.out.println(rocketDTOASauvegarder.getType());
        RocketService rocketService = new RocketService(mockPersistanceService);

        rocketService.putRocket(rocketDTOASauvegarder);

        verify(mockPersistanceService).save(rocketDTOASauvegarder);
        when(mockPersistanceService.retrieve(2)).thenReturn(rocketDTOASauvegarder);
        RocketDTO rocketAValider = rocketService.getRocket(2);

        System.out.println(rocketAValider.getName());
        assertEquals("Same Rocket",rocketDTOASauvegarder,rocketAValider);
        assertNotEquals("Not same Rocket",rocketDTOAErreur,rocketAValider);

        assertEquals("Check the id of the rocket",rocketAValider.getId(), rocketDTOASauvegarder.getId());
        assertEquals("Check the name of the rocket",rocketAValider.getName(), rocketDTOASauvegarder.getName());
        assertEquals("Check the type of the rocket",rocketAValider.getType(), rocketDTOASauvegarder.getType());

    }

    @Test
    public void testCreateRocket() throws Exception {


        Rocket rocketTest = new Rocket();
        rocketTest.setId(1);
        rocketTest.setName("TestRocket");
        rocketTest.setSorte("Fusee");

        Rocket rocketTest2 = new Rocket();
        rocketTest2.setId(2);
        rocketTest2.setName("HamburgerRockey");
        rocketTest2.setSorte("Bateau");

        assertNotEquals("Rocket not same id",rocketTest.getId(),rocketTest2.getId());
        assertNotEquals("Rocket not same name",rocketTest.getName(),rocketTest2.getName());
        assertNotEquals("Rocket not same sorte",rocketTest.getSorte(),rocketTest2.getSorte());
    }

    @Test
    public void testGetAndSetHttpStatus() throws Exception {

        RocketResponse rocketResponse = new RocketResponse();

        rocketResponse.setHttpStatusCode(1);


        assertEquals("Getting the http status code : ",rocketResponse.getHttpStatusCode(), 1);

    }


    @Test
    public void testPersistanceService() throws Exception {


        RocketDTO rocketDTOTest = new RocketDTO();
        rocketDTOTest.setId(1);
        rocketDTOTest.setName("TestRocket");
        rocketDTOTest.setType("Rocket");


        persistenceService.save(rocketDTOTest);

        RocketDTO rocketRetournee = persistenceService.retrieve(1);

        System.out.println(rocketRetournee.getName());
        assertEquals("Verifying save and retrieve persistence service :", rocketDTOTest.getId(), rocketRetournee.getId());
        assertEquals("Verifying save and retrieve persistence service :", rocketDTOTest.getName(), rocketRetournee.getName());
        assertEquals("Verifying save and retrieve persistence service :", rocketDTOTest.getType(), rocketRetournee.getType());


    }

    @Test
    public void testRocketToRocketDTO() throws Exception {


        RocketDTO rocketDTOTest = new RocketDTO();
        rocketDTOTest.setId(1);
        rocketDTOTest.setName("TestRocket");
        rocketDTOTest.setType("Rocket");

        RocketMapperImpl rocketMapper = new RocketMapperImpl();
        RocketDTO rocketDTOEmpty = rocketMapper.RocketToRocketDTO(null);
        Rocket rocketEmpty = rocketMapper.RocketDTOToRocket(null);
        assertEquals("Verifying the rocketDTO Is Null", null, rocketDTOEmpty);
        assertEquals("Verifying the rocketDTO Is Null", null, rocketEmpty);
    }

    @Test
    public void testRocketController() throws Exception {


        RocketDTO rocketDTOTest = new RocketDTO();
        rocketDTOTest.setId(1);
        rocketDTOTest.setName("TestRocket");
        rocketDTOTest.setType("Rocket");

        rocketController.saveRocket(rocketDTOTest);


        RocketDTO rocketDTORetreived = rocketController.getRocket("1");

        try{
            RocketDTO rocketDTORetreived2 = rocketController.getRocket("2");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        assertEquals("Verifying the setter and getter of RocketDTO works", rocketDTOTest,rocketDTORetreived);
    }


    @Test
    public void testApiApp() {
        try (MockedStatic<SpringApplication> mockSpringApplication = mockStatic(SpringApplication.class)) {
            mockSpringApplication.when(() -> SpringApplication.run(RestApiApp.class, new String[]{}))
                    .thenReturn(null);

            assertDoesNotThrow(() -> RestApiApp.main(new String[]{}),
                    "Test RestApiApp...");
        }
    }


}