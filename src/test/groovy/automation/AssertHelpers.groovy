package automation

import groovy.transform.CompileStatic

import java.util.regex.Pattern

@CompileStatic
class AssertHelpers {
    static def assertMatchesSingleOccurrence(Pattern pattern, textToBeMatched, String groupName) {

        // WHEN
        def text = """...before... ${textToBeMatched} ...after..."""
        def matcher = pattern.matcher(text);

        //println "text '${text}', to be found ${textToBeMatched}, pattern ${pattern}"

        assert matcher.find()
        assert matcher.group(groupName) == textToBeMatched
    }

    static def assertMatchesDoubleOccurrences(Pattern pattern, textToBeMatched, String groupName) {

        // WHEN
        def text = """...before... ${textToBeMatched} ...midle... ${textToBeMatched} ...after..."""
        def matcher = pattern.matcher(text);

        //println "text '${text}', to be found ${textToBeMatched}, pattern ${pattern}"

        assert matcher.find()
        assert matcher.group(groupName) == textToBeMatched
    }
}
