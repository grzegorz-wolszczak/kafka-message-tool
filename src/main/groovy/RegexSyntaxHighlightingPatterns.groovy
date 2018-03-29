import groovy.transform.CompileStatic

import java.util.regex.Pattern

@CompileStatic
class RegexSyntaxHighlightingPatterns {
    public static final String TRIPLE_APOSTROPHE_GROUP_NAME = "TRIPLEAPOSTROPHE";
    public static final Pattern TRIPLE_APOSTROPHE_PATTERN =
            Pattern.compile("""(?<${TRIPLE_APOSTROPHE_GROUP_NAME}>('{3}.*?'{3}))""");

    public static final String TRIPLE_QUOTATION_GROUP_NAME = "TRIPLEQUOT"
    public static final Pattern TRIPLE_QUOTATION_PATTERN =
            Pattern.compile("""(?<${TRIPLE_QUOTATION_GROUP_NAME}>("{3}.*?"{3}))""");


}


