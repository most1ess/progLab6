package command;

import java.io.Serializable;

public class CommandData implements Serializable {
    private static final long serialVersionUID = 1488L;
    private String name;
    private String param1;
    private String param2;

    public CommandData(String name) {
        this.name = name;
    }

    public CommandData(String name, String param1) {
        this.name = name;
        this.param1 = param1;
    }

    public CommandData(String name, String param1, String param2) {
        this.name = name;
        this.param1 = param1;
        this.param2 = param2;
    }
}
