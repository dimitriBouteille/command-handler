package com.dbout.commandhandler.exceptions;

import com.dbout.commandhandler.Command;

/**
 * AliasAlreadyExist
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class AliasAlreadyExist extends CommandHandlerException {

    public AliasAlreadyExist(String alias, Command command1, Command command2) {

        super(String.format("The alias [%s] is already used by the command [%s]. You can't use this alias in [%s].",
                alias, command1, command2));
    }

}
