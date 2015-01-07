package Airhockey.Main;

import Airhockey.User.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class ChatboxLine {

    private String text;
    private User user;

    public ChatboxLine(String text, User user) {
        this.text = text;
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}
