package com.dbout.commandhandler;

import com.dbout.commandhandler.exceptions.CommandHandlerException;
import com.dbout.commandhandler.exceptions.SenderTypeNotSupported;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public abstract class Command {

    /**
     * Command name (optional)
     * ie: Hello world
     */
    private String commandName;

    /**
     * Command description (optional)
     * ie: My best command
     */
    private String commandDescription;

    /**
     * Command reader
     */
    private CommandReader commandReader;

    /**
     * How the command should be executed (optional)
     * ie : /myCommand {PLAYER} {WORLD}
     */
    private String commandUsage;

    /**
     * Command examples (optional)
     * ie : /myCommand Player World
     */
    private List<String> examples = new ArrayList<>();

    /**
     * Aliases (optional)
     */
    private List<String> aliases = new ArrayList<>();

    /**
     * Who can execute the command (optional)
     * By default, the console and the player can execute the command
     */
    private int commandSenderType = com.dbout.commandhandler.CommandSender.ALL;

    /**
     * Permission that the player must have to execute the command
     */
    private Permission permission;

    /**
     * Contains the parameters that match the command after the match() function is called
     */
    private CommandParameters matches;

    /**
     * Set the command to be executed
     *
     * @param command   Command
     */
    protected final void setCommand(String command) { this.commandReader = new CommandReader(command); }

    /**
     * Set the command name
     *
     * @param name  Command name
     */
    protected final void setName(String name) { this.commandName = name; }

    /**
     * Set the command description
     *
     * @param description   Command description
     */
    protected final void setDescription(String description) { this.commandDescription = description; }

    /**
     * Set aliases
     *
     * @param alias aliases
     */
    protected final void addAlias(String... alias) { this.aliases.addAll(Arrays.asList(alias)); }

    /**
     * Set examples
     *
     * @param examples  Examples
     */
    protected final void addExamples(String... examples) { this.examples.addAll(Arrays.asList(examples)); }

    /**
     * Set how the command should be executed
     *
     * @param usage Command usage
     */
    protected final void setUsage(String usage) { this.commandUsage = usage; }

    /**
     * Set that can execute the command
     *
     * @param sender    Sender type
     */
    protected final void setSender(int sender) { this.commandSenderType = sender; }

    /**
     * Set permission to execute the command
     *
     * @param permission    Permission
     */
    protected final void setPermission(Permission permission) { this.permission = permission; }

    /**
     * @return  Returns command
     */
    public String getCommand() { return this.commandReader.getCommand(); }

    /**
     * @return  Returns sender type
     */
    public int getCommandSenderType() { return this.commandSenderType; }

    /**
     * @return  Returns permission
     */
    public Permission getPermission() { return this.permission; }

    /**
     * @return  Returns the list of parameters that match after the match() function is called
     */
    public CommandParameters getMatchesParameters() { return this.matches; }

    /**
     * @return  Returns aliases
     */
    public List<String> getAliases() { return this.aliases; }

    /**
     * Checks whether an executed command matches this command and extracts the parameters of the command
     *
     * @param commandPassed Command to be checked
     * @return              Returns true if the command matches with commandPassed or false
     */
    public boolean match(String commandPassed) {

        // Create regex validation
        // myCommand :param1 :param2 => ^(myCommand|alias1|alias1) ([regex1]) ([regex2])$
        List<String> regex = new ArrayList<>();
        regex.add("(" + String.join("|", this.getAllBases()) + ")");
        for (CommandParameter parameter : this.commandReader.getParameters()) {
            regex.add("(" + parameter.getRegex() + ")");
        }

        Pattern commandRegex = Pattern.compile("^" + String.join(" ", regex) + "$");
        Matcher matcher = commandRegex.matcher(commandPassed);

        if(!matcher.find()) {
            return false;
        }

        // Find parameters
        this.matches = new CommandParameters();
        for(CommandParameter parameter : this.commandReader.getParameters()) {
            this.matches.put(parameter.getName(), matcher.group(parameter.getPosition()));
        }

        return true;
    }

    /**
     * Check if it’s a help command
     * ie : /myCommand ?
     *
     * @param commandPassed Command to be checked
     * @return              Returns true if the command matches with commandPassed or false
     */
    public boolean matchHelp(String commandPassed) {

        // Check if command is 'myCommand ?'
        // Regex match : (myCommand|alias1|alias2) ?
        return Pattern.compile(String.format("^(%s) \\?$", String.join("|", this.getAllBases())))
                .matcher(commandPassed)
                .find();
    }

    /**
     * @return  Returns aliases and command fixed part
     */
    private List<String> getAllBases() {

        List<String> allBaseCmd = new ArrayList<>();
        allBaseCmd.add(this.commandReader.getCommandBase());
        allBaseCmd.addAll(this.aliases);

        return allBaseCmd;
    }

    /**
     * Init command
     *
     * @throws CommandHandlerException  Exception when initialisation of the command
     */
    public final void init() throws CommandHandlerException {

        if(this.commandReader == null) {
            throw new CommandHandlerException(String.format("The command [%s] is empty.", this));
        }

        this.commandReader.read();

        if(!com.dbout.commandhandler.CommandSender.isValidSender(this.commandSenderType)) {
            throw new SenderTypeNotSupported(this.commandSenderType, this);
        }
    }

    /**
     * Displays a help message in the chat if the command is not correct
     *
     * @param sender    Sender
     */
    public void showHelp(CommandSender sender) {

        sender.sendMessage(ChatColor.YELLOW + "-- "+ this.commandName + " --");
        if(this.commandDescription != null && !this.commandDescription.isEmpty()) {
            sender.sendMessage(ChatColor.AQUA + this.commandDescription);
        }

        if(this.commandUsage != null && !this.commandUsage.isEmpty()) {
            sender.sendMessage(ChatColor.GREEN + "Usage: "+ ChatColor.WHITE + this.commandUsage);
        }

        if(this.aliases.size() > 0) {
            sender.sendMessage(ChatColor.GREEN + "Aliases: " + ChatColor.RED + String.join(", ", this.aliases));
        }

        if(this.examples.size() > 0) {
            sender.sendMessage(ChatColor.GREEN + "Examples:");
            for(String example : this.examples) {
                sender.sendMessage(example);
            }
        }
    }

    /**
     * @return  Returns the class name
     */
    @Override
    public String toString() {

        return this.getClass().toString();
    }

    /**
     * Function that is called when the command is executed
     *
     * @param sender        Sender
     * @param parameters    List of parameters found in the command
     */
    public abstract void execute(CommandSender sender, CommandParameters parameters);

}
