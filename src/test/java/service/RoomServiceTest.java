package service;

import junit.framework.TestCase;
import model.room.Raum;

public class RoomServiceTest extends TestCase {

    public void testGetAllRoomsInitiallyEmpty() {
        RoomService service = new RoomService();

        assertNotNull(service.getAllRooms());
        assertEquals(0, service.getAllRooms().size());
    }

    public void testAddRoom() {
        RoomService service = new RoomService();
        Raum raum = new Raum("Wohnzimmer");

        service.addRoom(raum);

        assertEquals(1, service.getAllRooms().size());
        assertSame(raum, service.getAllRooms().get(0));
    }

    public void testAddMultipleRooms() {
        RoomService service = new RoomService();
        service.addRoom(new Raum("Wohnzimmer"));
        service.addRoom(new Raum("Küche"));
        service.addRoom(new Raum("Bad"));

        assertEquals(3, service.getAllRooms().size());
    }

    public void testDeleteRoom() {
        RoomService service = new RoomService();
        Raum raum = new Raum("Wohnzimmer");
        service.addRoom(raum);

        service.deleteRoom(raum);

        assertEquals(0, service.getAllRooms().size());
    }

    public void testDeleteRoomOnlyRemovesCorrectOne() {
        RoomService service = new RoomService();
        Raum r1 = new Raum("Küche");
        Raum r2 = new Raum("Bad");
        service.addRoom(r1);
        service.addRoom(r2);

        service.deleteRoom(r1);

        assertEquals(1, service.getAllRooms().size());
        assertSame(r2, service.getAllRooms().get(0));
    }

    public void testDeleteNonExistentRoomDoesNotThrow() {
        RoomService service = new RoomService();
        Raum raum = new Raum("Keller");

        service.deleteRoom(raum);

        assertEquals(0, service.getAllRooms().size());
    }
}
