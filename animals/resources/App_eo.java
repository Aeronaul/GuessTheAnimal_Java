package animals.resources;

import animals.Language;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static animals.Language.AUX_VERBS;

public class App_eo extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"yes-set", new String[]{
                        "y", "j", "jes", "certe", "ĝuste",
                        "jese", "ĝusta", "ja",
                }},
                {"no-set", new String[]{
                        "n", "neniel", "ne", "negativo",
                        "jes ne", "mi ne pensas tiel"
                }},
                {"aux-verbs", Map.of(
                        "havas", "ne havas",
                        "estas", "ne estas",
                        "povas", "ne povas",
                        "loĝas", "ne loĝas"
                )},
                {"farewell", new String[]{
                        "Ĝis revido!"
                }},
                {"clarify-yesno", new String[]{
                        "Mi ne certas, ke mi komprenis vin: ĉu jes aŭ ne?",
                        "Amuza, mi ankoraŭ ne komprenas, ĉu jes aŭ ne?",
                        "Ho, ĝi estas tro komplika por mi: nur diru al mi jes aŭ ne.",
                        "Ĉu vi bonvolus simple diri jes aŭ ne?",
                        "Ho, ne, ne provu konfuzi min: diru jes aŭ ne."
                }},
                {"start-greet", Map.of(
                        "dawn", "Bonan matenon",
                        "morning", "Bonan matenon",
                        "afternoon", "Bonan posttagmezon",
                        "evening", "Bonan vesperon",
                        "night", "Bonan vesperon"
                )},
                {"distinguish-query", """

                Indiku fakton, kiu distingas %s de %s.
                 La frazo devas kontentigi unu el la jenaj ŝablonoj:
                 - Ĝi povas ...
                 - Ĝi havas ...
                 - Ĝi estas/an...

                """},
                {"distinguish-examples", """
                 Ekzemploj de deklaro:
                - Ĝi povas flugi
                - Ĝi havas kornon
                - Ĝi estas mamulo
                 """},
                {"valid-statement-check",
                        (Predicate<String>) statement ->
                                statement.matches("ĝi (povas|havas|estas|loĝas) .*")
                },
                {"ask",
                        (UnaryOperator<String>) statement -> {
                            String[] sArr = statement.split(" ");
                            sArr[0] = "Ĉu " + sArr[0].toLowerCase();
                            String question = String.join(" ", sArr);
                            if (question.charAt(question.length() - 1) == '.') {
                                question = question.replaceFirst("\\.$", "\\?");
                            } else {
                                question += "?";
                            }
                            return question;
                        }
                },
                {"state",
                        (BiFunction<String, Boolean, String>) (statement, modality) -> {
                            // modality is true for "is", "can", "has" and false for "isn't", "can't", "doesn't have"
                            List<String> sArr = new LinkedList<>(Arrays.asList(statement.split(" ")));
                            sArr.remove(0);
                            sArr.set(0, modality ? sArr.get(0) : AUX_VERBS.get(sArr.get(0)));
                            statement = String.join(" ", sArr);
                            if (statement.charAt(statement.length() - 1) == '?') {
                                statement = statement.replaceFirst("\\?$", "\\.");
                            } else {
                                statement += ".";
                            }
                            return statement;
                        }
                },
                {"query-handler", (UnaryOperator<String>) (input) -> {
                    String[] words = input.split(" ");
                    if (words[0].equals("la")) return input.replaceFirst("\\w* ", "");
                    else return input;
                }},
                {"remove-article", (UnaryOperator<String>) name -> name},
                {"summarise", (BiConsumer<String[], Boolean>) (strings, factCheck) -> {
                    // strings[0] --> first animal
                    // strings[1] --> second animal
                    // strings[2] --> statement

                    System.out.println("Mi lernis la jenajn faktojn pri bestoj:");
                    System.out.printf(" - La %s %s\n", strings[0], Language.state(strings[2], !factCheck));
                    System.out.printf(" - La %s %s\n", strings[1], Language.state(strings[2], factCheck));
                    System.out.println("Mi povas distingi ĉi tiujn bestojn farante la demandon:");
                    System.out.printf(" - %s\n", Language.ask(strings[2]));
                    System.out.println("Bela! Mi lernis multe pri bestoj!\n");
                }},
                {"check-identity", "ĉu ĝi estas %s?"},
                {"animal-query", "Mi rezignas. Kiun beston vi havas en la kapo?"},
                {"animal-check", "Ĉu la aserto ĝustas por la %s?"},
                {"menu-intro", "Mi volas lerni pri bestoj."},
                {"menu-query", "Kiun beston vi plej ŝatas?"},
                {"menu-welcome", "Bonvenon al la sperta sistemo de la besto!\n"},
                {"menu-options", """
                Kion vi volas fari?

                 1. Ludi la divenludon
                 2. Listo de ĉiuj bestoj
                 3. Serĉi beston
                 4. Kalkuli statistikon
                 5. Presu la Scion-Arbon
                 0. Eliri
                """},
                {"menu-invalid", "Nevalida enigo! Bonvolu reprovi."},
                {"menu-play", """
                Vi pensu pri besto, kaj mi divenos ĝin.
                Premu enen kiam vi pretas.
                """},
                {"menu-again", "Ĉu vi ŝatus ludi denove?"},
                {"list-title", "Jen la bestoj, kiujn mi konas:"},
                {"search-query", "Enigu la beston:"},
                {"search-none", "Neniuj faktoj pri la %s.\n"},
                {"search-facts", "Faktoj pri la %s:\n"},
                {"search-point", " - Ĝi %s\n"},
                {"stats-format", """
                La statistiko de la Scio-Arbo
                
                - radika nodo %s
                - tuta nombro de nodoj %s
                - tuta nombro de bestoj %s
                - totala nombro de deklaroj %s
                - alteco de la arbo %s
                - minimuma profundo de besto %s
                - averaĝa profundo %.1f
                \n"""}
        };
    }
}
