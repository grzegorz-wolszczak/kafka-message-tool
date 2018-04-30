package application.root;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class SwingTextAreaWrapper implements TextAreaWrapper {

    private static final int FONT_SIZE = 14;
    private static final String FONT_NAME = "monospaced";
    public static final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
    private final JTextArea textArea;
    private final SwingNode node;
    private final JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem saveToFileMenu = new JMenuItem("Save to file");;

    public SwingTextAreaWrapper(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setEditable(true);
        textArea.setFont(FONT);

        final DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        node = new SwingNode();
        JScrollPane sp = new JScrollPane(textArea);
        textArea.setComponentPopupMenu(popupMenu);
        node.setContent(sp);

        popupMenu.add(saveToFileMenu);

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
        saveToFileMenu.addActionListener(e -> Platform.runLater(saveContentToFile::execute));
    }
}
