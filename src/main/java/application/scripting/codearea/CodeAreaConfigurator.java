package application.scripting.codearea;

import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeAreaConfigurator {

    private static final int WAIT_PERIOD_TO_START_HIGHLIGHTING_AFTER_LAST_KEY_PRESSED = 500;

    public static void configureCodeArea(CodeArea codeArea,
                                         Pattern pattern,
                                         Function<Matcher, String> getStyleClassFunction,
                                         Executor executor
    ) {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges()
            .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX

            .successionEnds(Duration.ofMillis(WAIT_PERIOD_TO_START_HIGHLIGHTING_AFTER_LAST_KEY_PRESSED))
            .supplyTask(() -> CodeAreaConfigurator.computeHighlightingAsync(codeArea,
                                                                            pattern,
                                                                            getStyleClassFunction,
                                                                            executor))
            .awaitLatest(codeArea.richChanges())
            .filterMap(t -> {
                if (t.isSuccess()) {
                    return Optional.of(t.get());
                } else {
                    t.getFailure().printStackTrace();
                    return Optional.empty();
                }
            })

            .subscribe(highlighting -> codeArea.setStyleSpans(0, highlighting));

    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text,
                                                                      Pattern pattern,
                                                                      Function<Matcher, String> getStyleClassFunction) {
        Matcher matcher;
        try {
            matcher = pattern.matcher(text);
        } catch (Exception e) {
            return null;
        }

        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            final String styleClass = getStyleClassFunction.apply(matcher);
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static Task<StyleSpans<Collection<String>>> computeHighlightingAsync(CodeArea codeArea,
                                                                                 Pattern pattern,
                                                                                 Function<Matcher, String> getStyleClassFunction,
                                                                                 Executor executor) {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text, pattern, getStyleClassFunction);
            }
        };
        executor.execute(task);
        return task;
    }

}
