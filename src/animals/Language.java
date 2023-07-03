package animals;

import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Language {
    public static final Map<String, String> AUX_VERBS;
    public static final ResourceBundle res;
    private static final Set<String> YES_SET = new HashSet<>();
    private static final Set<String> NO_SET = new HashSet<>();
    private static final String[] FAREWELL;
    private static final String[] CLARIFY_YESNO;
    private static final Scanner in = new Scanner(System.in);
    private static final Random random = new Random();

    static {
        switch (System.getProperty("user.language", "en")) {
            case "eo":
                res = ResourceBundle.getBundle("animals.resources.App_eo");
                break;
            default:
                res = ResourceBundle.getBundle("animals.resources.App_en");
        }

        AUX_VERBS = (Map<String, String>) res.getObject("aux-verbs");
        YES_SET.addAll(Arrays.asList(res.getStringArray("yes-set")));
        NO_SET.addAll(Arrays.asList(res.getStringArray("no-set")));
        CLARIFY_YESNO = res.getStringArray("clarify-yesno");
        FAREWELL = res.getStringArray("farewell");
    }

    static void summarise(String firstAnimal, String secondAnimal, String statement, boolean factCheck) {
        String[] strings = {firstAnimal, secondAnimal, statement};
        var biconsumer = (BiConsumer<String[], Boolean>) res.getObject("summarise");
        biconsumer.accept(strings, factCheck);
    }

    public static String distinguish(String firstAnimal, String secondAnimal) {
        System.out.printf(
                res.getString("distinguish-query"),
                firstAnimal, secondAnimal
        );

        String statement = in.nextLine().toLowerCase();
        if (isValidStatement(statement)) return statement;

        System.out.println(res.getString("distinguish-examples"));

        return distinguish(firstAnimal, secondAnimal);
    }

    public static String ask(String statement) {
        var uniOp = (UnaryOperator<String>) res.getObject("ask");
        return uniOp.apply(statement);
    }

    public static String state(String statement, boolean modality) {
        var biFunc = (BiFunction<String, Boolean, String>) res.getObject("state");
        return biFunc.apply(statement, modality);
    }

    private static boolean isValidStatement(String statement) {
        var pred = (Predicate<String>) res.getObject("valid-statement-check");
        return pred.test(statement);
    }

    public static void startGreet() {
        LocalTime now = LocalTime.now();
        Map<String, String> greetingsMap = (Map<String, String>) res.getObject("start-greet");
        String greeting = "";
        if (now.isBefore(LocalTime.of(5, 0))) {
            greeting = greetingsMap.get("dawn");
        } else if (now.isBefore(LocalTime.of(12, 0))) {
            greeting = greetingsMap.get("morning");
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            greeting = greetingsMap.get("afternoon");
        } else if (now.isBefore(LocalTime.of(21, 0))) {
            greeting = greetingsMap.get("evening");
        } else if (now.compareTo(LocalTime.of(23, 59)) <= 0) {
            greeting = greetingsMap.get("night");
        }

        System.out.printf("%s\n\n", greeting);
    }

    public static String query(String question) {
        System.out.println(question);
        String input = in.nextLine().toLowerCase();
        var biFunc = (UnaryOperator<String>) res.getObject("query-handler");
        return biFunc.apply(input);
    }

    public static boolean confirm(String question) {
        System.out.println(question);
        String answer;
        String answer_nop;
        while (true) {
            answer = in.nextLine().trim().toLowerCase();
            answer_nop = answer.replaceAll("\\p{Punct}", "");
            if (answer.matches(".*\\p{Punct}{2,}")) {
                clarify();
            } else if (NO_SET.contains(answer_nop)) {
                return false;
            } else if (!answer.equals(answer_nop) && answer.length() - answer_nop.length() > 1) {
                clarify();
            } else if (YES_SET.contains(answer_nop)) {
                return true;
            } else {
                clarify();
            }
        }
    }

    private static void clarify() {
        int index = random.nextInt(CLARIFY_YESNO.length);
        System.out.println(CLARIFY_YESNO[index]);
    }

    public static void skipLine() {
        in.nextLine();
    }

    public static void endGreet() {
        System.out.printf("\n%s\n", FAREWELL[random.nextInt(FAREWELL.length)]);
    }

    public static int queryMenuResponse() {
        String i = in.nextLine();
        if (i.matches("[0-5]"))
            return Integer.parseInt(i);
        else return -1;
    }

    public static String removeArticle(String name) {
        var unaryOp = (UnaryOperator<String>) res.getObject("remove-article");
        return unaryOp.apply(name);
    }
}
