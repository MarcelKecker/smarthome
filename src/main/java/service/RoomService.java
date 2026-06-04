package service;

import model.room.Raum;

import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private List<Raum> rooms = new ArrayList<>();

    public void addRoom(Raum room) {
        rooms.add(room);
    }

    public List<Raum> getAllRooms() {
        return rooms;
    }

    public void deleteRoom(Raum room) {
        rooms.remove(room);
    }


    public void setRooms(List<Raum> rooms) {
        this.clear();
        for (Raum room : rooms) {
            this.addRoom(room);
        }
    }

    public void clear() {
        this.rooms.clear();
    }
}
