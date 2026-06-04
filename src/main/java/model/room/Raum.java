package model.room;

//Sollte Room heißen - der Name "Room" wurde aus Versehen im Projekt als nicht-Java Vorlage gespeichert, weshalb der Name hier nicht verwendet werden kann
public class Raum {
    private static int idcounter;
    private int id;
    private String name;

    public Raum() {}

    public Raum(String name) {
        this.id = idcounter++;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

