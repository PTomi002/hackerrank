package preparationkit.arrays.arraymanipulation;

import java.util.List;

public class SolutionNOK {

    // idea:
    //  - jelöljük balról jobbra a tömböt, hogy melyik indextől mennyit kell hozzáadni és korrigálni
    //  - e.g.: n = 10, query = 2,4,2 ->
    //      1 2 3 4  5 6 7 8 9 10
    //      0 2 0 0 -2 0 0 0 0 0 hiszen 5-től 0 marad az értéke vagyis -2
    public static long arrayManipulation(int n, List<List<Integer>> queries) {
        int a, b;
        long k;

        long markers[] = new long[n + 1];

        for (List<Integer> query : queries) {
            a = query.get(0) - 1;
            b = query.get(1) - 1;
            k = query.get(2);

            markers[a] += k;
            markers[b + 1] -= k;
        }

        long sum = 0, maximum = 0;
        for (int i = 0; i < markers.length - 1; i++) {
            sum += markers[i];
            if (sum > maximum)
                maximum = sum;
        }

        return maximum;
    }


    public static void main(String[] args) {
        System.out.println( // expected = 10
                arrayManipulation(
                        10,
                        List.of(
                                List.of(1, 8, 1),
                                List.of(2, 8, 1),
                                List.of(2, 4, 1),
                                List.of(3, 4, 1),
                                List.of(5, 7, 3),
                                List.of(7, 10, 5)
                        )
                )
        );
    }
}
