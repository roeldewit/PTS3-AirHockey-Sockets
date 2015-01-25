/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Serializable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author stijn
 */
public class SerializableGame implements Serializable {

    public static final long serialVersionUID = 88L;

    public int id;

    public boolean busy;

    public String description;

    public String hostIP;

    public ArrayList<String> usernames;

    /**
     * contains all data for a game
     * @param id
     * @param description
     * @param hostIP
     * @param username 
     */
    public SerializableGame(int id, String description, String hostIP, String username) {
        this.id = id;
        this.description = description;
        this.hostIP = hostIP;

        usernames = new ArrayList<>();
        usernames.add(username);
    }
}
