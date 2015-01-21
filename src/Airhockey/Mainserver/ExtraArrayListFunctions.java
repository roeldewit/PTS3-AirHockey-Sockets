/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Mainserver;

import java.util.ArrayList;

/**
 *
 * @author pieper126
 */
public class ExtraArrayListFunctions {

    public static ArrayList<String> createsNodeArrayListWithEnetries(String... strings) {
        ArrayList<String> returnvalue = new ArrayList<>();

        for (String string : strings) {
            returnvalue.add(string);
        }

        return returnvalue;
    }
}
