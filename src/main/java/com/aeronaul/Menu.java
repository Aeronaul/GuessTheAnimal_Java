package animals;

import java.util.ResourceBundle;

public class Menu {
    Node root;
    ResourceBundle res;

    Menu(Node root) {
        this.root = root;
        res = Language.res;
    }

    public void run() {
        Language.startGreet();

        if (root.toString() == null) {
            System.out.println(res.getString("menu-intro"));
            root.setLabel(
                    Language.query(res.getString("menu-query"))
            );
        }

        int response;
        System.out.println(res.getString("menu-welcome"));
        do {
            System.out.print(res.getString("menu-options"));
            response = Language.queryMenuResponse();
            switch (response) {
                case 1 -> play();
                case 2 -> list();
                case 3 -> search();
                case 4 -> stats();
                case 5 -> printTree();
                case -1 -> System.out.println(res.getString("menu-invalid"));
            }
        } while (response != 0);

        Language.endGreet();
    }

    private void play() {
        boolean play;
        do {
            System.out.print(res.getString("menu-play"));
            Language.skipLine();
            root.insert();
            play = Language.confirm(res.getString("menu-again"));
        } while (play);
    }

    private void list() {
        String[] names = Tree.retrieveList(root);
        System.out.println(res.getString("list-title"));
        for (String name : names) {
            System.out.printf(" - %s\n", name);
        }
    }

    private void search() {
        String animal = Language.removeArticle(Language.query(res.getString("search-query")));
        String[] facts = Tree.getFacts(root, animal);
        if (facts.length == 0) {
            System.out.printf(res.getString("search-none"), animal);
        } else {
            System.out.printf(res.getString("search-facts"), animal);
            for (String fact : facts) {
                System.out.printf(res.getString("search-point"), fact);
            }
        }
    }

    private void stats() {
        Object[] numbers = Tree.stats(root);
        System.out.printf(res.getString("stats-format"),
                numbers[0], numbers[1], numbers[2],
                numbers[3], numbers[4], numbers[5], numbers[6]);
    }

    private void printTree() {
        Tree.print(root, " └", "  ");
    }
}
