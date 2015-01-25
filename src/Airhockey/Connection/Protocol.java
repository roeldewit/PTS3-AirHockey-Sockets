package Airhockey.Connection;

/**
 * Class used to define all command that are send over the connection between
 * the host and it's clients.
 *
 * @author Sam
 */
public class Protocol {

    public static final String SEPERATOR = ",";

    public static final String CLIENT_SET_UP_GAME = "client_set_up_game";
    public static final String SPECTATOR_SET_UP_GAME = "spectator_set_up_game";

    public static final String PUCK_LOCATION = "puck_location";

    public static final String RED_BAT_LOCATION = "red_bat_location";
    public static final String BLUE_BAT_LOCATION = "blue_bat_location";
    public static final String GREEN_BAT_LOCATION = "green_bat_location";

    public static final String CLIENT_BAT_MOVEMENT_DIRECTION = "client_bat_movement_direction";
    public static final String GOAL_MADE = "goal_made";

    public static final String CHAT_LINE = "chat_line";

    public static final String CLIENT_SEND_GAME_DATA = "client_send_game_data";
    public static final String SPECTATOR_SEND_GAME_DATA = "spectator_send_game_data";

    public static final String GAME_OVER = "game_over";

    public static final String CLIENT_LEFT_GAME = "client_leaving_game";

    public static final String GAME_CANCELLED = "game_cancelled";

// mainly used by connection between mainServer and client
    public static final String PROTOCOL_ENDER = ";";

    public static final String GET_CURRENT_RUNNINGGAMES = "get_current_runninggames";

    public static final String GET_CURRENT_OPENGAMES = "get_current_opengames";

    public static final String GET_CHATBOX_LINES = "get_chatbox_lines";

    public static final String START_GAME = "start_game";

    public static final String GAME_ID = "game_id";

    public static final String CHATBOX_LINES = "chatbox_lines";

    public static final String DELETE_GAME = "delete_game";

    public static final String CURRENT_OPENGAMES = "current_opengames";
    
    public static final String CURRENT_BUSYGAMES = "current_busygames";

    public static final String ADD_NEW_GAME = "add_new_game";

    public static final String OPEN_GAME = "open_game";
}
