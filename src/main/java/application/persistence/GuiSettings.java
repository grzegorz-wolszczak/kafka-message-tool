package application.persistence;

import application.model.XmlElementNames;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@ToString
@XmlRootElement(name = XmlElementNames.GUI_SETTINGS)
public class GuiSettings {

    private static final double DEFAULT_MAIN_SPLIT_PANE_DIVIDER_POSITION = 0.75f;
    private static final double DEFAULT_UPPER_SPLIT_PANE_DIVIDER_POSITION = 0.26f;

    private static final double DEFAULT_WINDOW_WIDTH = 1200.0;
    private static final double DEFAULT_WINDOW_HEIGHT = 900.0;

    private final DoubleProperty mainWindowWidthProperty = new SimpleDoubleProperty(DEFAULT_WINDOW_WIDTH);
    private final DoubleProperty mainWindowHeightProperty = new SimpleDoubleProperty(DEFAULT_WINDOW_HEIGHT);
    private final DoubleProperty mainWindowSplitPaneDividerPosition = new SimpleDoubleProperty(DEFAULT_MAIN_SPLIT_PANE_DIVIDER_POSITION);
    private final DoubleProperty upperSplitPaneDividerPosition = new SimpleDoubleProperty(DEFAULT_UPPER_SPLIT_PANE_DIVIDER_POSITION);
    private final BooleanProperty messagesViewButtonSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty problemsViewButtonSelected = new SimpleBooleanProperty(false);

    @XmlElement(name = XmlElementNames.PROBLEMS_VIEW_BUTTON_SELECTED)
    public boolean getProblemsViewButtonSelected() {
        return problemsViewButtonSelected.get();
    }

    public void setProblemsViewButtonSelected(boolean problemsViewButtonSelected) {
        this.problemsViewButtonSelected.set(problemsViewButtonSelected);
    }

    @XmlElement(name = XmlElementNames.MESSAGES_VIEW_BUTTON_SELECTED)
    public boolean getMessagesViewButtonSelected() {
        return messagesViewButtonSelected.get();
    }

    public void setMessagesViewButtonSelected(boolean messagesViewButtonSelected) {
        this.messagesViewButtonSelected.set(messagesViewButtonSelected);
    }

    public BooleanProperty messagesViewButtonSelectedProperty() {
        return messagesViewButtonSelected;
    }

    @XmlElement(name = XmlElementNames.MAIN_WINDOW_WIDTH)
    public double getMainWindowWidth() {
        return mainWindowWidthProperty.get();
    }


    public void setMainWindowWidth(double mainWindowWidthProperty) {
        this.mainWindowWidthProperty.set(mainWindowWidthProperty);
    }

    @XmlElement(name = XmlElementNames.MAIN_WINDOW_HEIGHT)
    public double getMainWindowHeight() {
        return mainWindowHeightProperty.get();
    }


    public void setMainWindowHeight(double mainWindowHeightProperty) {
        this.mainWindowHeightProperty.set(mainWindowHeightProperty);
    }


    @XmlElement(name = XmlElementNames.MAIN_WINDOW_SPLIT_PANE_DIVIDER_POSITION)
    public double getMainWindowSplitPaneDividerPosition() {
        return mainWindowSplitPaneDividerPosition.get();
    }


    public void setMainWindowSplitPaneDividerPosition(double mainSplitPaneDividerPosition) {
        this.mainWindowSplitPaneDividerPosition.set(mainSplitPaneDividerPosition);
    }

    @XmlElement(name = XmlElementNames.UPPER_SPLIT_PANE_DIVIDER_POSITION)
    public double getUpperSplitPaneDividerPosition() {
        return upperSplitPaneDividerPosition.get();
    }


    public void setUpperSplitPaneDividerPosition(double upperSplitPane) {
        this.upperSplitPaneDividerPosition.set(upperSplitPane);
    }


    public DoubleProperty mainWindowSplitPaneDividerPositionProperty() {
        return mainWindowSplitPaneDividerPosition;
    }


    public DoubleProperty upperSplitPaneDividerPositionProperty() {
        return upperSplitPaneDividerPosition;
    }


    public DoubleProperty mainWindowWidthProperty() {
        return mainWindowWidthProperty;
    }


    public DoubleProperty mainWindowHeightProperty() {
        return mainWindowHeightProperty;
    }


    public void fillFrom(GuiSettings a) {
        if (a == null) {
            return;
        }
        setMessagesViewButtonSelected(a.getMessagesViewButtonSelected());
        setProblemsViewButtonSelected(a.getProblemsViewButtonSelected());
        setMainWindowHeight(a.getMainWindowHeight());
        setMainWindowWidth(a.getMainWindowWidth());
        setMainWindowSplitPaneDividerPosition(a.getMainWindowSplitPaneDividerPosition());
        setUpperSplitPaneDividerPosition(a.getUpperSplitPaneDividerPosition());
    }

    public BooleanProperty problemsViewButtonSelectedProperty() {
        return problemsViewButtonSelected;
    }
}
