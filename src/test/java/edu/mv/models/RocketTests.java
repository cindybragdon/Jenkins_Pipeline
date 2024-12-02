package edu.mv.models;

import edu.mv.mv.db.models.Rocket;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RocketTests {

    @Test
    public void testConstructorAndGetterSetter() {
        Rocket rocket = new Rocket(1, "MiniWheat", "FuseeInterstellaire");

        assertEquals(1, rocket.getId());
        assertEquals("MiniWheat", rocket.getName());
        assertEquals("FuseeInterstellaire", rocket.getSorte());

        rocket.setName("Boris");
        assertEquals("Boris", rocket.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        Rocket rocket1 = new Rocket(1, "MiniWheat", "FuseeInterstellaire");
        Rocket rocket2 = new Rocket(1, "MiniWheat", "FuseeInterstellaire");

        assertEquals(rocket1, rocket2);
        assertEquals(rocket1.hashCode(), rocket2.hashCode());

        rocket2.setName("Boris");
        assertNotEquals(rocket1, rocket2);
    }

    @Test
    public void testToString() {
        Rocket rocket = new Rocket(1, "MiniWheat", "FuseeInterstellaire");

        assertTrue(rocket.toString().contains("MiniWheat"));
        assertTrue(rocket.toString().contains("FuseeInterstellaire"));
    }

    @Test
    public void testDefaultConstructor() {
        Rocket rocket = new Rocket();

        assertEquals(0, rocket.getId());
        assertNull(rocket.getName());
        assertNull(rocket.getSorte());
    }
}
