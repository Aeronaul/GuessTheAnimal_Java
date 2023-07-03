package animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Tree {
    public static void write(Node root, String[] args) {
        String type = args.length == 2 ? args[1] : "json";
        StringBuilder fileName = new StringBuilder("animals");
        String lang = System.getProperty("user.language", "en");
        if (!lang.equals("en")) {
            fileName.append("_");
            fileName.append(System.getProperty("user.language"));
        }
        ObjectMapper objectMapper;
        switch (type) {
            case "xml" -> {
                objectMapper = new XmlMapper();
                fileName.append(".xml");
            }
            case "yaml" -> {
                objectMapper = new YAMLMapper();
                fileName.append(".yaml");
            }
            default -> {
                objectMapper = new JsonMapper();
                fileName.append(".json");
            }
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    new File(fileName.toString()), root
            );
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

    public static Node init(String[] args) {
        if (args.length < 2 || !args[0].equals("-type")) return null;

        String type = args[1].toLowerCase();
        StringBuilder fileName = new StringBuilder("animals");
        String lang = System.getProperty("user.language", "en");
        if (!lang.equals("en")) {
            fileName.append("_");
            fileName.append(System.getProperty("user.language"));
        }
        ObjectMapper objectMapper;
        switch (type) {
            case "xml" -> {
                objectMapper = new XmlMapper();
                fileName.append(".xml");
            }
            case "yaml" -> {
                objectMapper = new YAMLMapper();
                fileName.append(".yaml");
            }
            default -> {
                objectMapper = new JsonMapper();
                fileName.append(".json");
            }
        }

        try {
            File file = new File(fileName.toString());
//            System.out.printf("Reading from: %s\n", file.getAbsolutePath());
            return objectMapper.readValue(
                    file, Node.class
            );
        } catch (IOException io) {
            return null;
        }
    }

    public static String[] retrieveList(Node root) {
        List<String> names = new ArrayList<>();
        listHelper(root, names);
        return names.stream()
                .sorted()
                .toArray(String[]::new);
    }

    private static void listHelper(Node node, List<String> names) {
        if (node.isLeaf()) names.add(Language.removeArticle(node.toString()));
        else {
            listHelper(node.noNode, names);
            listHelper(node.yesNode, names);
        }
    }

    public static String[] getFacts(Node root, String name) {
        Deque<String> facts = new ArrayDeque<>();
        gFHelper(root, name, facts);
        return facts.toArray(String[]::new);
    }

    private static boolean gFHelper(Node node, String name, Deque<String> facts) {
        if (node.isLeaf()) {
            return Language.removeArticle(node.toString()).equals(name);
        }

        facts.addLast(Language.state(node.toString(), false));
        if (gFHelper(node.noNode, name, facts)) {
            return true;
        }
        facts.removeLast();

        facts.addLast(Language.state(node.toString(), true));
        if (gFHelper(node.yesNode, name, facts)) {
            return true;
        }
        facts.removeLast();

        return false;
    }

    public static Object[] stats(Node root) {
        Object[] info = new Object[7];
        info[0] = root.toString();

        int[] nodeInfo = new int[5];
        List<Integer> depth = new ArrayList<>();
        nodeStats(root, nodeInfo, depth);

        info[1] = nodeInfo[0];
        info[2] = nodeInfo[1];
        info[3] = nodeInfo[2];
        info[4] = nodeInfo[4];

        IntSummaryStatistics summaryStatistics = depth.stream().mapToInt(Integer::intValue).summaryStatistics();
        info[5] = summaryStatistics.getMin();
        info[6] = summaryStatistics.getAverage();

        return info;
    }

    private static void nodeStats(Node node, int[] nodeInfo, List<Integer> depth) {
        nodeInfo[0] += 1;
        if (node.isLeaf()) {
            nodeInfo[1] += 1;
            depth.add(nodeInfo[3]);
            return;
        } else nodeInfo[2] += 1;

        nodeInfo[3] += 1;
        nodeInfo[4] = Math.max(nodeInfo[4], nodeInfo[3]);
        nodeStats(node.noNode, nodeInfo, depth);
        nodeStats(node.yesNode, nodeInfo, depth);
        nodeInfo[3] -= 1;
    }

    public static void print(Node node, String prefix, String nextPrefix) {
        if (node.isLeaf()) {
            System.out.printf("%s %s\n", prefix, node);
        } else {
            System.out.printf("%s %s\n", prefix, Language.ask(node.toString()));
            print(node.yesNode, nextPrefix + "├", nextPrefix + "│");
            print(node.noNode, nextPrefix + "└", nextPrefix + " ");
        }
    }
}
