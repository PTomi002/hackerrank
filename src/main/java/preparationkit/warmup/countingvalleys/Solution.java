package preparationkit.warmup.countingvalleys;

public class Solution {

    // ötlet:
    //      - ne tároljunk stacket meg semmit, elég egy levelt mozgatni és ha 0-ról megyünk le, akkor völgy
    // --i => first modifies i then reads it
    // i-- => first reads i then modifies it
    public static int countingValleys(int steps, String path) {
        int level = 0, valleys = 0;
        for (int i = 0; i < path.length(); i++) {
            char p = path.charAt(i);
            if ('D' == p && level-- == 0) valleys++;
            if ('U' == p) level++;
        }
        return valleys;
    }

    public static void main(String[] args) {
        // expected = 2
        System.out.println(
                countingValleys(
                        12,
                        "DDUUDDUDUUUD"
                )
        );
        // expected = 1
        System.out.println(
                countingValleys(
                        8,
                        "UDDDUDUU"
                )
        );
    }
}
