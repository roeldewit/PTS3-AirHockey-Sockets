package Airhockey.Main;

import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import java.util.ArrayList;
import javafx.application.Application;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class MainControl {

    private User currentUser;
    private Object currentPosition;
    private ArrayList<User> users;
    private ArrayList<Game> games;
    private ScoreCalculator scoreCalculator;
    private Lobby lobby;

    public void SetUp() {
        throw new UnsupportedOperationException();
    }

    public void loginUser(String username, String password) {
        throw new UnsupportedOperationException();
    }

    public void logOffUser() {
        throw new UnsupportedOperationException();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void createNewGame(String[] args) {
        Application.launch(Login.class, args);
    }

    public void joinGame(Game game) {
        throw new UnsupportedOperationException();
    }

    public void createUser(String username, String password) {
        throw new UnsupportedOperationException();
    }

    public void startGame() {
        throw new UnsupportedOperationException();
    }

    public void leaveGame() {
        throw new UnsupportedOperationException();
    }

    public void joinGameAsSpectator() {
        throw new UnsupportedOperationException();
    }

    public void viewScoreboard() {
        throw new UnsupportedOperationException();
    }

    public void writeInChat(ChatboxLine chatboxLine) {
        throw new UnsupportedOperationException();
    }

    public void viewChat() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        MainControl mc = new MainControl();
        mc.createNewGame(args);
    }
}
