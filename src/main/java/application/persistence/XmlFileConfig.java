package application.persistence;

import application.constants.ApplicationConstants;
import application.logging.Logger;
import application.model.FromPojoConverter;
import application.model.ModelDataProxy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class XmlFileConfig implements LoadableSavable {

    private final String configFilePath = Paths.get(ApplicationConstants.CONFIG_FILE_NAME).toAbsolutePath().toString();
    private final ModelDataProxy modelDataProxy;
    private final FromPojoConverter converter;

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    private GuiSettings guiSettings;
    private GlobalSettings globalSettings;

    public XmlFileConfig(ModelDataProxy modelDataProxy,
                         FromPojoConverter converter,
                         GuiSettings guiSettings,
                         GlobalSettings globalSettings) {
        this.modelDataProxy = modelDataProxy;
        this.converter = converter;
        this.guiSettings = guiSettings;
        this.globalSettings = globalSettings;
    }


    @Override
    public void save() {

        final XmlConfig xmlConfig = new XmlConfig();
        xmlConfig.setBrokerConfigs(modelDataProxy.brokerConfigPojos());
        xmlConfig.setTopicConfigs(modelDataProxy.topicConfigPojos());
        xmlConfig.setListenerConfigs(modelDataProxy.listenerConfigPojos());
        xmlConfig.setMessagesConfigs(modelDataProxy.messagesConfigPojos());
        xmlConfig.setGuiSettings(guiSettings);
        xmlConfig.setGlobalSettings(globalSettings);

        Logger.debug(String.format("Saving config to file '%s'", configFilePath));
        try {
            saveConfigFile(xmlConfig);
        } catch (Exception e) {
            Logger.error("Could not save  file.", e);
        }
    }

    @Override
    public void load() {
        readFromConfigFile();
    }

    private void saveConfigFile(XmlConfig xmlConfig) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(XmlConfig.class);
        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(xmlConfig, new File(configFilePath));
        Logger.debug("OK: Config file saved.");
    }


    private void readFromConfigFile() {
        Logger.debug(String.format("Reading config createFrom file '%s'", configFilePath));

        try {
            loadConfigFile();

        } catch (UnmarshalException e) {
            final Throwable cause = e.getLinkedException();
            if (cause instanceof FileNotFoundException) {
                Logger.warn("Config file not found.");
            } else {
                Logger.error("Could not loadOnAnchorPane config file", e);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
            Logger.error("Could not loadOnAnchorPane configuration. Exception happened: \n", e);
        }
    }

    private void loadConfigFile() throws JAXBException {
        final File file = new File(configFilePath);
        final JAXBContext jaxbContext = JAXBContext.newInstance(XmlConfig.class);
        final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        final XmlConfig xmlConfig = (XmlConfig) jaxbUnmarshaller.unmarshal(file);

        loadConfigs(xmlConfig);
        loadGuiSettings(xmlConfig);
        loadGlobalSettings(xmlConfig);
    }

    private void loadGlobalSettings(XmlConfig xmlConfig) {
        globalSettings.fillFrom(xmlConfig.getGlobalSettings());
    }

    private void loadConfigs(XmlConfig xmlConfig) {
    /* Warning!!! the order of this function matters,
     broker configs must be read BEFORE topic configs,
     topic configs must be read BEFORE messages configs and listener configs
    The order is important because these object depend on each other in that order
    broker_config <- topic_config <- message_config/listener_config
     */
        loadBrokerConfigs(xmlConfig);
        loadTopicConfigs(xmlConfig);
        loadMessages(xmlConfig);
        loadListenerConfigs(xmlConfig);
    }

    private void loadGuiSettings(XmlConfig xmlConfig) {
        guiSettings.fillFrom(xmlConfig.getGuiSettings());
    }

    private void loadBrokerConfigs(XmlConfig xmlConfig) {
        xmlConfig
            .getBrokerConfigs()
            .forEach(pojo -> modelDataProxy.addConfig(converter.fromPojo(pojo)));
    }

    private void loadTopicConfigs(XmlConfig xmlConfig) {
        xmlConfig
            .getTopicConfigs()
            .forEach(pojo -> modelDataProxy.addConfig(converter.fromPojo(pojo)));
    }

    private void loadListenerConfigs(XmlConfig xmlConfig) {
        xmlConfig
            .getListenerConfigs()
            .forEach(pojo -> modelDataProxy.addConfig(converter.fromPojo(pojo)));
    }

    private void loadMessages(XmlConfig xmlConfig) {
        xmlConfig
            .getMessagesConfigs()
            .forEach(pojo -> modelDataProxy.addConfig(converter.fromPojo(pojo)));
    }


}
