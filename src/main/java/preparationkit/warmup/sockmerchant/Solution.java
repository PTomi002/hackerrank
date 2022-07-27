package preparationkit.warmup.sockmerchant;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

    // idea:
    //      - csoportosítsuk színek szerint, majd nézzük meg mennyi pár van benne
    public static long sockMerchant(int n, List<Integer> ar) {
        return ar
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        )
                )
                .values()
                .stream()
                .mapToLong(s -> {
                    if (s % 2 != 0) s--;
                    return s / 2;
                })
                .sum();
    }

    public static void main(String[] args) {
        // expected = 2
        System.out.println(
                sockMerchant(
                        7,
                        List.of(1, 2, 1, 2, 1, 3, 2)
                )
        );
    }
}
