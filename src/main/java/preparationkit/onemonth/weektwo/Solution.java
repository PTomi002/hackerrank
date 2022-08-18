package preparationkit.onemonth.weektwo;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Solution {

    // [MATH]:
    //      permutáció: n! hányfélekép lehet mindenkit kiválasztani
    //      variáció: n!/(n - k)! hányfélekép lehet n emberből k-t úgy kiv. hogy egymás mellett legyenek
    //      kombináció: n!/k!(n - k)! hányfélekép
    // idea:
    //      - azok a számok kellenek, amik a szám inverzének a 0-ból álló összes permutációja + a defaultok
    //      - írjunk egy gyors faktoriális számolást
    private static BigInteger factorial(long n) {
        BigInteger result = BigInteger.valueOf(1);
        for (long i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private static BigInteger combination(long n, long r) {
        return factorial(n).divide(factorial(r).multiply(factorial(n - r)));
    }

    public static long sumXor(long n) {
        if (n == 0) return 1;

        long numOf = 1, zeros = 0;
        String binary = Long.toBinaryString(n);

        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '0') {
                numOf++;
                zeros++;
            }
        }

        for (long i = 2; i <= zeros; i++) {
            numOf += combination(zeros, i).longValue();
        }

        return numOf;
    }

    // [MATH]:
    //      logaritmus váltás másik alapra: loga b = loge b / loge a
    //      e.g.: Math.log(128) / Math.log(2) = 7.0
    // idea:
    //      - megkeressük mennyire volt emelve, majd ha egész szám, felezzük, else levonjuk
    private static final String R = "R";
    private static final String L = "L";

    public static String counterGame(long n) {
        if (n == 1) return R;

        String next = R;
        do {
            if (next.equals(R)) next = L;
            else next = R;

            double power = Math.log(n) / Math.log(2);
            if (power % 1 == 0) { // check if 'power' is a whole number
                n /= 2;
            } else {
                n -= Math.pow(2, (int) power);// forget the abc.XYZ XYZ part
            }
        } while (n > 1);

        return next;
    }

    // idea:
    //      - stackbe töltjük a karaktereket, amíg ellen karaktere nem lesz és ellenőrizzük a párokat
    private static Deque<Character> delimiters = new ArrayDeque<>();

    public static String balancedDelimiters(String s) {
        for (Character c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{')
                delimiters.push(c);
            else {
                Optional<Character> delimiter = Optional.ofNullable(delimiters.poll());
                if (delimiter.isEmpty()) return "False";

                char lastDelimiter = delimiter.get();
                if (
                        (c == ')' && lastDelimiter != '(')
                                || (c == ']' && lastDelimiter != '[')
                                || (c == '}' && lastDelimiter != '{')
                )
                    return "False";
            }
        }
        return delimiters.isEmpty() ? "True" : "False";
    }

    // idea:
    //      - mivel bármelyik elempárban keressük, ezért sorrendezzük, mivel ott lesz a legkissebb diff
    //      - mozgó ablakot nézünk ahol a min az első eleme a windownak a max az utolsó
    public static int maxMin(int k, List<Integer> arr) {
        Collections.sort(arr);
        int unfairness = Integer.MAX_VALUE, min, max;
        for (int i = 0; i < arr.size() - k + 1; i++) {
            min = arr.get(i);
            max = arr.get(i + (k - 1));
            if (max - min < unfairness) {
                unfairness = max - min;
            }
        }
        return unfairness;
    }

    // idea:
    //      - palindromot ellenőrizzük az első mismatchig, itt vagy az eredeti indexen álló rossz ezt teszteljük, különben a fordított indexen álló párja a rossz
    //          , string builder-t használunk, ott a törlés gyors
    private static boolean isPalindrom(StringBuilder sb) {
        for (int i = 0; i < sb.length() / 2; i++) {
            if(sb.charAt(i) != sb.charAt(sb.length() - i - 1))
                return false;
        }
        return true;
    }

    public static int palindromeIndex(String s) {
        // Write your code here
        StringBuilder sb = new StringBuilder(s);

        for (int i = 0; i < sb.length() / 2; i++) {
            if(sb.charAt(i) != sb.charAt(s.length() - i - 1)) {
                if(isPalindrom(sb.deleteCharAt(i)))
                    return i;
                else
                    return s.length() - i - 1;
            }
        }
        return -1;
    }

    // idea:
    //      - olyan számokat keresünk, amiket az 'a' tömbben lévő elemek osztanak maradék nélkül 'b' tömbben lévők pedig hatványai
    //      - megoldás a 'a' tömbb max és a 'b' tömbb min között lesz
    public static int getTotalX(List<Integer> a, List<Integer> b) {
        int from = a.stream().max(Comparator.naturalOrder()).get();
        int to = b.stream().min(Comparator.naturalOrder()).get();
        int numOf = 0;

        outer: for (int i = from; i <= to; i++) {
            for (int j = 0; j < a.size(); j++) {
                if(i % a.get(j) != 0) {
                    continue outer;
                }
            }
            for (int j = 0; j < b.size(); j++) {
                if(b.get(j) % i != 0) {
                    continue outer;
                }
            }
            numOf++;
        }

        return numOf;
    }

    // idea:
    //      - akkor anagram, ha ugyan annyi és ugyan azon betűket tartalmazza
    //      - sb-vel töröljük ki a megtaláltakat, és a maradék, amit cserélni kell
    public static int anagram(String s) {
        if(s.length() % 2 != 0) return -1;

        int mid = s.length() / 2;
        StringBuilder fPart = new StringBuilder(s.substring(0, mid));

        for (int i = mid; i < s.length(); i++) {
            int idx = fPart.indexOf(String.valueOf(s.charAt(i)));
            if(idx != -1) fPart.deleteCharAt(idx);
        }

        return fPart.length();
    }

    private static void test(Object o) {
    }

    private static void test(String s) {
    }

    public static void main(String[] args) {
        System.out.println(
                Math.log(128) / Math.log(2)
        );

        System.out.println(
                sumXor(5)
        );
        System.out.println(
                sumXor(42)
        );
        System.out.println(
                sumXor(570)
        );
        System.out.println(
                counterGame(
                        2079084773
                )
        );
        System.out.println(
                counterGame(
                        6
                )
        );
        System.out.println(
                maxMin(
                        5,
                        new ArrayList<>(List.of(
                                822, 1520, 1784, 2422, 2822, 4504, 5857, 4094, 4157, 3902, 6643, 7288,
                                8245, 9948, 7802, 3142, 9739, 5629, 5413, 7232
                        ))
                )
        );

        // which one is called?
        test(null);
        // why the string?
        // because Java calls the most specific one, as all can take null as valid parameter Java choose the String

        Scanner in = new Scanner(System.in);
        System.out.print(balancedDelimiters(in.nextLine()));
    }
}
