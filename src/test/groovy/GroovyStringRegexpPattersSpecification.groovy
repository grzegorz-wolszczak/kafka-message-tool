import automation.AssertHelpers
import org.testng.annotations.Test

import java.util.regex.Pattern

import static RegexSyntaxHighlightingPatterns.*;


class GroovyStringRegexpPattersSpecification {

    def TRIPLE_QUOTATION_INPUTS = [
            '''""""""''',
            '''"""abc"""''',
            '''""""abc"""''',
            '''"""\'abc"""''',
            '''"""\''abc'\'"""''',
            '''"""\"abc"""''',
            //'''"""\"abc\""""''', // TODO: make string this working,
            '''"""\'"""''',
    ]

    def TRIPLE_APOSTROPHE_INPUTS = [
            """''''''""",
            """'''abc'''""",
            """''''abc'''""",
            """'''\"abc'''""",
            """'''\"abc\"'''""",
            """'''\'abc'''""",
            """'''\'abc\' '''""",
            //"""'''\'abc\''''""", < -- invalid pattern
            """'''\"'''""",
    ]

    def SINGLE_QUOTATION_INPUTS = [
            '""',
            '''"abc"''',
            '''"\\\"abc"''',
            '''"\\\"abc\\\""''',
            '''"'\'abc'\'"''',
            '''"'\\'abc'\\'"''',
            '''"'\\\'abc'\\\'"''',
    ]

    def SINGLE_APOSTROPHE_INPUTS = []
    def REGEX_SLASHY_STRINGS = [
            "/abc/"
    ]


    @Test
    void shouldFindGstringPatternWithTripleQuotationMarkAsBoundary() {

        // WHEN/THEN
        TRIPLE_QUOTATION_INPUTS.forEach {
            AssertHelpers.assertMatchesSingleOccurrence(
                    TRIPLE_QUOTATION_PATTERN, it, TRIPLE_QUOTATION_GROUP_NAME)
        }
        TRIPLE_QUOTATION_INPUTS.forEach {
            AssertHelpers.assertMatchesDoubleOccurrences(
                    TRIPLE_QUOTATION_PATTERN, it, TRIPLE_QUOTATION_GROUP_NAME)
        }
    }


    @Test
    void shouldFindGstringPatternWithTripleApostrophesAsBoundary() {
        // GIVEN

        // WHEN/THEN
        TRIPLE_APOSTROPHE_INPUTS.forEach {
            AssertHelpers.assertMatchesSingleOccurrence(
                    TRIPLE_APOSTROPHE_PATTERN, it, TRIPLE_APOSTROPHE_GROUP_NAME)
        }
        TRIPLE_APOSTROPHE_INPUTS.forEach {
            AssertHelpers.assertMatchesDoubleOccurrences(
                    TRIPLE_APOSTROPHE_PATTERN, it, TRIPLE_APOSTROPHE_GROUP_NAME)
        }
    }

    @Test
    void shouldFindGstringPatternWithSingleQuotationMarkAsBoundary() {
        def groupName= "singleQuoteGroup"
        def pattern = Pattern.compile("(?<${groupName}>\"([^\"\\\\]|\\\\.)*\")");


        // WHEN/THEN
        SINGLE_QUOTATION_INPUTS.forEach {
            AssertHelpers.assertMatchesSingleOccurrence(
                    pattern, it, groupName)
        }
        SINGLE_QUOTATION_INPUTS.forEach {
            AssertHelpers.assertMatchesDoubleOccurrences(
                    pattern, it, groupName)
        }
    }

    @Test
    void shouldFindGstringPatternWithSingleApostropheAsBoundary() {
        def groupName= "singleApostropheGroup"
        def pattern = Pattern.compile("(?<${groupName}>\'([^\'\\\\]|\\\\.)*\')");
        // GIVEN
        def inputs = [
                """''""",
                """'abc'"""
                //'''"abc"''',
                //'''"\\\"abc"''',
                //'''"\\\"abc\\\""''',
                //'''"'\'abc'\'"''',
                //'''"'\\'abc'\\'"''',
                //'''"'\\\'abc'\\\'"''',
        ]

        // WHEN/THEN
        inputs.forEach {
            AssertHelpers.assertMatchesSingleOccurrence(
                    pattern, it, groupName)
        }
        inputs.forEach {
            AssertHelpers.assertMatchesDoubleOccurrences(
                    pattern, it, groupName)
        }
    }



}

