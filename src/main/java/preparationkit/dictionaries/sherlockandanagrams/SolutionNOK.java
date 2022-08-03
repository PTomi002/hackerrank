package preparationkit.dictionaries.sherlockandanagrams;

public class SolutionNOK {

    // idea:
    //      - töröljük ki az egyik feléből az összes olyan karaktert, amit tartalmaz a másik -> azok maradnak bent amiket cserélni kell hogy anagramma legyen
    public static int anagram(String s) {
        if (s.length() % 2 != 0) return -1;

        int mid = s.length() / 2;
        StringBuilder fHalf = new StringBuilder(s.substring(0, mid));
        StringBuilder sHalf = new StringBuilder(s.substring(mid));

        for (int i = 0; i < fHalf.length(); i++) {
            int idx = sHalf.indexOf(Character.toString(fHalf.charAt(i)));
            if (idx != -1) sHalf.deleteCharAt(idx);
        }

        return sHalf.length();
    }



    public static void main(String[] args) {
        System.out.println( // expected = 1
                anagram("xaxbbbxx")
        );
        System.out.println( // expected = 5
                anagram("fdhlvosfpafhalll")
        );
    }
}
