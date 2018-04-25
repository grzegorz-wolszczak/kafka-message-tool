package application.utils;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class TableUtils {

    public static final double NEW_MAX_WIDTH_EXTRA_SPACE = 15.0d;
    private static Method columnToFitMethod;

    static {
        try {
            columnToFitMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
            columnToFitMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void customResize(TableView<?> view) {

        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth() + ((tableWidth - width.get()) / view.getColumns().size()));
            });
        }
    }
//    public static void autoFitTable(TableView tableView) {
//        System.out.println("Installing listener");
//        tableView.getItems().addListener(new ListChangeListener<Object>() {
//            @Override
//            public void onChanged(Change<?> c) {
//                for (Object column : tableView.getColumns()) {
//                    try {
//                        System.out.println("Column to fit (action): " + columnToFitMethod);
//                        final Skin<?> skin = tableView.getSkin();
//                        System.out.println("Skin : " + skin);
//                        if(skin==null)
//                        {
//                            System.out.println("Skin is null");
//                            continue;
//                        }
//                        columnToFitMethod.invoke(skin, column, -1);
//                    } catch (IllegalAccessException | InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    public static void autoResizeColumns(TableView<?> table) {
        //Set the right policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach((column) -> {
            //Minimal width = columnheader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                //cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            column.setPrefWidth(max + NEW_MAX_WIDTH_EXTRA_SPACE);
        });
    }

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