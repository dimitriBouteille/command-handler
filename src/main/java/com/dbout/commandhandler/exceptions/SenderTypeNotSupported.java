package com.dbout.commandhandler.exceptions;

import com.dbout.commandhandler.Command;

/**
 * SenderTypeNotSupported
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class SenderTypeNotSupported extends CommandHandlerException {

    public SenderTypeNotSupported(int senderType, Command command) {

        super(String.format("The sender type [%s] of the command [%s] is not supported.",
                senderType, command));
    }

}
