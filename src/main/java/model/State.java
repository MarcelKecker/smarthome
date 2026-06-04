package model;

public enum State {
    TURNED_OFF("Aus"),
    TURNED_ON("An"),
    ROLLED_DOWN("Unten"),
    ROLLED_UP("Oben");

    private final String label;

    State(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static State getValue(String value) {
        for (State state : values()) {
            if (state.label.equalsIgnoreCase(value)
                    || state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown State: " + value);
    }
}