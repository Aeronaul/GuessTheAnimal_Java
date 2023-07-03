package animals;

public class Main {

    public static void main(String[] args) {
        Node root = Tree.init(args);
        if (root == null) root = new Node();
        Menu menu = new Menu(root);
        menu.run();
        if (!root.toString().isBlank())
            Tree.write(root, args);
    }
}

