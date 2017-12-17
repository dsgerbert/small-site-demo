package ru.h562.smallsite.model;

/**
 * Message data
 */
public class MessageData {
    private String name;
    private String value;

    public MessageData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(
                "%s\t%s\n",
                name,
                value.replaceAll("\t", " ").replaceAll("[\"]", "\"\"")
        );
    }
}
