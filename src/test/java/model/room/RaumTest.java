package model.room;

import junit.framework.TestCase;

public class RaumTest extends TestCase {
    public void testCreateRaum(){
        Raum raum = new Raum("Wohnzimmer");
        assertEquals("Wohnzimmer", raum.getName());
        assertEquals("Wohnzimmer", raum.toString());
        assertTrue(raum.getId() >= 0);
    }
    public void testSetName(){
        Raum raum = new Raum("Küche");
        raum.setName("Esszimmer");
        assertEquals("Esszimmer", raum.getName());
        assertEquals("Esszimmer", raum.toString());
    }
    public void testCreateMultipleRooms(){
        Raum raum1 = new Raum("Bad");
        Raum raum2 = new Raum("Wohnzimmer");
        assertTrue(raum2.getId() > raum1.getId());
    }
}