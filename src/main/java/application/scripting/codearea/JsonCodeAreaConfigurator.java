package application.scripting.codearea;

import org.fxmisc.richtext.CodeArea;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonCodeAreaConfigurator {

    private static final String BRACE_PATTERN = "(\\{|\\})";
    private static final String BRACKET_PATTERN = "(\\[|\\])";

    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String NUMBER_PATTERN = "([0-9.]+)";
    private static final String BOOLEAN_PATTERN = "(?i:true|false)";
    private static final String NULL_PATTERN = "(?i:null)";

    private static final Pattern PATTERN = Pattern.compile(
        "(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
            + "|(?<NULL>" + NULL_PATTERN + ")"
            + "|(?<BOOLEAN>" + BOOLEAN_PATTERN + ")"
    );


    public static void configure(CodeArea codeArea, Executor executor) {
        CodeAreaConfigurator.configureCodeArea(codeArea,
                                               PATTERN,
                                               JsonCodeAreaConfigurator::getStyleClass,
                                               executor);
    }


    private static String getStyleClass(Matcher matcher) {
        return matcher.group("BRACE") != null ? "json_brace"
            : matcher.group("BRACKET") != null ? "json_bracket"
            : matcher.group("STRING") != null ? "json_string"
            : matcher.group("NUMBER") != null ? "json_number"
            : matcher.group("NULL") != null ? "json_null"
            : matcher.group("BOOLEAN") != null ? "json_boolean"
            : null;
    }
}
