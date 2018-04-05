package application.kafka.listener;

public class SynchronizedStringBuffer {
    private StringBuilder builder = new StringBuilder();

    synchronized private String getOrAppendContentSync(String input, Mode mode) {
        if (mode == Mode.APPEND) {
            builder.append(input);
            return "";
        }
        final String content = builder.toString();
        builder = new StringBuilder();
        return content;

    }

    public String getContent() {
        return getOrAppendContentSync(null, Mode.GET_AND_CLEAR);
    }

    public void appendContent(String content)
    {
        getOrAppendContentSync(content, Mode.APPEND);
    }

    private static enum Mode {
        GET_AND_CLEAR,
        APPEND
    }

}
