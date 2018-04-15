package application.logging;

import application.root.Restartable;
import application.utils.RepeatableTimer;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FixedRecordsCountLogger implements Restartable {

    public static final int REPEAT_RATE_MS = 250;
    private final TextArea logTextArea;
    private CyclicStringBuffer buffer;
    private final ConcurrentLinkedQueue<String> stringBufferQueue = new ConcurrentLinkedQueue<>();
    private final RepeatableTimer appendTextTimer= new RepeatableTimer();

    public FixedRecordsCountLogger(TextArea logTextArea,
                                   CyclicStringBuffer buffer) {
        this.logTextArea = logTextArea;
        this.buffer = buffer;
    }

    public void appendText(String text)
    {
        text = normalizeNewlines(text);
        stringBufferQueue.add(text);
    }

    /*
    For unknown reason, when text with sequence '\r\n' is appended to  TextArea it will be replaced with '\n'
    so when we call TextArea.getText() it return different! text that was put in !
    In order to compare text between cyclicBuffer text and TextArea text, we make sure that
    all strings in TextArea are also without '\r'
     */
    private String normalizeNewlines(String text) {
        return text.replace("\r\n", "\n");
    }

    public void start(){
        appendTextTimer.cancel();
        appendTextTimer.startExecutingRepeatedly(this::periodicallyAppendTextToTextEdit, REPEAT_RATE_MS);
    }
    public void stop(){
        appendTextTimer.cancel();
    }

    private void periodicallyAppendTextToTextEdit(){
        final int size = stringBufferQueue.size();
        for(int i =0 ;i < size; i++)
        {
            buffer.appendText(stringBufferQueue.remove());
        }

        final String currentBufferContent = buffer.getContent();
        final String textAreaText = logTextArea.getText();
        if(!currentBufferContent.equals(textAreaText))
        {
            Platform.runLater(()->{
                logTextArea.clear();
                logTextArea.appendText(currentBufferContent);

            });
        }
    }
    public void clear(){
        Platform.runLater(logTextArea::clear);
        buffer.clear();
    }
}
