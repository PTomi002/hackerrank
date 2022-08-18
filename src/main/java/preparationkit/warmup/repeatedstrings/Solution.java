package preparationkit.warmup.repeatedstrings;

public class Solution {

    public static long repeatedString(String s, long n) {
        long numOfa = 0;
        for (int i = 0; i < s.length(); i++) if ('a' == s.charAt(i)) numOfa++;

        long numOfSubString = n / s.length(); // long loses the fractional remainder, e.g.: 4.9 -> 4
        long remain = n % s.length(); //  pl 3*3=9 de 10-ig kell akkor a maradék 1, ami az indexe a substringnek hogy meddig kell

        if (remain == 0) return numOfSubString * numOfa;
        else {
            long result = numOfSubString * numOfa;
            String sub = s.substring(0, (int) remain); // compact string = some char can be stored on only in 2 byte but most characters can be stored in 1 byte (saves memory)
            for (int i = 0; i < sub.length(); i++) if ('a' == sub.charAt(i)) result++;
            return result;
        }
    }

    public static void main(String[] args) {
        // expected = 4
        System.out.println(
                repeatedString(
                        "abcac",
                        10
                )
        );
        // expected = 7
        System.out.println(
                repeatedString(
                        "aba",
                        10
                )
        );
        // expected = 588525
        System.out.println(
                repeatedString(
                        "aab",
                        882787
                )
        );
        // expected = 164280
        System.out.println(
                repeatedString(
                        "gfcaaaecbg",
                        547602
                )
        );
    }
}
