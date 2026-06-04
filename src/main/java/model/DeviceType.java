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

    public static DeviceType getValue(String value) {
        for (DeviceType type : values()) {
            if (type.label.equalsIgnoreCase(value)
                    || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DeviceType: " + value);
    }
}