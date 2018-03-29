package application.persistence;

public class DefaultApplicationSettings implements ApplicationSettings {


    private final XmlFileConfig xmlConfig;
    private final GlobalSettings globalSettings;
    private GuiSettings guiSettings;

    public DefaultApplicationSettings(XmlFileConfig xmlConfig) {

        this.xmlConfig = xmlConfig;
        this.globalSettings = xmlConfig.getGlobalSettings();
        this.guiSettings = xmlConfig.getGuiSettings();

    }

    @Override
    public GuiSettings guiSettings() {
        return guiSettings;
    }

    @Override
    public void load() {
        xmlConfig.load();
    }


    @Override
    public void save() {
        xmlConfig.save();
    }

    @Override
    public GlobalSettings appSettings() {
        return globalSettings;
    }
}
