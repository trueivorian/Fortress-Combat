package networking.server.utils;

import networking.CommandType;

import java.util.HashMap;

public class NetworkingEvent {
    private CommandType commandType;
    private HashMap<Object, Object> command;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public HashMap<Object, Object> getCommand() {
        return command;
    }

    public void setCommand(HashMap<Object, Object> command) {
        this.command = command;
    }
}
