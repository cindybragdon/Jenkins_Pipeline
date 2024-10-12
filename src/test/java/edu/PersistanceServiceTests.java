package edu;
import edu.mv.persistence.PersistenceService;
import edu.mv.persistence.RocketNotFoundException;
import edu.mv.db.models.Rocket;
import edu.mv.models.RocketDTO;
import edu.mv.repository.RocketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersistenceServiceTest {

    @Mock
    private RocketRepository rocketRepository;

    @InjectMocks
    private PersistenceService persistenceService;

    public PersistenceServiceTest() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testRetrieveRocketSuccess() throws Exception {
        Rocket mockRocket = new Rocket();
        mockRocket.setId(1);
        mockRocket.setName("MiniWheat");
        mockRocket.setSorte("Cereal");

        // Mock the repository behavior
        when(rocketRepository.findById(1)).thenReturn(Optional.of(mockRocket));

        // Act
        RocketDTO result = persistenceService.retrieve(1);

        // Assert
        assertNotNull(result, "RocketDTO is not null");
        assertEquals(mockRocket.getId(), result.getId(), "Rocket have same id");
        assertEquals(mockRocket.getName(), result.getName(), "Same Rocket name");
        assertEquals(mockRocket.getSorte(), result.getType(), "Rocket match sorte/type");

        verify(rocketRepository).findById(1); 
    }

    @Test
    void testRetrieveRocketNotFound() {
        // Mock the repository to return an empty Optional
        when(rocketRepository.findById(2)).thenReturn(Optional.empty());

        // Assert that an exception is thrown
        Exception exception = assertThrows(RocketNotFoundException.class, () -> {
            persistenceService.retrieve(2);
        });

        // Verify exception message
        String expectedMessage = "Rocket with ID 2 not found";
        String actualMessage = exception.getMessage();
        //assertTrue(actualMessage.contains(expectedMessage), "Expected exception message not found");

        verify(rocketRepository).findById(2); // Verify that findById was called
    }

    @Test
    void testSaveRocket() {
        // Create a mock RocketDTO
        RocketDTO rocketDTOToSave = new RocketDTO();
        rocketDTOToSave.setId(3);
        rocketDTOToSave.setName("MINIWHEAT!");
        rocketDTOToSave.setType("SpaceCereal");

        // Act
        persistenceService.save(rocketDTOToSave);

        // Assert
        verify(rocketRepository).save(any(Rocket.class)); 
    }

    @Test
    void testConvertToRocketPersistence() throws Exception {
        // Create a RocketDTO
        RocketDTO rocketDTO = new RocketDTO();
        rocketDTO.setId(4);
        rocketDTO.setName("Doudou");
        rocketDTO.setType("LikeHome");
    
        
        Method method = PersistenceService.class.getDeclaredMethod("convertToRocketPersistence", RocketDTO.class);
        method.setAccessible(true); 
    
        Rocket rocket = (Rocket) method.invoke(persistenceService, rocketDTO);
    
        // Assert the mapping
        assertNotNull(rocket, "Rocket is not null");
        assertEquals(rocketDTO.getId(), rocket.getId(), "ID are the same");
        assertEquals(rocketDTO.getName(), rocket.getName(), "Same name");
        assertEquals(rocketDTO.getType(), rocket.getSorte(), "Type/sorte are same");
    }

    @Test
void testConvertToRocketDTO() throws Exception {
    Rocket rocket = new Rocket();
    rocket.setId(5);
    rocket.setName("Endeavour");
    rocket.setSorte("Shuttle");

    Method method = PersistenceService.class.getDeclaredMethod("convertToRocketDTO", Rocket.class);
    method.setAccessible(true);

    RocketDTO rocketDTO = (RocketDTO) method.invoke(persistenceService, rocket);

    // Debugging output
    System.out.println("RocketDTO ID: " + rocketDTO.getId());
    System.out.println("RocketDTO Name: " + rocketDTO.getName());
    System.out.println("RocketDTO Type: " + rocketDTO.getType());

    assertNotNull(rocketDTO, "RocketDTO is not null");
    assertEquals(rocket.getId(), rocketDTO.getId(), "ID are the same");
    assertEquals(rocket.getName(), rocketDTO.getName(), "Name are the same");
    assertEquals(rocket.getSorte(), rocketDTO.getType(), "Type/sorte are the same");
}
}
