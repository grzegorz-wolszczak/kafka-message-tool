package application.scripting.codearea;

import org.fxmisc.richtext.CodeArea;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroovyCodeAreaConfigurator {

    private static final String[] GROOVY_KEYWORDS = new String[]{
        "as", "assert", "break", "case", "catch", "class", "const",
        "continue", "def", "default", "do", "else", "enum", "extends", "false",
        "finally", "for", "goto", "if", "implements", "import",
        "in", "instanceof", "interface", "new", "null", "package", "return",
        "super", "switch", "this", "throw", "throws", "trait", "true", "try", "while"
    };


    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", GROOVY_KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
        "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public static void configure(CodeArea codeArea, Executor executor) {
        CodeAreaConfigurator.configureCodeArea(codeArea,
                                               PATTERN,
                                               GroovyCodeAreaConfigurator::getStyleClass,
                                               executor);


    }


    private static String getStyleClass(Matcher matcher) {
        return matcher.group("KEYWORD") != null ? "groovy_keyword" :
            matcher.group("PAREN") != null ? "groovy_paren" :
                matcher.group("BRACE") != null ? "groovy_brace" :
                    matcher.group("BRACKET") != null ? "groovy_bracket" :
                        matcher.group("SEMICOLON") != null ? "groovy_semicolon" :
                            matcher.group("STRING") != null ? "groovy_string" :
                                matcher.group("COMMENT") != null ? "groovy_comment" :
                                    null;
    }

}
