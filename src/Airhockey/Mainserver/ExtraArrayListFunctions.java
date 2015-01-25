package Airhockey.Mainserver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Extra functions for array list
 * @author pieper126
 */
public class ExtraArrayListFunctions {

    /**
     * Create collection of strings from infinite number of given strings
     * @param strings Infinite number of strings
     * @return Collection of strings
     */
    public static ArrayList<String> createNodeArrayListWithEntries(String... strings) {
        
        ArrayList<String> stringCollection = new ArrayList<>();

        stringCollection.addAll(Arrays.asList(strings));

        return stringCollection;
    }
}
