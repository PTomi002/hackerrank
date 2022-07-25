package problemsolving.queensattack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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

    @FunctionalInterface
    private interface ScanCommand {
        int scan();
    }

    private static abstract class AbstractScan implements ScanCommand {
        private final int positionX, positionY, chessboard;
        private final Map<Integer, Set<Integer>> obstacles;

        protected AbstractScan(int positionX, int positionY, int chessboard, Map<Integer, Set<Integer>> obstacles) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.chessboard = chessboard;
            this.obstacles = obstacles;
        }

        public int scan() {
            int queenSteps = 0;
            int x = nextX(positionX);
            int y = nextY(positionY);

            while (canQueenStep(chessboard).test(x, y)) {
                if (!isObstacleInStep(x, y)) {
                    queenSteps++;
                    y = nextY(y);
                    x = nextX(x);
                } else break;
            }
            return queenSteps;
        }

        protected abstract BiPredicate<Integer, Integer> canQueenStep(int n);

        protected abstract int nextX(int x);

        protected abstract int nextY(int y);

        private boolean isObstacleInStep(int x, int y) {
            return Optional
                    .ofNullable(obstacles.get(x))
                    .map(columns -> columns.contains(y))
                    .orElse(false);
        }

    }

    private static class RightScan extends AbstractScan {
        protected RightScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> x < n;
        }

        @Override
        protected int nextX(int x) {
            return x + 1;
        }

        @Override
        protected int nextY(int y) {
            return y;
        }
    }

    private static class LeftScan extends AbstractScan {
        protected LeftScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> x >= 0;
        }

        @Override
        protected int nextX(int x) {
            return x - 1;
        }

        @Override
        protected int nextY(int y) {
            return y;
        }
    }

    private static class UpScan extends AbstractScan {
        protected UpScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> y < n;
        }

        @Override
        protected int nextX(int x) {
            return x;
        }

        @Override
        protected int nextY(int y) {
            return y + 1;
        }
    }

    private static class DownScan extends AbstractScan {
        protected DownScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> y >= 0;
        }

        @Override
        protected int nextX(int x) {
            return x;
        }

        @Override
        protected int nextY(int y) {
            return y - 1;
        }
    }

    private static class DiagonalRightUpScan extends AbstractScan {
        protected DiagonalRightUpScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> x < n && y < n;
        }

        @Override
        protected int nextX(int x) {
            return x + 1;
        }

        @Override
        protected int nextY(int y) {
            return y + 1;
        }
    }

    private static class DiagonalLeftDownScan extends AbstractScan {
        protected DiagonalLeftDownScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> x >= 0 && y >= 0;
        }

        @Override
        protected int nextX(int x) {
            return x - 1;
        }

        @Override
        protected int nextY(int y) {
            return y - 1;
        }
    }

    private static class DiagonalLeftUpScan extends AbstractScan {
        protected DiagonalLeftUpScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> x >= 0 && y < n;
        }

        @Override
        protected int nextX(int x) {
            return x - 1;
        }

        @Override
        protected int nextY(int y) {
            return y + 1;
        }
    }

    private static class DiagonalRightDownScan extends AbstractScan {
        protected DiagonalRightDownScan(int x, int y, int n, Map<Integer, Set<Integer>> obstacles) {
            super(x, y, n, obstacles);
        }

        @Override
        protected BiPredicate<Integer, Integer> canQueenStep(int n) {
            return (x, y) -> x < n && y >= 0;
        }

        @Override
        protected int nextX(int x) {
            return x + 1;
        }

        @Override
        protected int nextY(int y) {
            return y - 1;
        }
    }

    private static Map<Integer, Set<Integer>> loadOptimizedObstacles(List<List<Integer>> obstacles) {
        return obstacles
                .stream()
                .collect(
                        groupingBy(
                                o -> o.get(0) - 1,
                                mapping(
                                        o -> o.get(1) - 1,
                                        toSet()
                                )
                        )
                );
    }

    // idea:
    //      - load the obstacles into a two layered hash to be able to quickly get them O(1)
    //      - apply CommandPatter for all the 8 directions and count the possible steps
    public static int queensAttack(int n, int k, int r_q, int c_q, List<List<Integer>> obstacles) {
        // Write your code here
        Map<Integer, Set<Integer>> optimizedObstacles = loadOptimizedObstacles(obstacles);

        int optimized_r_q = r_q - 1;
        int optimized_c_q = c_q - 1;

        return Stream
                .of(
                        new RightScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new LeftScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new UpScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new DownScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new DiagonalRightUpScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new DiagonalLeftDownScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new DiagonalLeftUpScan(optimized_r_q, optimized_c_q, n, optimizedObstacles),
                        new DiagonalRightDownScan(optimized_r_q, optimized_c_q, n, optimizedObstacles)
                )
                .mapToInt(AbstractScan::scan)
                .sum();
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
