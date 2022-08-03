package preparationkit.arrays.twodimensionalarray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Solution {

    public static List<Integer> rotLeft(List<Integer> a, int d) {
        if (a.size() < 1) return a;

        while (d-- > 0) {
            int tmp = a.remove(0);
            a.add(tmp);
        }

        return a;
    }

    public static void main(String[] args) {
        System.out.println(
                rotLeft(
                        new ArrayList<>(List.of(1, 2, 3, 4, 5)),
                        4
                )
        );
        System.out.println(
                // balra shiftelésnél elég a ref-eket átírni a 0.elemre (head), nem kell a több ezer elemet shiftelni
                rotLeft(
                        new LinkedList<>(List.of(1, 2, 3, 4, 5)),
                        4
                )
        );
    }
}
