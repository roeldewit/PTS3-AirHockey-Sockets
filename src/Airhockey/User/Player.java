package Airhockey.User;

/**
 *
 * @author Roel
 */
public class Player {

    private int id;
    private int score = 20;
    public User user;

    public Player(int id, User user) {
        this.id = id;
        this.user = user;
    }

    public void upScore() {
        score++;
    }

    public void downScore() {
        score--;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
