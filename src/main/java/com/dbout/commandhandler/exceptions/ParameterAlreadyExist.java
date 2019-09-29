package com.dbout.commandhandler.exceptions;

/**
 * ParameterAlreadyExist
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class ParameterAlreadyExist extends CommandHandlerException {

    public ParameterAlreadyExist(String parameterName, String command) {

        super(String.format("The parameter [%s] already exists in the command [%s]",
                parameterName, command));
    }

}
