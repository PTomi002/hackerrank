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
    //      - állapotgéppel kialakítjuk a rangeket majd mapbe beletesszük
    //      - minden ranget lecsekkolunk, hogy benne van-e a megadottban
    // problem:
    //      - TimeOutError
    public static List<Integer> numberOfItemsTrivial(String s, List<Integer> startIndices, List<Integer> endIndices) {
        Map<Integer[], Integer> containers = new HashMap<>();

        int from = 0, sum = 0;
        boolean canCount = false;
        for (int i = 0; i < s.length(); i++) {
            if (canCount && '|' == s.charAt(i) && sum == 0) {
                canCount = false;
            }
            if (canCount && '|' == s.charAt(i)) {
                containers.put(new Integer[]{from, i}, sum);
                canCount = false;
                sum = 0;
            }
            if (canCount && '|' != s.charAt(i)) sum++;
            if (!canCount && '|' == s.charAt(i)) {
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

    // [ALG]: binary search tree (BST)
    // idea:
    //      - kialakított rangeket egy bst-be forgatjuk
    //      - majd csekkoljuk hogy érdemes-e a bal vagy jobb subtreeben folytatni a keresést
    private static class Range {
        int low, high, numOfItems;

        public Range(int low, int high) {
            this.low = low;
            this.high = high;
        }

        public Range(int low, int high, int numOfItems) {
            this.low = low;
            this.high = high;
            this.numOfItems = numOfItems;
        }
    }

    private static class BinarySearchTree {
        public static class Node {
            Range range;
            Node left, right;

            public Node(Range range) {
                this.range = range;
            }
        }

        private Node root;

        public void createBST(List<Range> arr) {
            root = createBST(arr, 0, arr.size() - 1);
        }

        public int intervalSearch(Range x) {
            return intervalSearch(root, x);
        }

        private Node createBST(List<Range> arr, int start, int end) {
            if (start > end)
                return null;

            int mid = (start + end) / 2;
            Node node = new Node(arr.get(mid));

            node.left = createBST(arr, start, mid - 1);
            node.right = createBST(arr, mid + 1, end);

            return node;
        }

        private int intervalSearch(Node root, Range x) {
            if (root == null) return 0;

            int sum = 0;
            if (x.low <= root.range.low && x.high >= root.range.high)
                sum += root.range.numOfItems;

            if (root.left != null && x.low <= root.range.low)
                sum += intervalSearch(root.left, x);

            if (root.right != null && x.high >= root.range.high)
                sum += intervalSearch(root.right, x);

            return sum;
        }
    }

    public static List<Integer> numberOfItems(String s, List<Integer> startIndices, List<Integer> endIndices) {
        BinarySearchTree bst = new BinarySearchTree();
        List<Range> ranges = new ArrayList<>();

        int from = 0, sum = 0;
        boolean canCount = false;
        for (int i = 0; i < s.length(); i++) {
            if (canCount && '|' == s.charAt(i) && sum == 0) {
                canCount = false;
            }
            if (canCount && '|' == s.charAt(i)) {
                ranges.add(new Range(from, i, sum));
                canCount = false;
                sum = 0;
            }
            if (canCount && '|' != s.charAt(i))
                sum++;
            if (!canCount && '|' == s.charAt(i)) {
                canCount = true;
                from = i;
            }
        }

        bst.createBST(ranges);
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < startIndices.size(); i++) {
            int start = startIndices.get(i) - 1;
            int end = endIndices.get(i) - 1;

            results.add(bst.intervalSearch(new Range(start, end)));
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
                numberOfItemsTrivial( // expected = [2,0]
                        "**|*|*|***|**",
                        List.of(2, 4),
                        List.of(7, 6)
                )
        );
        System.out.println(
                numberOfItems( // expected = [0]
                        "*|*|",
                        List.of(1),
                        List.of(3)
                )
        );
        System.out.println(
                numberOfItemsTrivial(
                        "|||||******|**|****|******|*|*||*|******|*||**|***|***|**||*|**|***|*|*|**||***|******|*|||*****||||",
                        List.of(53, 7, 16, 65, 51, 44, 42, 20, 19, 44, 8, 5, 4, 7, 66, 8, 14, 84, 94, 45, 3, 82, 7, 29, 7, 14, 62, 11, 17, 46, 42, 18, 66, 11, 48, 50, 43, 60, 1, 7, 12, 73, 83, 41, 52, 76, 39, 57, 23, 19, 7, 8, 66, 20, 54, 1, 51, 25, 33, 73, 52, 28, 87, 22, 12, 56, 39, 13, 45, 32, 10, 14, 12, 45, 36, 39, 60, 24, 49, 12, 50, 82, 43, 7, 56, 5, 43, 18, 32, 53, 39, 43, 75, 29, 25, 10, 29, 53, 2, 22, 18, 5, 12, 68, 14, 81, 17, 67, 16, 54, 73, 63, 64, 65, 17, 30, 35, 86, 53, 26, 6, 13, 29, 14, 29, 58, 2, 17, 12, 33, 6, 22, 8, 9, 3, 43, 8, 42, 7, 16, 35, 25, 7, 4, 37, 28, 68, 7, 27, 39, 32, 81, 74, 83, 1, 14, 26, 2, 5, 53, 93, 87, 1, 24, 17, 35, 48, 21, 40, 63, 10, 61, 37, 7, 59, 39, 9, 24, 47, 71, 8, 14, 16, 13, 8, 81, 19, 41, 7, 32, 22, 65, 53, 43, 8, 22, 54, 36, 69, 14, 41, 11, 13, 22, 5, 24, 26, 31, 74, 19, 46, 21, 30, 22, 74, 14, 34, 4, 26, 28, 3, 9, 16, 10, 23, 20, 26, 41, 14, 35, 76, 53, 28, 67, 67, 31, 15, 10, 59, 33, 50, 5, 22, 40, 6, 31, 35, 41, 15, 63, 69, 16, 34, 4, 4, 5, 28, 2, 3, 41, 24, 36, 58, 24, 45, 15, 13, 18, 22, 42, 1, 29, 42, 20, 22, 27, 86, 55, 61, 69, 44, 19, 83, 7, 9, 62, 19, 16, 23, 35, 85, 29, 45, 82, 28, 50, 20, 6, 5, 60, 56, 74, 55, 61, 75, 11, 59, 80, 35, 29, 11, 17, 16, 90, 4, 9, 90, 3, 63, 11, 68, 54, 68, 55, 30, 83, 52, 64, 19, 5, 13, 21, 2, 17, 19, 27, 23, 68, 24, 45, 48, 81, 79, 96, 5, 86, 13, 63, 39, 9, 39, 47, 43, 4, 43, 70, 20, 60, 67, 11, 4, 30, 53, 48, 81, 37, 41, 50, 5, 1, 4, 17, 41, 91, 5, 23, 35, 4, 39, 37, 7, 24, 25, 6, 30, 82, 32, 17, 9, 14, 15, 15, 19, 3, 21, 4, 94, 23, 19, 78, 60, 25, 1, 49, 61, 75, 71, 66, 52, 43, 69, 90, 63, 33, 13, 33, 53, 35, 35, 6, 43, 6, 4, 16, 7, 58, 4, 18, 56, 2, 34, 5, 54, 13, 25, 53, 58, 42, 13, 20, 64, 67, 61, 23, 16, 64, 23, 16, 15, 1, 50, 11, 52, 23, 81, 46, 15, 15, 27, 37, 6, 6, 30, 45, 54, 55, 42, 20, 42, 29, 26, 15, 92, 19, 42, 61, 18, 42, 31, 49, 20, 13, 28, 68, 48, 51, 33, 17, 76, 17, 47, 39, 17, 43, 73, 44, 5, 52, 24, 79),
                        List.of(93, 75, 81, 80, 100, 95, 80, 57, 48, 98, 23, 46, 12, 78, 73, 58, 77, 92, 100, 72, 66, 88, 59, 38, 88, 72, 76, 17, 87, 52, 53, 61, 96, 62, 85, 55, 50, 83, 73, 88, 59, 73, 86, 72, 87, 93, 88, 62, 36, 83, 74, 84, 68, 67, 90, 79, 64, 49, 66, 84, 100, 42, 90, 50, 69, 71, 94, 78, 57, 46, 50, 47, 71, 62, 86, 80, 85, 33, 82, 97, 53, 95, 60, 99, 91, 97, 52, 64, 65, 60, 69, 54, 98, 98, 57, 58, 91, 72, 11, 57, 53, 62, 31, 77, 27, 83, 23, 88, 98, 64, 92, 73, 67, 96, 78, 74, 35, 98, 65, 79, 31, 48, 100, 78, 67, 83, 45, 30, 25, 41, 94, 80, 92, 77, 12, 87, 51, 88, 50, 65, 99, 36, 44, 7, 83, 47, 94, 86, 84, 94, 38, 97, 82, 85, 80, 50, 52, 95, 32, 67, 99, 94, 18, 45, 36, 73, 67, 81, 57, 71, 64, 84, 89, 21, 94, 86, 32, 78, 84, 74, 15, 95, 74, 94, 72, 83, 71, 60, 47, 100, 57, 86, 61, 89, 16, 59, 76, 49, 81, 40, 48, 73, 18, 25, 12, 68, 61, 85, 79, 81, 77, 54, 41, 50, 87, 90, 62, 24, 98, 89, 54, 80, 45, 89, 28, 21, 56, 86, 67, 40, 100, 85, 89, 88, 77, 55, 70, 37, 71, 77, 97, 53, 93, 42, 29, 57, 56, 45, 29, 91, 95, 73, 84, 94, 80, 27, 79, 10, 23, 80, 31, 39, 76, 83, 56, 51, 87, 48, 73, 76, 68, 81, 98, 82, 44, 99, 97, 65, 99, 78, 95, 59, 88, 55, 100, 82, 62, 42, 55, 51, 96, 93, 70, 99, 43, 89, 72, 47, 8, 68, 93, 81, 74, 89, 96, 44, 91, 88, 86, 62, 65, 88, 31, 93, 72, 80, 97, 53, 94, 15, 78, 79, 93, 58, 71, 94, 82, 82, 26, 42, 30, 78, 26, 80, 46, 38, 32, 91, 78, 53, 79, 98, 82, 100, 100, 94, 14, 66, 94, 82, 60, 62, 66, 18, 100, 91, 78, 71, 98, 54, 18, 39, 70, 60, 98, 69, 67, 83, 32, 75, 22, 33, 87, 92, 55, 44, 58, 79, 51, 39, 32, 25, 26, 56, 58, 85, 98, 72, 85, 41, 58, 24, 44, 82, 41, 70, 99, 79, 37, 100, 94, 84, 57, 93, 62, 86, 100, 98, 70, 100, 92, 98, 77, 64, 94, 58, 68, 61, 68, 80, 48, 71, 94, 36, 35, 99, 7, 40, 83, 47, 49, 53, 91, 73, 70, 72, 64, 58, 78, 73, 82, 70, 88, 36, 38, 69, 73, 76, 81, 39, 53, 66, 62, 75, 95, 97, 76, 15, 51, 43, 88, 62, 74, 90, 90, 83, 45, 55, 49, 81, 88, 77, 92, 30, 66, 81, 23, 53, 91, 96, 83, 51, 70, 76, 64, 56, 74, 18, 99, 35, 64, 49, 90, 59, 91, 55, 25, 57, 26, 89)
                )
        );
        System.out.println(
                numberOfItems(
                        "|||||******|**|****|******|*|*||*|******|*||**|***|***|**||*|**|***|*|*|**||***|******|*|||*****||||",
                        List.of(53, 7, 16, 65, 51, 44, 42, 20, 19, 44, 8, 5, 4, 7, 66, 8, 14, 84, 94, 45, 3, 82, 7, 29, 7, 14, 62, 11, 17, 46, 42, 18, 66, 11, 48, 50, 43, 60, 1, 7, 12, 73, 83, 41, 52, 76, 39, 57, 23, 19, 7, 8, 66, 20, 54, 1, 51, 25, 33, 73, 52, 28, 87, 22, 12, 56, 39, 13, 45, 32, 10, 14, 12, 45, 36, 39, 60, 24, 49, 12, 50, 82, 43, 7, 56, 5, 43, 18, 32, 53, 39, 43, 75, 29, 25, 10, 29, 53, 2, 22, 18, 5, 12, 68, 14, 81, 17, 67, 16, 54, 73, 63, 64, 65, 17, 30, 35, 86, 53, 26, 6, 13, 29, 14, 29, 58, 2, 17, 12, 33, 6, 22, 8, 9, 3, 43, 8, 42, 7, 16, 35, 25, 7, 4, 37, 28, 68, 7, 27, 39, 32, 81, 74, 83, 1, 14, 26, 2, 5, 53, 93, 87, 1, 24, 17, 35, 48, 21, 40, 63, 10, 61, 37, 7, 59, 39, 9, 24, 47, 71, 8, 14, 16, 13, 8, 81, 19, 41, 7, 32, 22, 65, 53, 43, 8, 22, 54, 36, 69, 14, 41, 11, 13, 22, 5, 24, 26, 31, 74, 19, 46, 21, 30, 22, 74, 14, 34, 4, 26, 28, 3, 9, 16, 10, 23, 20, 26, 41, 14, 35, 76, 53, 28, 67, 67, 31, 15, 10, 59, 33, 50, 5, 22, 40, 6, 31, 35, 41, 15, 63, 69, 16, 34, 4, 4, 5, 28, 2, 3, 41, 24, 36, 58, 24, 45, 15, 13, 18, 22, 42, 1, 29, 42, 20, 22, 27, 86, 55, 61, 69, 44, 19, 83, 7, 9, 62, 19, 16, 23, 35, 85, 29, 45, 82, 28, 50, 20, 6, 5, 60, 56, 74, 55, 61, 75, 11, 59, 80, 35, 29, 11, 17, 16, 90, 4, 9, 90, 3, 63, 11, 68, 54, 68, 55, 30, 83, 52, 64, 19, 5, 13, 21, 2, 17, 19, 27, 23, 68, 24, 45, 48, 81, 79, 96, 5, 86, 13, 63, 39, 9, 39, 47, 43, 4, 43, 70, 20, 60, 67, 11, 4, 30, 53, 48, 81, 37, 41, 50, 5, 1, 4, 17, 41, 91, 5, 23, 35, 4, 39, 37, 7, 24, 25, 6, 30, 82, 32, 17, 9, 14, 15, 15, 19, 3, 21, 4, 94, 23, 19, 78, 60, 25, 1, 49, 61, 75, 71, 66, 52, 43, 69, 90, 63, 33, 13, 33, 53, 35, 35, 6, 43, 6, 4, 16, 7, 58, 4, 18, 56, 2, 34, 5, 54, 13, 25, 53, 58, 42, 13, 20, 64, 67, 61, 23, 16, 64, 23, 16, 15, 1, 50, 11, 52, 23, 81, 46, 15, 15, 27, 37, 6, 6, 30, 45, 54, 55, 42, 20, 42, 29, 26, 15, 92, 19, 42, 61, 18, 42, 31, 49, 20, 13, 28, 68, 48, 51, 33, 17, 76, 17, 47, 39, 17, 43, 73, 44, 5, 52, 24, 79),
                        List.of(93, 75, 81, 80, 100, 95, 80, 57, 48, 98, 23, 46, 12, 78, 73, 58, 77, 92, 100, 72, 66, 88, 59, 38, 88, 72, 76, 17, 87, 52, 53, 61, 96, 62, 85, 55, 50, 83, 73, 88, 59, 73, 86, 72, 87, 93, 88, 62, 36, 83, 74, 84, 68, 67, 90, 79, 64, 49, 66, 84, 100, 42, 90, 50, 69, 71, 94, 78, 57, 46, 50, 47, 71, 62, 86, 80, 85, 33, 82, 97, 53, 95, 60, 99, 91, 97, 52, 64, 65, 60, 69, 54, 98, 98, 57, 58, 91, 72, 11, 57, 53, 62, 31, 77, 27, 83, 23, 88, 98, 64, 92, 73, 67, 96, 78, 74, 35, 98, 65, 79, 31, 48, 100, 78, 67, 83, 45, 30, 25, 41, 94, 80, 92, 77, 12, 87, 51, 88, 50, 65, 99, 36, 44, 7, 83, 47, 94, 86, 84, 94, 38, 97, 82, 85, 80, 50, 52, 95, 32, 67, 99, 94, 18, 45, 36, 73, 67, 81, 57, 71, 64, 84, 89, 21, 94, 86, 32, 78, 84, 74, 15, 95, 74, 94, 72, 83, 71, 60, 47, 100, 57, 86, 61, 89, 16, 59, 76, 49, 81, 40, 48, 73, 18, 25, 12, 68, 61, 85, 79, 81, 77, 54, 41, 50, 87, 90, 62, 24, 98, 89, 54, 80, 45, 89, 28, 21, 56, 86, 67, 40, 100, 85, 89, 88, 77, 55, 70, 37, 71, 77, 97, 53, 93, 42, 29, 57, 56, 45, 29, 91, 95, 73, 84, 94, 80, 27, 79, 10, 23, 80, 31, 39, 76, 83, 56, 51, 87, 48, 73, 76, 68, 81, 98, 82, 44, 99, 97, 65, 99, 78, 95, 59, 88, 55, 100, 82, 62, 42, 55, 51, 96, 93, 70, 99, 43, 89, 72, 47, 8, 68, 93, 81, 74, 89, 96, 44, 91, 88, 86, 62, 65, 88, 31, 93, 72, 80, 97, 53, 94, 15, 78, 79, 93, 58, 71, 94, 82, 82, 26, 42, 30, 78, 26, 80, 46, 38, 32, 91, 78, 53, 79, 98, 82, 100, 100, 94, 14, 66, 94, 82, 60, 62, 66, 18, 100, 91, 78, 71, 98, 54, 18, 39, 70, 60, 98, 69, 67, 83, 32, 75, 22, 33, 87, 92, 55, 44, 58, 79, 51, 39, 32, 25, 26, 56, 58, 85, 98, 72, 85, 41, 58, 24, 44, 82, 41, 70, 99, 79, 37, 100, 94, 84, 57, 93, 62, 86, 100, 98, 70, 100, 92, 98, 77, 64, 94, 58, 68, 61, 68, 80, 48, 71, 94, 36, 35, 99, 7, 40, 83, 47, 49, 53, 91, 73, 70, 72, 64, 58, 78, 73, 82, 70, 88, 36, 38, 69, 73, 76, 81, 39, 53, 66, 62, 75, 95, 97, 76, 15, 51, 43, 88, 62, 74, 90, 90, 83, 45, 55, 49, 81, 88, 77, 92, 30, 66, 81, 23, 53, 91, 96, 83, 51, 70, 76, 64, 56, 74, 18, 99, 35, 64, 49, 90, 59, 91, 55, 25, 57, 26, 89)
                )
        );
    }
}
