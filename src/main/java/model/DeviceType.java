package model;

public enum DeviceType {
    LAMP("Lampe"),
    HEATING("Heizung"),
    SHUTTER("Rollladen");

    private final String label;

    DeviceType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}