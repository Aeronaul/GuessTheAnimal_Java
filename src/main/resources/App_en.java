package animals.resources;

import animals.Language;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static animals.Language.AUX_VERBS;

public class App_en extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"yes-set", new String[]{
                        "y", "yes", "yeah", "yep", "sure", "right", "affirmative",
                        "correct", "indeed", "you bet", "exactly", "you said it"
                }},
                {"no-set", new String[]{
                        "n", "no", "no way", "nah", "nope", "negative",
                        "yeah no", "i dont think so"
                }},
                {"aux-verbs", Map.of(
                        "has", "doesn't have",
                        "is", "isn't",
                        "can", "can't"
                )},
                {"farewell", new String[]{
                        "Bye!", "Goodbye!", "See you soon:)"
                }},
                {"clarify-yesno", new String[]{
                        "I'm not sure I caught you: was it yes or no?",
                        "Funny, I still don't understand, is it yes or no?",
                        "Oh, it's too complicated for me: just tell me yes or no.",
                        "Could you please simply say yes or no?",
                        "Oh, no, don't try to confuse me: say yes or no."
                }},
                {"start-greet", Map.of(
                        "dawn", "Hi, Early Bird",
                        "morning", "Good morning",
                        "afternoon", "Good afternoon",
                        "evening", "Good evening",
                        "night", "Hi, Night Owl"
                )},
                {"distinguish-query", """

                Specify a fact that distinguishes %s from %s.
                The sentence should satisfy one of the following templates:
                - It can ...
                - It has ...
                - It is a/an ...

                """},
                {"distinguish-examples", """
                 The examples of a statement:
                  - It can fly
                  - It has horn
                  - It is a mammal
                 """},
                {"valid-statement-check",
                        (Predicate<String>) statement ->
                                statement.matches("it (can|has|is) .*")
                },
                {"ask",
                        (UnaryOperator<String>) statement -> {
                            String[] sArr = statement.split(" ");
                            String temp = sArr[0];
                            sArr[0] = sArr[1].toLowerCase();
                            sArr[1] = temp.toLowerCase();
                            String question = String.join(" ", sArr);
                            question = question.substring(0, 1).toUpperCase() + question.substring(1);
                            if (question.charAt(question.length() - 1) == '.') {
                                question = question.replaceFirst("\\.$", "\\?");
                            } else {
                                question += "?";
                            }
                            question = question.replaceFirst("^Has it", "Does it have");
                            return question;
                        }
                },
                {"state",
                        (BiFunction<String, Boolean, String>) (statement, modality) -> {
                            // modality is true for "is", "can", "has" and false for "isn't", "can't", "doesn't have"
                            String[] sArr = statement.split(" ");
                            sArr[1] = modality ? sArr[1] : AUX_VERBS.get(sArr[1]);
                            statement = String.join(" ", sArr);
                            if (statement.charAt(statement.length() - 1) == '?') {
                                statement = statement.replaceFirst("\\?$", "\\.");
                            } else {
                                statement += ".";
                            }
                            statement = statement.replaceFirst("\\w* ", "");
                            return statement;
                        }
                },
                {"query-handler", (UnaryOperator<String>) (input) -> {
                    String[] words = input.split(" ");
                    if (!words[0].matches("a|an|the")) return String.format("%s %s",
                            words[0].matches("[aeiou].*") ? "an" : "a",
                            input);

                    if (words[0].equals("the")) return String.format("%s %s",
                            words[1].matches("[aeiou].*") ? "an" : "a",
                            words[1]);

                    return input;
                }},
                {"remove-article", (UnaryOperator<String>) name -> {
                    String[] nArr = name.split(" ");
                    nArr = Arrays.copyOfRange(nArr, 1, nArr.length);
                    return String.join(" ", nArr);
                }},
                {"summarise", (BiConsumer<String[], Boolean>) (strings, factCheck) -> {
                    strings[0] = strings[0].split(" ")[1]; // first animal
                    strings[1] = strings[1].split(" ")[1]; // second animal
                    // strings[2] --> statement

                    System.out.println("I have learned the following facts about animals:");
                    System.out.printf(" - The %s %s\n", strings[0], Language.state(strings[2], !factCheck));
                    System.out.printf(" - The %s %s\n", strings[1], Language.state(strings[2], factCheck));
                    System.out.println("I can distinguish these animals by asking the question:");
                    System.out.printf(" - %s\n", Language.ask(strings[2]));
                    System.out.println("Nice! I've learned so much about animals!\n");
                }},
                {"check-identity", "Is it %s?"},
                {"animal-query", "I give up. What animal do you have in mind?"},
                {"animal-check", "Is the statement correct for %s?"},
                {"menu-intro", "I want to learn about animals."},
                {"menu-query", "Which animal do you like most?"},
                {"menu-welcome", "Welcome to the animal expert system!\n"},
                {"menu-options", """
                What do you want to do?
                                
                1. Play the guessing game
                2. List of all animals
                3. Search for an animal
                4. Calculate statistics
                5. Print the Knowledge Tree
                0. Exit
                """},
                {"menu-invalid", "Invalid input! Please try again."},
                {"menu-play", """
                You think of an animal, and I guess it.
                Press enter when you're ready.
                """},
                {"menu-again", "Would you like to play again?"},
                {"list-title", "Here are the animals I know:"},
                {"search-query", "Enter the animal:"},
                {"search-none", "No facts about the %s.\n"},
                {"search-facts", "Facts about the %s:\n"},
                {"search-point", " - It %s\n"},
                {"stats-format", """
                The Knowledge Tree stats

                - root node                    %s
                - total number of nodes        %s
                - total number of animals      %s
                - total number of statements   %s
                - height of the tree           %s
                - minimum animal's depth       %s
                - average animal's depth       %.1f
                \n"""}
        };
    }
}
