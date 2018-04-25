package application.root;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;

public class FxTextAreaWrapper implements TextAreaWrapper {

    private final MenuItem saveToFilePopupMenuItem = new MenuItem("Save to file");
    private final TextArea fxTextArea;

    public FxTextAreaWrapper(TextArea fxTextArea) {
        this.fxTextArea = fxTextArea;
    }

    @Override
    public void setText(String text) {
        fxTextArea.setText(text);
    }

    @Override
    public void appendText(String text) {
        fxTextArea.appendText(text);
    }

    @Override
    public void clear() {
        fxTextArea.clear();
    }

    @Override
    public Node asNode() {
        return fxTextArea;
    }

    @Override
    public void setPopupSaveToAction(Executable saveContentToFile) {
        saveToFilePopupMenuItem.setOnAction(event -> saveContentToFile.execute());

        TextAreaSkin customContextSkin = new TextAreaSkin(fxTextArea) {
            @Override
            public void populateContextMenu(ContextMenu contextMenu) {
                super.populateContextMenu(contextMenu);
                contextMenu.getItems().add(0, new SeparatorMenuItem());
                contextMenu.getItems().add(0, saveToFilePopupMenuItem);
            }
        };
        fxTextArea.setSkin(customContextSkin);
    }
}
