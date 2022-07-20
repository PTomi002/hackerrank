package problemsolving.queensattack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Solution {

    /*
     * Complete the 'queensAttack' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER n
     *  2. INTEGER k
     *  3. INTEGER r_q
     *  4. INTEGER c_q
     *  5. 2D_INTEGER_ARRAY obstacles
     */

    private static final String TEST_0 = "./src/main/resources/problemsolving/queensattack/test_0.txt";

    private static boolean isObstacle(int i, int j, Map<Integer, Set<Integer>> obstacles) {
        return Optional.ofNullable(obstacles.get(i)).map(columns -> columns.contains(j)).orElse(false);
    }

    public static int queensAttack(int n, int k, int r_q, int c_q, List<List<Integer>> obstacles) {
        // Write your code here
        Map<Integer, Set<Integer>> obs = new HashMap<>();
        obstacles.forEach(o -> {
            Set<Integer> columns = obs.computeIfAbsent(o.get(0) - 1, key -> new HashSet<>());
            columns.add(o.get(1) - 1);
        });

        int queen_row = r_q - 1;
        int queen_column = c_q - 1;

        int count = 0;
        // i++
        for (int i = queen_row + 1; i < n; i++) {
            if (!isObstacle(i, queen_column, obs)) count++;
            else break;
        }
        // i--
        for (int i = queen_row - 1; i >= 0; i--) {
            if (!isObstacle(i, queen_column, obs)) count++;
            else break;
        }
        // j++
        for (int i = queen_column + 1; i < n; i++) {
            if (!isObstacle(queen_row, i, obs)) count++;
            else break;
        }
        // j--
        for (int i = queen_column - 1; i >= 0; i--) {
            if (!isObstacle(queen_row, i, obs)) count++;
            else break;
        }

        // i++ j++
        int j = queen_column + 1;
        for (int i = queen_row + 1; i < n && j < n; i++) {
            if (!isObstacle(i, j, obs)) {
                count++;
                j++;
            } else break;
        }
        // i-- j--
        j = queen_column - 1;
        for (int i = queen_row - 1; i >= 0 && j >= 0; i--) {
            if (!isObstacle(i, j, obs)) {
                count++;
                j--;
            } else break;
        }
        // i++ j--
        j = queen_column - 1;
        for (int i = queen_row + 1; i < n && j >= 0; i++) {
            if (!isObstacle(i, j, obs)) {
                count++;
                j--;
            } else break;
        }
        // i-- j++
        j = queen_column + 1;
        for (int i = queen_row - 1; i >= 0 && j < n; i--) {
            if (!isObstacle(i, j, obs)) {
                count++;
                j++;
            } else break;
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(
                queensAttack(
                        5, 3,
                        4, 3,
                        List.of(
                                List.of(5, 5),
                                List.of(4, 2),
                                List.of(2, 3)
                        )
                )
        );

        System.out.println(
                queensAttack(
                        1, 0,
                        1, 1,
                        List.of()
                )
        );

        System.out.println(
                queensAttack(
                        4, 0,
                        4, 4,
                        List.of()
                )
        );

        System.out.println(
                queensAttack(
                        100000, 0,
                        4187, 5068,
                        List.of()
                )
        );

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Path.of(TEST_0).toFile()))) {
            String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            int n = Integer.parseInt(firstMultipleInput[0]);
            int k = Integer.parseInt(firstMultipleInput[1]);

            String[] secondMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            int r_q = Integer.parseInt(secondMultipleInput[0]);
            int c_q = Integer.parseInt(secondMultipleInput[1]);
            List<List<Integer>> obstacles = new ArrayList<>();

            IntStream.range(0, k).forEach(i -> {
                try {
                    obstacles.add(
                            Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                    .map(Integer::parseInt)
                                    .collect(toList())
                    );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            System.out.println("TC: " + queensAttack(n, k, r_q, c_q, obstacles));
        }

    }
}
