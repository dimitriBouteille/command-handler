package com.dbout.commandhandler;

import com.dbout.commandhandler.exceptions.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CommandHandler
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandHandler {

    /**
     * List of commands that can be executed
     *
     * @since 1.0
     */
    protected List<Command> commands = new ArrayList<>();

    /**
     * Console sender
     *
     * @since 1.0
     */
    protected ConsoleCommandSender consoleSender;

    /**
     * Constructor
     */
    public CommandHandler() {

        this.consoleSender = Bukkit.getConsoleSender();
    }

    /**
     * Register new command
     *
     * @param command   New command
     * @return          Return CommandHandler instance
     * @throws CommandHandlerException  Exception when initialisation of the command
     */
    public CommandHandler registerCommand(Command command) throws CommandHandlerException {

        // Test to initialize the command
        command.init();

        for(Command cmd : this.commands) {

            // Check if alias is already use
            for(String alias : command.getAliases()) {
                if(cmd.getAliases().contains(alias)) {
                    throw new AliasAlreadyExist(alias, cmd, command);
                }
            }

            if(cmd.getCommand().equals(command.getCommand())) {
                throw new CommandAlreadyExist(command);
            }
        }


        this.commands.add(command);
        return this;
    }

    /**
     * Run command on onCommand() method with notifySender=true
     *
     * @param sender            Sender
     * @param commandPassed     Command
     * @param args              Command arguments
     * @return                  Return only true
     */
    public boolean runCommand(CommandSender sender, org.bukkit.command.Command commandPassed, String[] args) {

        return this.runCommand(sender, commandPassed, args, true); // Notify sender by default
    }

    /**
     * Run command on onCommand() method
     *
     * @param sender            Sender
     * @param commandPassed     Command
     * @param args              Command arguments
     * @param notifySender      If true, sender is informed if has not permission
     * @return                  Return only true
     */
    public boolean runCommand(CommandSender sender, org.bukkit.command.Command commandPassed, String[] args, boolean notifySender) {

        // Format the command that has just been executed
        String commandExecute = commandPassed.getName() + ' ' + String.join(" ", Arrays.asList(args));

        // Search for the command that matches the command that has just been executed
        for(Command command : this.commands) {

            // Is help command
            // ie : /myCommand ?
            if(command.matchHelp(commandExecute)) {
                this.showHelpMessage(command, sender, notifySender);
                break;
            }

            // Is basic command
            // ie : /myCommand param1 param2
            if(command.match(commandExecute)) {
                this.callCommand(command, sender, notifySender);
                break;
            }
        }

        return true;
    }

    /**
     * @return  Returns all commands
     */
    public List<Command> getCommands() { return this.commands; }

    /**
     * Call command
     *
     * @param command       Command
     * @param sender        Sender
     * @param notifySender  If true, sender is informed if has not permission
     */
    private void callCommand(Command command, CommandSender sender, boolean notifySender) {

        if(this.canRunCommand(command, sender, notifySender)) {

            // Call callback function
            command.execute(sender, command.getMatchesParameters());
        }
    }

    /**
     * Show help message
     *
     * @param command       Command
     * @param sender        Sender
     * @param notifySender  If true, sender is informed if has not permission
     */
    private void showHelpMessage(Command command, CommandSender sender, boolean notifySender) {

        if(this.canRunCommand(command, sender, notifySender)) {

            command.showHelp(sender);
        }
    }

    /**
     * Check if sender can run command
     *
     * @param command       Command
     * @param sender        Sender
     * @param notifySender  If true, sender is informed if has not permission
     * @return              Returns true if sender can call command or false
     */
    private boolean canRunCommand(Command command, CommandSender sender, boolean notifySender) {

        if(com.dbout.commandhandler.CommandSender.canExecuteCommand(command, sender)) {
            if(this.hasPermission(sender, command)) {

                return true;
            } else {

                if(notifySender) {
                    sender.sendMessage("You don't have the required permission(s)");
                }
            }
        } else {

            this.consoleSender.sendMessage("You can't execute this command in this context ...");
        }

        return false;
    }

    /**
     * Checks if a sender has permission to execute a command
     *
     * @param sender    User who just executed the command
     * @param command   Command
     * @return          Returns true if the user can execute the command or false
     */
    private boolean hasPermission(CommandSender sender, Command command) {

        if(!(sender instanceof  Player)) {
            return true;
        }

        Permission perm = command.getPermission();
        if(perm == null) {
            return true;
        }

        Player player = (Player)sender;

        boolean hasPermission = sender.hasPermission(perm.getName());
        if(!sender.isPermissionSet(perm.getName())) {
            this.consoleSender.sendMessage(String.format("%sThe permission [%s%s%s] was not set for [%s%s%s]",
                    ChatColor.WHITE, ChatColor.RED, perm.getName(), ChatColor.WHITE,
                    ChatColor.RED, player.getDisplayName(), ChatColor.WHITE));
        }

        if(hasPermission) {
            this.consoleSender.sendMessage(String.format("Checking to see if player [%s] has permission [%s] : %sYES",
                    player.getDisplayName(), perm.getName(),
                    ChatColor.GREEN));
        } else {
            this.consoleSender.sendMessage(String.format("Checking to see if player [%s] has permission [%s] : %sNO",
                    player.getDisplayName(), perm.getName(),
                    ChatColor.RED));
        }

        return hasPermission;
    }

}