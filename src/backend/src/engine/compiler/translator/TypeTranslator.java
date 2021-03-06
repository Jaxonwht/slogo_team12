package engine.compiler.translator;

import engine.errors.CommandSyntaxException;
import engine.errors.UndefinedKeywordException;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Simple parser based on regular expressions that matches program strings to
 * kinds of language features.
 *
 * @author Robert C. Duvall
 */
public class TypeTranslator implements Translator {
    private static final String NO_MATCH = " is not defined in the syntax file.";

    // "types" and the regular expression patterns that recognize those types
    // note, it is a list because order matters (some patterns may be more generic)
    private List<Map.Entry<String, Pattern>> mySymbols;


    /**
     * Create an empty translator.
     */
    public TypeTranslator() {
        mySymbols = new ArrayList<>();
    }

    /**
     * Create a tranlator with a resource file as its starting recognized types.
     *
     * @param syntax: A resource file.
     */
    public TypeTranslator(String syntax) {
        this();
        addPatterns(syntax);
    }

    /**
     * Adds the given resource file to this language's recognized types
     */
    @Override
    public void addPatterns (String syntax) throws MissingResourceException{
        if (syntax.isEmpty() || syntax == null) {
            return;
        }
        var resources = ResourceBundle.getBundle(syntax);
        for (var key : Collections.list(resources.getKeys())) {
            var regex = resources.getString(key);
            mySymbols.add(new AbstractMap.SimpleEntry<>(key,
                    // THIS IS THE IMPORTANT LINE
                    Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        }
    }

    @Override
    public void setPatterns(String syntax) throws MissingResourceException{
        mySymbols.clear();
        addPatterns(syntax);
    }

    /**
     * Returns language's type associated with the given text if one exists
     *
     * @param text: The input raw String
     * @return The associated String symbol that the raw string represents.
     * @throws UndefinedKeywordException: When the input raw String is not defined in the resources/languages properties files, a CommandSyntaxException is thrown, containing a message about which String is not defined.
     */
    @Override
    public String getSymbol (String text) throws UndefinedKeywordException {
        for (var e : mySymbols) {
            if (match(text, e.getValue())) {
                return e.getKey();
            }
        }
        throw new UndefinedKeywordException("\"" + text + "\"" + NO_MATCH);
    }

    /**
     * This method checks whether an input command is indeed defined in the properties files.
     *
     * @param text : A String representing the input command.
     * @return whether the properties file contains that command.
     */
    @Override
    public boolean containsString(String text) {
        try {
            getSymbol(text);
            return true;
        } catch (UndefinedKeywordException e) {
            return false;
        }
    }

    /**
     * Returns true if the given text matches the given regular expression pattern
     */
    private boolean match (String text, Pattern regex) {
        // THIS IS THE IMPORTANT LINE
        return regex.matcher(text).matches();
    }
}
