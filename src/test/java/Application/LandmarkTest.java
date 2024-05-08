package Application;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LandmarkTest {
    @Test
    public void testLandmark() {
        // Create a new landmark
        Landmark landmark = new Landmark("Landmark1", "Street1", 10, 20, "CulturalValue1");

        // Check the name
        assertEquals("Landmark1", landmark.getName());

        // Check the street
        assertEquals("Street1", landmark.getStreet());

        // Check the location
        assertEquals(10, landmark.getX());
        assertEquals(20, landmark.getY());

        // Check the cultural value
        assertEquals("CulturalValue1", landmark.getCulturalValue());

        // Check the toString method
        String expectedString = "Landmark: Name: Landmark1, Street: Street1, Location: (10, 20), Cultural Value: CulturalValue1";
        assertEquals(expectedString, landmark.toString());
    }
}

