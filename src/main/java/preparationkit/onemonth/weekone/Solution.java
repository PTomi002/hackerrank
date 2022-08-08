package preparationkit.onemonth.weekone;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

    // idea:
    //      - 'a' means AM/PM
    private static String timeConversion(String s) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("hh:mm:ssa", Locale.US);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(parser.parse(s));
    }

    // idea:
    //      - count all the elements in a map to find the only non-duplicate one
    private static int lonelyinteger(List<Integer> a) {
        return a
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    // signed 32bit int range: −2^32/2 (= -2147483648) to 2^32/2−1 (= 2147483647) because it uses 1 bit to sign negative numbers
    // unsigned 32bit int range: 0 to 2^32−1 (= 4294967295) because it uses all the 32 bits
    // bitwise operations: |, &, ^ (xor), ~ (complement)
    private static long flippingBits(long n) {
        for (int i = 0; i < 32; i++) {
            n = n ^ (1 << i); // xor with a mask
        }
        return Integer.toUnsignedLong((int) n); // forget the highest 32 bit
    }

    // idea:
    //      - negate all, forget the highest 32 bit, then change it to unsigned
    private static long flippingBitsWithComplement(long n) {
        return Integer.toUnsignedLong((int) ~n);
    }

    // idea:
    //      - ascii table a=97 but we can compare with the character itself as it is an int
    private static String pangrams(String s) {
        Set<Character> characters = s
                .chars()
                .filter(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                .mapToObj(c -> (char) Character.toLowerCase(c))
                .collect(Collectors.toSet());
        if (characters.size() == 26)
            return "pangram";
        return "not pangram";
    }

    // idea:
    //      - mivel szimmetrikusan tud csak egy szám helyet cserélni 3 másik helyre, ezért elég ezeknek a maximumát ellenőrizni
    //          mert a cserék száma nem számít, így tudnak mozogni azon a 4 helyen, mintha körök lennének
    private static int max(int... numbers) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) max = numbers[i];
        }
        return max;
    }

    public static int flippingMatrix(List<List<Integer>> matrix) {
        // Write your code here
        int n = matrix.size() / 2, sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum += max(
                        matrix.get(i).get(j),
                        matrix.get(i).get(matrix.size() - 1 - j),
                        matrix.get(matrix.size() - 1 - i).get(matrix.size() - 1 - j),
                        matrix.get(matrix.size() - 1 - i).get(j)
                );
            }
        }
        return sum;
    }

    // idea:
    //      - use bigXYZ to be able to scale for the required format
    public static void plusMinus(List<Integer> arr) {
        // Write your code here
        double size = arr.size(), numOfNegative = 0, numOfPositive = 0, numOfZero = 0;

        for (double i = 0; i < size; i++) {
            int actual = arr.get((int) i);
            if (actual == 0) numOfZero++;
            else if (actual < 0) numOfNegative++;
            else numOfPositive++;
        }

        System.out.println(new BigDecimal(numOfPositive / size).setScale(6, RoundingMode.HALF_UP));
        System.out.println(new BigDecimal(numOfNegative / size).setScale(6, RoundingMode.HALF_UP));
        System.out.println(new BigDecimal(numOfZero / size).setScale(6, RoundingMode.HALF_UP));
    }

    public static void main(String[] args) {
        System.out.println( // expected = 19:05:45
                timeConversion(
                        "07:05:45PM"
                )
        );
        System.out.println( // expected = 4
                lonelyinteger(
                        List.of(1, 2, 3, 4, 3, 2, 1)
                )
        );
        System.out.println( // expected = 4294967286
                flippingBitsWithComplement(
                        9
                )
        );
        // expected = 0.400000, 0.400000, 0.200000
        plusMinus(
                List.of(1, 1, 0, -1, -1)
        );
        System.out.println( // expected = not pangram ('a' is missing from 'a'ntique)
                pangrams("We promptly judged ntique ivory buckles for the next prize")
        );
        System.out.println( // expected = 414
                flippingMatrix(
                        new ArrayList<>(List.of(
                                new ArrayList<>(List.of(112, 42, 83, 119)),
                                new ArrayList<>(List.of(56, 125, 56, 49)),
                                new ArrayList<>(List.of(15, 78, 101, 43)),
                                new ArrayList<>(List.of(62, 98, 114, 108))
                        ))
                )
        );

        System.out.println( // expected = 488
                flippingMatrix(
                        new ArrayList<>(List.of(
                                new ArrayList<>(List.of(107, 54, 128, 15)),
                                new ArrayList<>(List.of(12, 75, 110, 138)),
                                new ArrayList<>(List.of(100, 96, 34, 85)),
                                new ArrayList<>(List.of(75, 15, 28, 112))
                        ))
                )
        );
    }
}
