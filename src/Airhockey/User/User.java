package Airhockey.User;

/**
 *
 * @author Roel
 */
public class User {

    private String username;
    private String password;
    private double rating;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        return this.username.equals(((User)o).username);
    }
}
