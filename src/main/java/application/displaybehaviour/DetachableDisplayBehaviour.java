package application.displaybehaviour;

import application.constants.ApplicationConstants;
import application.model.ModelConfigObject;
import application.utils.GuiUtils;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DetachableDisplayBehaviour implements DisplayBehaviour {

    private static final int DEFAULT_WIDHT = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    private static final int MIN_WIDHT = 600;
    private static final int MIN_HEIGHT = 400;
    private static final double POSITION_NOT_SET = -1;
    private final AnchorPane parentPane;
    private final Pane childNode;
    private final BooleanProperty isDetachedProperty;
    private final ModelConfigObject trackedModeObject;
    private final DetachedPaneContent detachedPaneContent;
    private StringExpression windowTitleProperty;
    private ModelConfigObjectsGuiInformer guiInformer;
    private final Stage stage = new Stage();
    private double sceneWidth = DEFAULT_WIDHT;
    private double sceneHeight = DEFAULT_HEIGHT;
    private double positionX = POSITION_NOT_SET;
    private double positionY = POSITION_NOT_SET;
    private Scene scene;

    public DetachableDisplayBehaviour(AnchorPane parentPane,
                                      StringExpression windowTitleProperty,
                                      Pane childNode,
                                      BooleanProperty isDetachedProperty,
                                      ModelConfigObject trackedModeObject,
                                      ModelConfigObjectsGuiInformer guiInformer) {
        this.parentPane = parentPane;
        this.windowTitleProperty = windowTitleProperty;
        this.childNode = childNode;
        this.isDetachedProperty = isDetachedProperty;
        this.trackedModeObject = trackedModeObject;
        this.guiInformer = guiInformer;

        setStageAppearance();
        setDetachPropertyListener();
        setStageOnCloseEvent();
        setSizeChangedListeners();
        setPositionChangedListeners();
        setLastElementRemovedListener();
        detachedPaneContent = new DetachedPaneContent(this::reattachPaneCallback);

    }

    @Override
    public void display() {
        if (isDetachedProperty.getValue()) {
            detachFromParentPane();
            displayOnOwnStage();
        } else {
            displayAttachedToParentPane();
        }
    }

    private void reattachPaneCallback() {
        isDetachedProperty.setValue(false);
    }

    private void setStageAppearance() {
        GuiUtils.addApplicationIcon(stage);
        stage.titleProperty().bind(windowTitleProperty);

        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDHT);
    }

    private void setLastElementRemovedListener() {
        guiInformer.lastRemovedObjectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == trackedModeObject) {
                // element (modelObject) for this view is just removed
                // if we are in detached state then close the window
                detachFromOwnStage();
            }
        });
    }

    private void setPositionChangedListeners() {
        stage.xProperty().addListener((observable, oldValue, newValue) -> positionX = newValue.doubleValue());
        stage.yProperty().addListener((observable, oldValue, newValue) -> positionY = newValue.doubleValue());
    }

    private void setSizeChangedListeners() {
        stage.heightProperty().addListener((observable, oldValue, newValue) -> sceneHeight = newValue.doubleValue());
        stage.widthProperty().addListener((observable, oldValue, newValue) -> sceneWidth = newValue.doubleValue());
    }

    private void setStageOnCloseEvent() {
        stage.setOnCloseRequest(windowEvent -> isDetachedProperty.setValue(false));
    }

    private void setDetachPropertyListener() {
        isDetachedProperty.addListener((observable, oldValue, newValue) -> {
            display();
        });
    }

    private void detachFromParentPane() {
        GuiUtils.setOnParentPane(parentPane, detachedPaneContent);
    }

    private void displayOnOwnStage() {
        initSceneIfNeeded();
        stage.show();
    }

    private void initSceneIfNeeded() {
        if (scene == null) {
            scene = new Scene(childNode, sceneWidth, sceneHeight);
            GuiUtils.loadCssIfPossible(scene, ApplicationConstants.GLOBAL_CSS_FILE_NAME);
        }
        if (positionX != POSITION_NOT_SET && positionY != POSITION_NOT_SET) {
            stage.setX(positionX);
            stage.setY(positionY);
        }
        stage.setScene(scene);
    }

    private void displayAttachedToParentPane() {
        detachFromOwnStage();
        if (isMyTrackedModelObjectSelectedAndFocused()) {
            attachChildToParent();
        }
    }

    private void detachFromOwnStage() {
        scene = null;
        stage.setScene(null);
        stage.close();
    }

    private boolean isMyTrackedModelObjectSelectedAndFocused() {
        return guiInformer.selectedObject() == trackedModeObject;
    }

    private void attachChildToParent() {
        GuiUtils.setOnParentPane(parentPane, childNode);
    }
}
