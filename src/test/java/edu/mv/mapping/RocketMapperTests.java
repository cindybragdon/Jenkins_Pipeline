package edu.mv.mapping;


import edu.mv.mv.db.models.Rocket;
import edu.mv.mv.mapping.RocketMapper;
import edu.mv.mv.models.RocketDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RocketMapperTests {

    private final RocketMapper mapper = RocketMapper.INSTANCE;

    @Test
    public void testRocketToRocketDTO() {
        Rocket rocket = new Rocket(1, "MiniWheat", "FuseeInterstellaire");

        RocketDTO rocketDTO = mapper.RocketToRocketDTO(rocket);

        assertNotNull(rocketDTO);
        assertEquals(rocket.getId(), rocketDTO.getId());
        assertEquals(rocket.getName(), rocketDTO.getName());
        assertEquals(rocket.getSorte(), rocketDTO.getType());
    }

    @Test
    public void testRocketDTOToRocket() {
        RocketDTO rocketDTO = new RocketDTO(2, "Boris", "BlablaType");

        Rocket rocket = mapper.RocketDTOToRocket(rocketDTO);

        assertNotNull(rocket);
        assertEquals(rocketDTO.getId(), rocket.getId());
        assertEquals(rocketDTO.getName(), rocket.getName());
        assertEquals(rocketDTO.getType(), rocket.getSorte());
    }

    @Test
    public void testNullMapping() {
        assertNull(mapper.RocketToRocketDTO(null));
        assertNull(mapper.RocketDTOToRocket(null));
    }
}