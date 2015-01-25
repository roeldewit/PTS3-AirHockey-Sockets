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
