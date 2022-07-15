package problemsolving.climbingtheladder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Solution {

    private static final String TEST_0 = "./src/main/resources/problemsolving/climbingtheladder/test_0.txt";
    private static final String TEST_2 = "./src/main/resources/problemsolving/climbingtheladder/test_2.txt";

    /*
     * Complete the 'climbingLeaderboard' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. INTEGER_ARRAY ranked
     *  2. INTEGER_ARRAY player
     */
    public static List<Integer> climbingLeaderboard(List<Integer> ranked, List<Integer> player) {
        Set<Integer> set = new LinkedHashSet<>(ranked);
        List<Integer> list = new ArrayList<>(set);
        return player.stream()
                .map(score -> binarySearch(list, score) + 1)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static int binarySearch(List<Integer> list, int key) {
        if (key >= list.get(0)) return 0;
        else if (key == list.get(list.size() - 1)) return list.size() -1;
        else if (key < list.get(list.size() - 1)) return list.size();
        else {
            int high = list.size() - 1;
            int low = 0;
            while (Math.abs(high - low) != 1) {
                int mid = (low + high) / 2;
                if (key == list.get(mid))
                    return mid;
                if (key > list.get(mid))
                    high = mid;
                if (key < list.get(mid))
                    low = mid;
            }
            return low + 1;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Path.of(TEST_2).toFile()))) {
            List<Integer> ranked = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Integer::parseInt)
                    .collect(toList());

            List<Integer> player = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Integer::parseInt)
                    .collect(toList());

            System.out.println(
                    climbingLeaderboard(ranked, player)
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(System.lineSeparator()))
            );
        }
    }
}
