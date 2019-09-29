package com.dbout.commandhandler;

import org.bukkit.entity.Player;
import java.util.Arrays;

/**
 * CommandSender
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandSender {

    /**
     * The player and the console can execute the command
     *
     * @since 1.0
     */
    public static final int ALL = 1;

    /**
     * Only the player can execute the command
     *
     * @since 1.0
     */
    public static final int PLAYER = 2;

    /**
     * Only the console can execute the command
     *
     * @since 1.0
     */
    public static final int CONSOLE = 3;

    /**
     * Checks if a sender is valid
     *
     * @param senderType    Sender
     * @return              Returns true if the sender is valid or false
     */
    public static boolean isValidSender(int senderType) {

        int[] senders = {CommandSender.PLAYER, CommandSender.CONSOLE, CommandSender.ALL};
        return Arrays.stream(senders).anyMatch(v -> v == senderType);
    }

    /**
     * Checks if a sender can execute a command
     *
     * @param command   Command to be executed
     * @param sender    Sender who has executed the command
     * @return          Returns true if the sender can execute the command or false
     */
    public static boolean canExecuteCommand(Command command, org.bukkit.command.CommandSender sender) {

        if(command.getCommandSenderType() == CommandSender.ALL) {
            return true;
        } else if(command.getCommandSenderType() == CommandSender.PLAYER) {
            return (sender instanceof Player);
        } else {

            // Console sender only
            return CommandSender.isConsole(sender);
        }
    }

    /**
     * Check if sender is console
     *
     * @param sender    Sender
     * @return          Returns true if sender is console of false
     */
    public static boolean isConsole(org.bukkit.command.CommandSender sender) {

        return !(sender instanceof Player);
    }

}
