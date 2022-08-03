package preparationkit.dictionaries.ransomenote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

    // idea:
    //      - számoljuk össze szavanként miből mennyi van és vonjuk ki, ha nincs elég akkor print "no"
    public static void checkMagazine(List<String> magazine, List<String> note) {
        Map<String, Long> m = magazine.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> n = note.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (Map.Entry<String, Long> entry : n.entrySet()) {
            long numOfWord = m.getOrDefault(entry.getKey(), 0L);
            if (numOfWord - entry.getValue() < 0) {
                System.out.println("No");
                return;
            }
        }

        System.out.println("Yes");
    }

    public static void main(String[] args) {
        checkMagazine( // expected = No
                new ArrayList<>(List.of("two", "times", "three", "is", "not", "four")),
                new ArrayList<>(List.of("two", "times", "two", "is", "four"))
        );
    }
}
