package application.root;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class SwingTextAreaWrapper implements TextAreaWrapper {

    private static final int FONT_SIZE = 14;
    private static final String FONT_NAME = "Courier New";
    public static final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
    private final JTextArea textArea;
    private final SwingNode node;
    private final JPopupMenu popupMenu = new JPopupMenu();

    public SwingTextAreaWrapper(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setEditable(false);
        textArea.setFont(FONT);

        final DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        node = new SwingNode();
        JScrollPane sp = new JScrollPane(textArea);
        textArea.setComponentPopupMenu(popupMenu);
        node.setContent(sp);
    }

    @Override
    public void setText(String localBuffer) {
        textArea.setText(localBuffer);
    }

    @Override
    public void appendText(String s) {
        textArea.append(s);
    }

    @Override
    public void clear() {
        textArea.setText("");
    }

    @Override
    public Node asNode() {
        return node;
    }

    @Override
    public void setPopupSaveToAction(Executable saveContentToFile) {
        final JMenuItem saveToFileMenu = new JMenuItem("Save to file");
        saveToFileMenu.addActionListener(e -> Platform.runLater(() -> {
            saveContentToFile.execute();
        }));
        popupMenu.add(saveToFileMenu);
    }
}
