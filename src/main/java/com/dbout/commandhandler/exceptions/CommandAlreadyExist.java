package com.dbout.commandhandler.exceptions;

import com.dbout.commandhandler.Command;

/**
 * CommandAlreadyExist
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandAlreadyExist extends CommandHandlerException {

    public CommandAlreadyExist(Command command) {

        super(String.format("The command [%s] already exists.",
                command));
    }

}
