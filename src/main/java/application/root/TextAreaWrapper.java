package application.root;

import javafx.scene.Node;

public interface TextAreaWrapper {
    void setText(String localBuffer);

    void appendText(String s);

    void clear();

    Node asNode();

    void setPopupSaveToAction(Executable saveContentToFile);
}
