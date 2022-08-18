package preparationkit.arrays.newyearchaos;

import java.util.ArrayList;
import java.util.List;

public class SolutionNOK {

    // [ALG]: bubble sort
    // idea:
    //      - próbáljuk meg a rossz állapotot visszaállítani az eredetire hogy növekvő sorrendben legyenek, ne rekonstruálni
    //      - nézzük meg bubble sort algoritmus alappal
    // problem:
    //      - TimeOutError (O(n^2) is too slow)
    public static void minimumBribes(List<Integer> q) {
        bubbleSort(q, 2);
    }

    // complexity: O(n^2) as nested for-loop
    private static void bubbleSort(List<Integer> a, int allowedChange) {
        int[] changes = new int[a.size()];
        int change = 0;

        for (int i = 0; i < a.size() - 1; i++) {
            for (int j = 0; j < a.size() - i - 1; j++) {
                if (a.get(j) > a.get(j + 1)) {
                    if (++changes[a.get(j) - 1] > allowedChange) {
                        System.out.println("Too chaotic");
                        return;
                    }
                    change++;
                    int tmp = a.get(j);
                    a.set(j, a.get(j + 1));
                    a.set(j + 1, tmp);
                }
            }
        }
        System.out.println(change);
    }

    public static void main(String[] args) {
        minimumBribes(
                new ArrayList<>(List.of(2, 1, 5, 3, 4))
        );
        minimumBribes(
                new ArrayList<>(List.of(2, 5, 1, 3, 4))
        );
        minimumBribes(
                new ArrayList<>(List.of(1, 2, 5, 3, 7, 8, 6, 4))
        );
    }
}
