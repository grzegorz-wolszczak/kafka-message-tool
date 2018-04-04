package application.utils;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.*;

public class TableUtils {

    public static void installCopyPasteHandlerForSingleCell(TableView<?> table) {

        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setOnKeyPressed(new TableKeyEventHandler());

    }

    private static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        if (positionList.size() == 0) {
            return;
        }
        if (positionList.size() > 1) {
            System.err.println("Invalid selection: should be selected only one cell, but is " + positionList.size());
            return;
        }

        TablePosition position = positionList.get(0);
        int row = position.getRow();
        int col = position.getColumn();

        Object cell = table.getColumns().get(col).getCellData(row);
        if (cell == null) {
            return;
        }

        clipboardString.append(cell.toString());
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }


    private static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCombination.match(keyEvent)) {

                if (keyEvent.getSource() instanceof TableView) {
                    copySelectionToClipboard((TableView<?>) keyEvent.getSource());
                    keyEvent.consume();

                }
            }
        }
    }
}