package Airhockey.Utils;

/**
 * Class containing the score calculator
 *
 * @author Roel + Sam
 */
public class ScoreCalculator {

    /**
     * Calculate the rating of a user bases on his five most recent games
     *
     * @param score1 Most recent score
     * @param score2
     * @param score3
     * @param score4
     * @param score5 Earliest score
     * @return Calculated rating
     */
    public static double calculateRating(int score1, int score2, int score3, int score4, int score5) {
        double rating;

        rating = Math.floor((5 * score1 + 4 * score4 + 3 * score3 + 2 * score2 + score1) / 15);

        return rating;
    }

    /**
     * Calculates the score of the given player if the game stopped early after a disconnect.
     * Calculated by (newScore := (currentScore-20)*10/rounds + 20). The player is given one of those 2 scores.
     * If the player disconnected he gets the lowest score of the 2, otherwise he'll get the highest.
     *
     * @param playerScore Score of the player when the game stopped.
     * @param nubmerOfRoundsPlayed The number of game rounds already finished.
     * @param playerLeftGameEarly True if this is the number of the player who disconnected.
     * @return the player's final score.
     */
    public static int calculateAdjustedGameScore(int playerScore, int nubmerOfRoundsPlayed, boolean playerLeftGameEarly) {
        int newPlayerScore = (int) (Math.floor((double) (playerScore - 20) * (10.0 / (double) (nubmerOfRoundsPlayed))) + 20);

        if (playerLeftGameEarly) {
            if (newPlayerScore > playerScore) {
                return playerScore;
            } else {
                return newPlayerScore;
            }
        } else {
            if (newPlayerScore > playerScore) {
                return newPlayerScore;
            } else {
                return playerScore;
            }
        }
    }
}
