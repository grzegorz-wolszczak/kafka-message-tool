package application.constants

import groovy.transform.CompileStatic

@CompileStatic
class GuiStrings {
    public static final String BROKER_TAB_NAME = "Brokers";
    public static final String TOPIC_CONFIGS_TAB_NAME = "Topic configs";
    public static final String SENDER_MSG_TAB_NAME = "Send msg";
    public static final String LISTENER_MSG_TAB_NAME = "Receive msg";

    public static final String GROOVY_SCRIPTING_TAB_NAME = "Execute script before sending message"
    public static final String BEFORE_FIRST_MSGS_SCRIPT_TAB = "Before FIRST message";
    public static final String BEFORE_EACH_MSGS_SCRIPT_TAB = "Before EACH message";

    public static final String MESSAGE_BODY_TEMPLATE_NAME = "Message body"
    public static final String REPEAT_COUNT_LABEL_TEXT = "Repeat count";
    public static final String SEND_BUTTON_TEXT = "Send";

    private static final String varName = "cat_age";
    public static final String MESSAGE_DEFINITION_TOOL_TIP =
            """'${MESSAGE_BODY_TEMPLATE_NAME}' tab holds the message body that will be sent to kafka broker
example :

{"animal_age": "\${${varName}}"}

The body contains variable reference '${varName}'.
You can define value for '${varName}' in '${GROOVY_SCRIPTING_TAB_NAME}' section.
Either in '${BEFORE_FIRST_MSGS_SCRIPT_TAB}' or '${BEFORE_EACH_MSGS_SCRIPT_TAB}' tab

Example:
1. Set ${REPEAT_COUNT_LABEL_TEXT} to 3
2. In '${BEFORE_FIRST_MSGS_SCRIPT_TAB}' tab write:
    cat_age = 10;
3. In '${BEFORE_EACH_MSGS_SCRIPT_TAB}' tab write
    cat_age++
4. Click '${SEND_BUTTON_TEXT}' button
5. Notice that each message has value for '${varName}' incremented.""";

    public static final String BEFORE_FIRST_MSG_TAB_TOOLTIP = """This script (in groovy) will be executed only once just before sending first message.
You can setup/define variable/classes that will be used later during sending message
e.g.
message_id = 1

You can refer to this variable in message body by typing \${message_id}.
""";
    public static final String BEFORE_EACH_MSG_TAB_TOOLTIP =
            """This script (in groovy) will be executed before sending every message.
You can set/modify variables here that were previously devined in the '${BEFORE_FIRST_MSGS_SCRIPT_TAB}' tab
e.g.
message_id++

You can refer to this variable in message body by typing \${message_id}.
""";
}
