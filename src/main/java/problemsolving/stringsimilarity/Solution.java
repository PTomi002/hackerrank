package problemsolving.stringsimilarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class Solution {

    private static final String TEST_10 = "./src/main/resources/problemsolving/stringsimilarity/test_10.txt";

    // idea:
    //      - do not store anything, use two moving index on the string, O(n^2)
    // problem:
    //      - TimeOutError (O(n^2) is too slow)
    public static long linearStringSimilarity(String s) {
        long sumOfSimilarities = 0;

        for (int i = 0; i < s.length(); i++) {
            int idx = 0;
            for (int j = i; j < s.length(); j++) {
                if (s.charAt(j) == s.charAt(idx)) {
                    sumOfSimilarities++;
                    idx++;
                } else break;
            }
        }

        return sumOfSimilarities;
    }

    // idea:
    //      - https://www.hackerrank.com/challenges/string-similarity/topics/z-function
    public static long[] zFunStringSimilarity(String s) {
        int n = s.length();
        long[] z = new long[n];
        for (int i = 1, l = 0, r = 0; i < n; ++i) {
            if (i <= r)
                z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < n && s.charAt((int) z[i]) == s.charAt(i + (int) z[i]))
                z[i] = z[i] + 1;
            if (i + z[i] - l > r) {
                l = i;
                r = i + (int) z[i] - 1;
            }
        }
        return z;
    }

    // idea:
    //      - fork a task for each suffix, parallel, but constructs a lot of objects
    // problem:
    //      - RunTimeError (stack overflow happens at big string)
    private static class StringSimilarityTask extends RecursiveTask<Integer> {
        private final String word;
        private final String suffix;

        StringSimilarityTask(String word, String suffix) {
            this.word = word;
            this.suffix = suffix;
        }

        @Override
        protected Integer compute() {
            if (suffix.length() == 1) {
                if (word.charAt(0) == suffix.charAt(0)) return 1;
                else return 0;
            } else {
                StringSimilarityTask t1 = new StringSimilarityTask(word, suffix.substring(1));
                t1.fork();

                int sumOfSimilarities = 0;

                for (int i = 0; i < suffix.length(); i++) {
                    if (word.charAt(i) == suffix.charAt(i)) sumOfSimilarities++;
                    else break;
                }

                return sumOfSimilarities + t1.join();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println(
                "rec: " + new StringSimilarityTask("aaabaab", "aaabaab").invoke()
        );
        System.out.println(
                "linear: " + linearStringSimilarity("aaabaab")
        );
        System.out.println(
                "z-fun: " + (Arrays.stream(zFunStringSimilarity("aaabaab")).sum() + "aaabaab".length())
        );
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Path.of(TEST_10).toFile()))) {
            int t = Integer.parseInt(bufferedReader.readLine().trim());

            IntStream.range(0, t).forEach(tItr -> {
                try {
                    String s = bufferedReader.readLine();
                    System.out.println(
                            "linear: " + linearStringSimilarity(s)
                    );
                    System.out.println(
                            "z-fun: " + (Arrays.stream(zFunStringSimilarity(s)).sum() + s.length())
                    );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}
