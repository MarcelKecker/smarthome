package model.room;

//Sollte Room heißen - der Name "Room" wurde aus Versehen im Projekt als nicht-Java Vorlage gespeichert, weshalb der Name hier nicht verwendet werden kann
public class Raum {
    private String id;
    private String name;

    public Raum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }
}

