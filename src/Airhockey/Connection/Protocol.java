package Airhockey.Connection;

/**
 *
 * @author Sam
 */
public class Protocol {

    protected static final String SEPERATOR = ",";

    protected static final String SET_UP_GAME = "set_up_game";

    protected static final String PUCK_LOCATION = "puck_location";

    protected static final String BOTTOM_BAT_LOCATION = "bottom_bat_location";

    protected static final String LEFT_BAT_LOCATION = "left_bat_location";

    protected static final String RIGHT_BAT_LOCATION = "right_bat_location";

    protected static final String GOAL_MADE = "goal_made";

    protected static final String CHAT_LINE = "chat_line";

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

}
