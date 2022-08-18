package preparationkit.arrays.minimumswaps2;

public class Solution {

    private static int minimumSwaps(int[] arr) {
        return selectionSort(arr);
    }

    // [ALG]: selection sort
    // idea:
    //      - selection sort a minimum swappel csinálja meg a sorrendet, de O(n^2)
    // problem:
    //      - TimeOutError (O(n^2) is too slow)
    private static int selectionSort(int[] arr) {
        int swaps = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            if (arr[minIdx] != arr[i]) {
                swaps++;
                int tmp = arr[i];
                arr[i] = arr[minIdx];
                arr[minIdx] = tmp;
            }
        }
        return swaps;
    }

    // idea:
    //      - mivel indexel egyező minden szám, így elég végigmenni mindegyiken és ellenőrizni hogy jó helyen vannak-e ha nem akkor swap++
    private static int counting(int[] arr) {
        int swap = 0;
        for (int i = 0; i < arr.length;) {
            if (arr[i] - 1 != i) { // indexre fordítjuk a -1-el
                int tmp = arr[i];
                arr[i] = arr[tmp - 1];
                arr[tmp - 1] = tmp;
                swap++;
            } else {
                i++;
            }
        }
        return swap;
    }

    public static void main(String[] args) {
//        System.out.println(
//                counting(
//                        new int[]{7, 1, 3, 2, 4, 5, 6}
//                )
//        );
        System.out.println(
                counting(
                        new int[]{4, 3, 1, 2}
                )
        );
//        System.out.println(
//                counting(
//                        new int[]{2, 3, 4, 1, 5}
//                )
//        );
//        System.out.println(
//                counting(
//                        new int[]{1, 3, 5, 2, 4, 6, 7}
//                )
//        );
    }
}
