package preparationkit.onemonth.amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution {

    // idea:
    //      - megszámoljuk a sender és recipienteket egyszerűen, majd stream-el szűrjük
    public static List<String> processLogs(List<String> logs, int threshold) {
        Map<Long, Long> transactions = new HashMap<>();
        for (String s : logs) {
            String[] log = s.split(" ");
            long sender = Long.parseLong(log[0]);
            long recipient = Long.parseLong(log[1]);
            if (sender == recipient)
                transactions.compute(sender, (key, value) -> value == null ? 1L : value + 1L);
            else {
                transactions.compute(sender, (key, value) -> value == null ? 1L : value + 1L);
                transactions.compute(recipient, (key, value) -> value == null ? 1L : value + 1L);
            }
        }
        return transactions
                .entrySet()
                .stream()
                .filter(e -> e.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    // idea:
    //      -
    public static List<Integer> numberOfItems(String s, List<Integer> startIndices, List<Integer> endIndices) {
        Map<Integer[], Integer> containers = new HashMap<>();

        int from = 0, sum = 0;
        boolean canCount = false;
        for (int i = 0; i < s.length(); i++) {
            if(canCount && '|' == s.charAt(i)) {
                containers.put(new Integer[] {from, i}, sum);
                canCount = false;
                sum = 0;
            }
            if(canCount && '|' != s.charAt(i)) sum++;
            if(!canCount && '|' == s.charAt(i)) {
                canCount = true;
                from = i;
            }
        }

        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < startIndices.size(); i++) {
            int start = startIndices.get(i) - 1;
            int end = endIndices.get(i) - 1;

            int tmp = containers.entrySet()
                    .stream()
                    .filter(e -> {
                        int st = e.getKey()[0];
                        int en = e.getKey()[1];

                        return st >= start && en <= end;
                    })
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            results.add(tmp);
        }

        return results;
    }

    public static void main(String[] args) {
        System.out.println(
                processLogs( // expected = [1, 2]
                        new ArrayList<>(List.of(
                                "1 2 10",
                                "1 7 20",
                                "1 3 30",
                                "2 2 40"
                        )),
                        2
                )
        );
        System.out.println(
                numberOfItems( // expected = 1
                        "*|*|",
                        List.of(2,1),
                        List.of(4,3)
                )
        );
    }
}
