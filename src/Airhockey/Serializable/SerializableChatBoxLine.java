/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Serializable;

import java.io.Serializable;

/**
 *
 * @author stijn
 */
public class SerializableChatBoxLine implements Serializable {

    public static final long serialVersionUID = 45L;
    
    public String player;
    
    public String text;

    /**
     * contains all data of a chatboxLine
     * @param player
     * @param text 
     */
    public SerializableChatBoxLine(String player, String text) {
        this.player = player;
        this.text = text;
    }
}
