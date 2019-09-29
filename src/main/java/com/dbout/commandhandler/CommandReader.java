package com.dbout.commandhandler;

import com.dbout.commandhandler.exceptions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CommandReader
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandReader {

    /**
     * Command
     * ie : myCommand :param1 :param2
     */
    private String command;

    /**
     * Fixed part of the command
     * ie : myCommand
     */
    private String commandBase;

    /**
     * List of command parameters
     */
    private Map<String, CommandParameter> parameters = new HashMap<>();

    /**
     * Constructor
     *
     * @param command   Command
     */
    public CommandReader(String command) {

        this.command = this.substringFirstCharacter(command, "/");
    }

    /**
     * Reads the command to extract the command parameters
     *
     * @throws CommandHandlerException  Exception if a parameter already exists in the command
     */
    public void read() throws CommandHandlerException {

        // Check if command matching with :
        // - myCommand
        // - myCommand :param1
        // - myCommand :param1 :param2
        // ...
        Matcher validCmd = Pattern.compile("^([\\w\\s]+)( ?:[\\w]+)*$").matcher(this.command);
        if(!validCmd.find()) {
            throw new CommandInvalidFormat(String.format("The command [%s] is invalid.", this.command));
        }

        // Get fixed part of command
        this.commandBase = validCmd.group(1).trim();

        // Search all parameters ':param'
        Matcher m = Pattern.compile(":[\\w]+").matcher(this.command);
        int position = 1;
        while (m.find()) {

            String paramName = this.substringFirstCharacter(m.group(), ":");
            if(this.parameters.containsKey(paramName)) {
                throw new ParameterAlreadyExist(m.group(), this.command);
            }

            this.parameters.put(paramName, new CommandParameter(paramName,position));
            position++;
        }
    }

    /**
     * @return  Returns command
     */
    public String getCommand() { return this.command; }

    /**
     * @return  Returns fixed part of the command
     */
    public String getCommandBase() { return this.commandBase; }

    /**
     * @return  Returns all parameters in command
     */
    public List<CommandParameter> getParameters() { return new ArrayList<>(this.parameters.values()); }

    /**
     * Removes the first character of a string
     *
     * @param str           String
     * @param character     Character to be deleted
     * @return              Returns the string with the deleted character
     */
    private String substringFirstCharacter(String str, String character) {

        if(str.substring(0,1).equals(character)) {
            str = str.substring(1);
        }

        return str;
    }

}
