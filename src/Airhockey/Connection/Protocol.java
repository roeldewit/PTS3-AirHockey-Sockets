package Airhockey.Connection;

/**
 *
 * @author Sam
 */
class Protocol {

    protected static final String SEPERATOR = ",";

    protected static final String CLIENT_SET_UP_GAME = "client_set_up_game";
    protected static final String SPECTATOR_SET_UP_GAME = "spectator_set_up_game";

    protected static final String PUCK_LOCATION = "puck_location";

    protected static final String RED_BAT_LOCATION = "red_bat_location";
    protected static final String BLUE_BAT_LOCATION = "blue_bat_location";
    protected static final String GREEN_BAT_LOCATION = "green_bat_location";

    protected static final String CLIENT_BAT_MOVEMENT_DIRECTION = "client_bat_movement_direction";
    protected static final String GOAL_MADE = "goal_made";

    protected static final String CHAT_LINE = "chat_line";

    protected static final String CLIENT_SEND_GAME_DATA = "client_send_game_data";
    protected static final String SPECTATOR_SEND_GAME_DATA = "spectator_send_game_data";

    protected static final String GAME_OVER = "game_over";

    protected static final String CLIENT_LEFT_GAME = "client_leaving_game";

    protected static final String GAME_CANCELLED = "game_cancelled";

// mainly used by connection between mainServer and client
    protected static final String PROTOCOL_ENDER = ";";

    protected static final String GET_CURRENT_RUNNINGGAMES = "get_current_runninggames";

    protected static final String GET_CURRENT_OPENGAMES = "get_current_opengames";

    protected static final String GET_CHATBOX_LINES = "get_chatbox_lines";

    protected static final String START_GAME = "start_game";

    protected static final String GAME_ID = "game_id";

    protected static final String CHATBOX_LINES = "chatbox_lines";

    protected static final String DELETE_GAME = "delete_game";

    protected static final String CURRENT_OPENGAMES = "current_opengames";

    protected static final String ADD_NEW_GAME = "add_new_game";

    protected static final String OPEN_GAME = "open_game";
}
