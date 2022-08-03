package preparationkit.dictionaries.twostrings;

import java.util.Set;
import java.util.stream.Collectors;

public class Solution {

    // idea:
    //      - "A substring may be as small as one character." -y karakterenkénti közös metszet elég
    public static String twoStrings(String s1, String s2) {
        Set<Character> s1Characters = s1.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
        Set<Character> s2Characters = s2.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());

        s1Characters.retainAll(s2Characters); // ne az if-be legyen, mert ha ugyan az a két halmaz akkor nem változik meg a set
        if (!s1Characters.isEmpty()) {
            return "YES";
        }
        return "NO";
    }

    public static void main(String[] args) {
        System.out.println( // expected = TRUE
                twoStrings(
                        "hello",
                        "world"
                )
        );
        System.out.println( // expected = YES
                twoStrings(
                        "hello",
                        "hello"
                )
        );
    }
}
