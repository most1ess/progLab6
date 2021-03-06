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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
