package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.room.Raum;
import java.util.List;

public class RoomService {
    private ObservableList<Raum> rooms = FXCollections.observableArrayList();

    public void addRoom(Raum room) {
        rooms.add(room);
    }

    public ObservableList<Raum> getAllRooms() {
        return rooms;
    }

    public void deleteRoom(Raum room) {
        rooms.remove(room);
    }

    public void setRooms(List<Raum> rooms) {
        this.clear();
        if (rooms != null) {
            this.rooms.addAll(rooms);
        }
    }

    public void clear() {
        this.rooms.clear();
    }
}