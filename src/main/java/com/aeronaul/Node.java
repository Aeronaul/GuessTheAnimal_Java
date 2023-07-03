package animals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    @JsonProperty("noNode")
    Node noNode;

    @JsonProperty("yesNode")
    Node yesNode;

    @JsonIgnore
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("label")
    private String label;

    @JsonCreator
    public Node() {
    }

    Node(String label) {
        this.label = label;
    }

    @JsonIgnore
    boolean ask() {
        if (isLeaf())
            return Language.confirm(Language.res.getString("check-identity").formatted(label));
        else
            return Language.confirm(Language.ask(label));
    }

    @JsonIgnore
    void insert() {
        boolean fact = ask();
        if (isLeaf()) {
            if (fact) return;
            String animal = Language.query(Language.res.getString("animal-query"));
            String diff = Language.distinguish(label, animal);
            boolean isTrueForSecondNode = Language.confirm(Language.res.getString("animal-check").formatted(animal));
            Node yesNode = new Node(isTrueForSecondNode ? animal : label);
            Node noNode = new Node(isTrueForSecondNode ? label : animal);
            Language.summarise(label, animal, diff, isTrueForSecondNode);
            this.label = diff;
            this.noNode = noNode;
            this.yesNode = yesNode;
        } else {
            if (fact) yesNode.insert();
            else noNode.insert();
        }
    }

    @JsonIgnore
    boolean isLeaf() {
        return noNode == null && yesNode == null;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return label;
    }
}
