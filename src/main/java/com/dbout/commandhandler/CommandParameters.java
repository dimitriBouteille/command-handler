package com.dbout.commandhandler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.HashMap;

/**
 * CommandParameters
 * https://github.com/dimitriBouteille/command-handler
 *
 * @author  Dimitri BOUTEILLE <bonjour@dimitri-bouteille.fr>
 * @version 1.0
 */
public class CommandParameters extends HashMap<String, String> {

    /**
     * @see #getString(String)
     *
     * @param parameterName Parameter name
     * @return              Returns a string type value
     */
    public String get(String parameterName) {

        return this.getString(parameterName);
    }

    /**
     * Retourne un paramètre au format string
     *
     * @param parameterName Parameter name
     * @return              Returns a string type value
     */
    public String getString(String parameterName) {

        if(this.paramExist(parameterName)) {
            return this.get(parameterName);
        }

        return null;
    }

    /**
     * @param parameterName Parameter name
     * @return              Returns a int type value
     */
    public Integer getInt(String parameterName) {

        if(this.paramExist(parameterName)) {
            try {
                return Integer.parseInt(this.get(parameterName));
            } catch (NumberFormatException e) {}
        }

        return null;
    }

    /**
     * @param parameterName Parameter name
     * @return              Returns a double type value
     */
    public Double getDouble(String parameterName) {

        if(this.paramExist(parameterName)) {
            try {

                double x = Double.parseDouble(this.get(parameterName));
                if(x == (int)x) {
                    return null;
                }

                return x;
            } catch (NumberFormatException e) {}
        }

        return null;
    }

    /**
     * @param parameterName Parameter name
     * @return              Returns a Player object if the player exists
     */
    public Player getPlayer(String parameterName) {

        if(this.paramExist(parameterName)) {

            return Bukkit.getPlayer(this.get(parameterName));
        }


        return null;
    }

    /**
     * @param parameterName Parameter name
     * @return              Returns a World object if the world exists
     */
    public World getWorld(String parameterName) {

        if(this.paramExist(parameterName)) {

            return Bukkit.getWorld(this.get(parameterName));
        }

        return  null;
    }

    /**
     * Check if a parameter exists
     *
     * @param paramName Parameter name
     * @return          Returns true if the parameter exists or false
     */
    private boolean paramExist(String paramName) {

        return this.containsKey(paramName);
    }

}