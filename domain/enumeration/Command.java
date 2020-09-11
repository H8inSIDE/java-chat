package domain.enumeration;

public enum Command {
    QUIT("/quit");

    private String value;

    private Command(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}