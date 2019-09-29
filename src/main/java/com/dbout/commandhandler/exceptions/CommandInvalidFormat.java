package com.dbout.commandhandler.exceptions;

/**
 * CommandInvalidFormat
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandInvalidFormat extends CommandHandlerException {

    public CommandInvalidFormat(String error) {

        super(error);
    }

}
