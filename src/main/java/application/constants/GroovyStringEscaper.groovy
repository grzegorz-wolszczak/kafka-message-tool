package application.constants

import groovy.transform.CompileStatic

@CompileStatic
class GroovyStringEscaper {
    static String escape(String varName, String content)
    {
        content = content.replace("\\", "\\\\");
        content = content.replace("\"", "\\\"");
        return """${varName}=\"\"\"${content}\"\"\""""
    }
}
