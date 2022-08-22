package preparationkit.onemonth.weekthree;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

    // idea:
    //      - command patternnel készítsük el a text editort
    //      - undo commandok mindig párban állnak a do commandokkal
    @FunctionalInterface
    private interface Edit<T> {
        T edit();
    }

    private record Append(StringBuilder b, String s) implements Edit<Void> {

        @Override
        public Void edit() {
            b.append(s);
            return null;
        }
    }

    private record UndoAppend(StringBuilder b, String s) implements Edit<Void> {

        @Override
        public Void edit() {
            b.delete(b.lastIndexOf(s), b.length());
            return null;
        }
    }

    private record Delete(StringBuilder b, int k) implements Edit<Void> {

        @Override
        public Void edit() {
            b.delete(b.length() - k, b.length());
            return null;
        }
    }

    private static class UndoDelete implements Edit<Void> {
        private final StringBuilder b;
        private final String s;

        private UndoDelete(StringBuilder b, int k) {
            this.b = b;
            this.s = b.substring(b.length() - k);
        }

        @Override
        public Void edit() {
            b.append(s);
            return null;
        }
    }

    private record Print(StringBuilder b, int k) implements Edit<Character> {

        @Override
        public Character edit() {
            return b.charAt(k - 1);
        }
    }

    private static class TextEditor {
        private final StringBuilder b = new StringBuilder();
        private final Deque<Edit<?>> q = new ArrayDeque<>();

        public void append(String s) {
            q.offerLast(new UndoAppend(b, s));
            new Append(b, s).edit();
        }

        public void delete(int k) {
            q.offerLast(new UndoDelete(b, k));
            new Delete(b, k).edit();
        }

        public void print(int k) {
            System.out.println(new Print(b, k).edit());
        }

        public void undo() {
            q.pollLast().edit();
        }
    }

    private static final char SECOND_BOMB = 'X';
    private static final char BOMB = 'O';
    private static final char EMPTY = '.';

    // idea:
    //      - kiindulási állapot után ismétlődnek a ciklusok 4 virtuális secenként
    //      - n ha nagyobb mint 5 akkor letranszformáljuk a 4 ismétlődő ciklusra, majd szimuláljuk a játékot
    public static List<String> bomberMan(int n, List<String> grid) {
        if(n == 1) return grid;

        int rounds = n, rows = grid.size(), columns = grid.get(0).length();
        if(rounds > 5) {
            int remainder = n % 4;
            if(remainder == 0) rounds = 4;
            else if(remainder == 1) rounds = 5;
            else rounds = remainder;
        }

        char [][] map = new char[rows][];
        for (int i = 0; i < rows; i++) {
            map[i] = new char[columns];
            for (int j = 0; j < columns; j++) {
                map[i][j] = grid.get(i).charAt(j);
            }
        }

        char currentBomb = SECOND_BOMB;
        char previousBomb = BOMB;
        for (int i = 2; i <= rounds; i++) {
            if(i % 2 ==0) {
                for (int j = 0; j < rows; j++) {
                    for (int k = 0; k < columns; k++) {
                        if(EMPTY == map[j][k]) map[j][k] = currentBomb;
                    }
                }
            } else {
                for (int j = 0; j < rows; j++) {
                    for (int k = 0; k < columns; k++) {
                        if(previousBomb == map[j][k]) {
                            map[j][k] = EMPTY;
                            if(j + 1 < rows && map[j + 1][k] != previousBomb) map[j + 1][k] = EMPTY;
                            if(j - 1 > -1 && map[j - 1][k] != previousBomb) map[j - 1][k]  = EMPTY;
                            if(k + 1 < columns && map[j][k + 1] != previousBomb) map[j][k + 1] = EMPTY;
                            if(k - 1 > -1 && map[j][k - 1] != previousBomb) map[j][k - 1] = EMPTY;
                        }
                    }
                }
                char tmp = currentBomb;
                currentBomb = previousBomb;
                previousBomb = tmp;
            }
        }

        return Arrays.stream(map)
                .map(String::new)
                .map(s -> s.replace("X", "O"))
                .collect(Collectors.toList());
    }

    // idea:
    //      - ha a min és a max karakterszám egyezik jók vagyunk
    //      - ha max - 1 megegyezik a minimummal, akkor meg kell nézni, hogy a következő maximum is megegyezik-e a minimummal
    //      - ha a min - 1 megegyezik 0-val, akkor meg kell nézni, hogy a kövi min megegyezik-e a maximummal
    public static String isValid(String s) {
        if(s.length() == 1) return "YES";

        Map<Character, Long> stats = s.chars()
                .mapToObj(c -> (char) c)
                .collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        )
                );

        Map.Entry<Character, Long> max = stats.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        Map.Entry<Character, Long> min = stats.entrySet().stream().min(Map.Entry.comparingByValue()).get();
        stats.remove(max.getKey());
        stats.remove(min.getKey());
        Map.Entry<Character, Long> nextMax = stats.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        Map.Entry<Character, Long> nextMin = stats.entrySet().stream().min(Map.Entry.comparingByValue()).get();

        if(Long.compare(max.getValue(), min.getValue()) == 0) return "YES";
        if(
                Long.compare(max.getValue() - 1, min.getValue()) == 0 && Long.compare(nextMax.getValue(), min.getValue()) == 0
                        ||
                        Long.compare(min.getValue() - 1, 0L) == 0 && Long.compare(nextMin.getValue(), max.getValue()) == 0
        ) return "YES";

        return "NO";
    }

    // [ALG]: reverse (double) linked list, merge lists, insert into list
    static class SinglyLinkedListNode {
        public int data;
        public SinglyLinkedListNode next;

        public SinglyLinkedListNode(int nodeData) {
            this.data = nodeData;
            this.next = null;
        }
    }

    static class SinglyLinkedList {
        public SinglyLinkedListNode head;
        public SinglyLinkedListNode tail;

        public SinglyLinkedList() {
            this.head = null;
            this.tail = null;
        }

        public void insertNode(int nodeData) {
            SinglyLinkedListNode node = new SinglyLinkedListNode(nodeData);

            if (this.head == null) {
                this.head = node;
            } else {
                this.tail.next = node;
            }

            this.tail = node;
        }

        public static String printSinglyLinkedList(
                SinglyLinkedListNode node
        ) throws IOException {
            StringBuilder b = new StringBuilder("HEAD -> ");
            while (node != null) {
                b.append(node.data);
                node = node.next;
                if (node != null) {
                    b.append(" -> ");
                }
            }
            b.append(" -> NULL");
            return b.toString();
        }
    }

    static class DoublyLinkedListNode {
        public int data;
        public DoublyLinkedListNode next;
        public DoublyLinkedListNode prev;

        public DoublyLinkedListNode(int nodeData) {
            this.data = nodeData;
            this.next = null;
            this.prev = null;
        }
    }

    static class DoublyLinkedList {
        public DoublyLinkedListNode head;
        public DoublyLinkedListNode tail;

        public DoublyLinkedList() {
            this.head = null;
            this.tail = null;
        }

        public void insertNode(int nodeData) {
            DoublyLinkedListNode node = new DoublyLinkedListNode(nodeData);

            if (this.head == null) {
                this.head = node;
            } else {
                this.tail.next = node;
                node.prev = this.tail;
            }

            this.tail = node;
        }

        public static String printDoublyLinkedList(
                DoublyLinkedListNode node
        ) throws IOException {
            StringBuilder b = new StringBuilder("HEAD -> ");
            while (node != null) {
                b.append(node.data);
                node = node.next;
                if (node != null) {
                    b.append(" -> ");
                }
            }
            b.append(" -> NULL");
            return b.toString();
        }
    }

    // idea:
    //      - aktuális következő az előző, előző lesz az aktuális, aktuális pedig a kövi
    public static SinglyLinkedListNode reverse(SinglyLinkedListNode llist) {
        SinglyLinkedListNode actual = llist, prev = null, next;
        while (actual != null) {
            next = actual.next;
            actual.next = prev;
            prev = actual;
            actual = next;
        }
        return prev;
    }

    public static DoublyLinkedListNode reverse(DoublyLinkedListNode llist) {
        DoublyLinkedListNode actual = llist, prev = null, next;
        while (actual != null) {
            next = actual.next;
            actual.next = prev;
            actual.prev = next;
            prev = actual;
            actual = next;
        }
        return prev;
    }

    // idea:
    //      - kiszámoljuk hova kerülne index alapján, előző következője lesz az új, az új következője lesz az aktuális
    public static SinglyLinkedListNode insertNodeAtPosition(SinglyLinkedListNode llist, int data, int position) {
        SinglyLinkedListNode actual = llist, prev = null, node = new SinglyLinkedListNode(data);
        if(actual == null) {
            return node;
        } else {
            while(position-- > 0) {
                prev = actual;
                actual = actual.next;
            }
            prev.next = node;
            node.next = actual;
            return llist;
        }
    }

    // idea:
    //      - két cursort mozgatunk a listákon páronként összehasonlítva, a kissebbet rakjuk az új láncolásba, és ott léptetjük a cursort
    //      - előző következőjét állítgatjuk
    public static SinglyLinkedListNode mergeLists(SinglyLinkedListNode head1, SinglyLinkedListNode head2) {
        SinglyLinkedListNode head = null, prev = null;
        while(head1 != null || head2 != null) {
            if(head1 != null && (head2 == null || head1.data <= head2.data)) {
                if(head == null) head = head1;
                if(prev != null) prev.next = head1;
                prev = head1;
                head1 = head1.next;
            } else {
                if(head == null) head = head2;
                if(prev != null) prev.next = head2;
                prev = head2;
                head2 = head2.next;
            }
        }
        return head;
    }

    // idea:
    //      - legyen egy in és out stack
    //      - in-be csak pusholunk amíg push command jön
    //      - out-ba akkor pakolunk át ha delete vagy print command jön, elég ekkor átteni, addig ha push van mehet az inbe
    //      - out-on addig csináljuk deletet vagy printet amíg ki nem ürül majd áttöltjük az in-ből, sorrendhelyes marad
    private static class FIFOQueue {
        private final Stack<Integer> in = new Stack<>();
        private final Stack<Integer> out = new Stack<>();

        public void push(int number) {
            in.push(number);
        }

        public void pop() {
            if(out.isEmpty()) {
                int size = in.size();
                while(size-- > 0) {
                    out.push(in.pop());
                }
            }
            out.pop();
        }

        public int peek() {
            if(out.isEmpty()) {
                int size = in.size();
                while(size-- > 0) {
                    out.push(in.pop());
                }
            }
            return out.peek();
        }
    }

    public static void main(String[] args) {
        // expected =
        // OOO.OOO
        // OO...OO
        // OOO...O
        // ..OO.OO
        // ...OOOO
        // ...OOOO
        System.out.println(
            bomberMan(
                    3,
                    List.of(
                            ".......",
                            "...O...",
                            "....O..",
                            ".......",
                            "OO.....",
                            "OO....."
                    )
            )
        );

        System.out.println( // expected = NO
                isValid("aabbcd")
        );

        FIFOQueue queue = new FIFOQueue();
        try(Scanner scanner = new Scanner(System.in)) {
            int queries = scanner.nextInt();
            while(queries-- > 0) {
                int type = scanner.nextInt();
                if(1 == type) {
                    queue.push(scanner.nextInt());
                } else if(2 == type) {
                    queue.pop();
                } else {
                    System.out.println(queue.peek());
                }
            }
        }

        TextEditor editor = new TextEditor();
        try(Scanner s = new Scanner(System.in)) {
            int q = Integer.parseInt(s.nextLine());
            while(q-- > 0) {
                String[] opArr = s.nextLine().split(" ");
                int op = Integer.parseInt(opArr[0]);
                if(1 == op) editor.append(opArr[1]);
                else if(2 == op) editor.delete(Integer.parseInt(opArr[1]));
                else if(3 == op) editor.print(Integer.parseInt(opArr[1]));
                else editor.undo();
            }
        }
    }
}
