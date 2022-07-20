package problemsolving.extralongfactorials;

import java.math.BigInteger;

public class Solution {

    /*
     * Complete the 'extraLongFactorials' function below.
     *
     * The function accepts INTEGER n as parameter.
     */

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
