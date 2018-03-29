package application.persistence;

public interface ApplicationSettings extends LoadableSavable {
    GuiSettings guiSettings();

    GlobalSettings appSettings();
}
