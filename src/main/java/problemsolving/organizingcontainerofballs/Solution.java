package problemsolving.organizingcontainerofballs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Solution {

    /*
    Expected output:
Possible
Possible
Possible
Impossible
Possible
Impossible
Possible
Possible
Possible
Possible
     */
    private static final String TEST_1 = "./src/main/resources/problemsolving/organizingcontainerofballs/test_1.txt";

    /*
     * Complete the 'organizingContainers' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts 2D_INTEGER_ARRAY container as parameter.
     */

    // idea:
    //      - csak akkor tudunk cserélni, ha van mit elvenni egy másik sorból, ez azt jelenti ha összeadom a a sor és oszlopokat majd sorrendezem,
    //          ugyan annyi labdának kell lennie, különben nincs mivel kicserélni az addtt labdát
    //      - nem érdekel a kivitelezés, csak az hogy lehetséges vagy sem
    public static String organizingContainers(List<List<Integer>> container) {
        // Write your code here
        final int size = container.size();
        final int[][] mx = new int[size][2];

        for (int i = 0; i < size; i++) {
            final int local = i;
            int rowSum = container.get(local).stream().mapToInt(Integer::intValue).sum();
            int colSum = container.stream().mapToInt(l -> l.get(local)).sum();
            mx[local][0] = rowSum;
            mx[local][1] = colSum;
        }

        if (
                !Arrays.equals(
                        Arrays.stream(mx).mapToInt(i -> i[0]).sorted().toArray(),
                        Arrays.stream(mx).mapToInt(i -> i[1]).sorted().toArray()
                )
        )
            return "Impossible";

        return "Possible";
    }

    public static void main(String[] args) throws IOException {
        System.out.println(
                organizingContainers(
                        List.of(
                                List.of(0, 2, 1),
                                List.of(1, 1, 1),
                                List.of(2, 0, 0)
                        )
                )
        );

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Path.of(TEST_1).toFile()))) {

            int q = Integer.parseInt(bufferedReader.readLine().trim());

            IntStream.range(0, q).forEach(qItr -> {
                try {
                    int n = Integer.parseInt(bufferedReader.readLine().trim());

                    List<List<Integer>> container = new ArrayList<>();

                    IntStream.range(0, n).forEach(i -> {
                        try {
                            container.add(
                                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                            .map(Integer::parseInt)
                                            .collect(toList())
                            );
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    System.out.println(
                            organizingContainers(
                                    container
                            )
                    );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

    }
}
