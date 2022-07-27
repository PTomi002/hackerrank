package preparationkit.warmup.jumpingontheclouds;

import java.util.List;

public class Solution {

    // idea:
    //      - ugorjunk mindig kettőt ha nem megyünk ki a tömbből és nem thunderre ugrunk, különben ugorjunk egyet
    //      - vagy 2-t vagy 1-t mindig tudunk ugrani, mivel a játékot mindig meg lehet nyerni
    public static int jumpingOnClouds(List<Integer> c) {
        int i = 0, jumps = 0;
        do {
            jumps++; // I will do at least one jump
            int next = i + 1, nextDouble = i + 2;
            if (nextDouble < c.size() && c.get(nextDouble) == 0) i = nextDouble;
            else i = next; // can always win the game, if I can not jump 2, I can jump 1 cloud
        } while (i < c.size() - 1);
        return jumps;
    }

    public static void main(String[] args) {
        // expected = 4
        System.out.println(
                jumpingOnClouds(
                        List.of(0, 0, 1, 0, 0, 1, 0)
                )
        );
        // expected = 3
        System.out.println(
                jumpingOnClouds(
                        List.of(0, 0, 0, 0, 1, 0)
                )
        );
    }
}
