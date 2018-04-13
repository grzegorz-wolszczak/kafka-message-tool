package application.customfxwidgets.listenerconfig;

import application.utils.UserInteractor;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.function.Supplier;

public class ToFileSaver {

    public static final String CHARSET_UTF8 = "UTF-8";
    private Supplier<String> textSupplier;
    private UserInteractor interactor;

    public ToFileSaver(UserInteractor interactor) {
        this.interactor = interactor;
    }

    public void setContentSupplier(Supplier<String> supplier) {

        textSupplier = supplier;
    }

    public void saveContentToFile() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        final File file = fileChooser.showSaveDialog(interactor.getOwnerWindow());

        if (file != null) {
            saveFile(textSupplier.get(), file);
        }
    }

    private void saveFile(String content, File file) {
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CHARSET_UTF8))) {
            fileWriter.write(content);
            interactor.showInfo("Saving to file...", String.format("File '%s' saved successfully",
                                                                   file.getName()));
        } catch (IOException ex) {
            interactor.showError("Saving to file...", String.format("Saving file '%s' failed",
                                                                 file.getName()));
            ex.printStackTrace();
        }
    }
}
