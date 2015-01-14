package Airhockey.Connection;

/**
 *
 * @author Sam
 */
class Protocol {

    protected static final String SEPERATOR = ",";

    protected static final String SET_UP_GAME = "set_up_game";

    protected static final String PUCK_LOCATION = "puck_location";
    protected static final String RED_BAT_LOCATION = "red_bat_location";
    protected static final String BLUE_BAT_LOCATION = "blue_bat_location";
    protected static final String GREEN_BAT_LOCATION = "green_bat_location";

    protected static final String CLIENT_BAT_MOVEMENT_DIRECTION = "client_bat_movement_direction";

    protected static final String GOAL_MADE = "goal_made";

    protected static final String CHAT_LINE = "chat_line";

    protected static final String CLIENT_SEND_GAME_DATA = "client_send_game_data";

    protected static final String GAME_OVER = "game_over";

    protected static final String CLIENT_LEAVING_GAME = "client_leaving_game";

    protected static final String GAME_CANCELLED = "game_cancelled";

}
