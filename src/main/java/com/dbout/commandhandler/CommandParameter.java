package com.dbout.commandhandler;

/**
 * CommandParameter
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandParameter {

    /**
     * Parameter name without :
     * ie: player
     *
     * @since 1.0
     */
    private String name;

    /**
     * Parameter position
     * /!\ First position is 1
     */
    private int position;

    /**
     * Regex validation
     * ie: [\w]+
     */
    private String regex = "[\\w]+";

    /**
     * Constructor
     *
     * @param parameterName Parameter name
     * @param position      Parameter position
     */
    public CommandParameter(String parameterName, int position) {

        this.name = parameterName;
        this.position = position;
    }

    /**
     * @return  Get parameter name
     */
    public String getName() { return this.name; }

    /**
     * @return  Get regex
     */
    public String getRegex() { return this.regex; }

    /**
     * @return  Get parameter position
     */
    public int getPosition() { return this.position; }

}
