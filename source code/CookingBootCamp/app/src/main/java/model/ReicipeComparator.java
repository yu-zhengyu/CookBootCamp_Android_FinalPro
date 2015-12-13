package model;

import java.util.Comparator;

/**
 * Created by Yi on 11/28/15.
 * This class is mainly for sort the recipe by its distance.
 */

public class ReicipeComparator implements Comparator<Recipe>{
    /**
     * Override this method to sort this array.
     * @param a a
     * @param b b
     * @return int
     */
    public int compare(Recipe a, Recipe b) {
        if (a.getDistance() < b.getDistance()) return -1;
        else if (a.getDistance() > b.getDistance()) return 1;
        else return 0;
    }
}
