package problemsolving.extralongfactorials;

import java.math.BigInteger;

public class Solution {

    // idea:
    //      - use BigInteger as it can handle all the huge numbers
    public static BigInteger extraLongFactorials(int n) {
        BigInteger factorial = BigInteger.valueOf(n);
        for (int i = n -1 ; i > 0; i--) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }

    public static void main(String[] args) {
        System.out.println(
                extraLongFactorials(30)
        );
    }
}
