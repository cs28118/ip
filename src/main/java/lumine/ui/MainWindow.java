package lumine.ui;

import java.util.Objects;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lumine.Lumine;

/**
 * Controls Lumine's main conversation window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private Lumine lumine;

    private final Image userImage = new Image(Objects.requireNonNull(
            MainWindow.class.getResource("/images/user.gif")).toExternalForm());
    private final Image lumineImage = new Image(Objects.requireNonNull(
            MainWindow.class.getResource("/images/lumine_profile.png")).toExternalForm());

    @FXML
    private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Connects the application logic to this conversation window.
     *
     * @param lumine the application instance that handles user commands.
     */
    public void setLumine(Lumine lumine) {
        this.lumine = Objects.requireNonNull(lumine);
        addLumineDialog(lumine.getGreeting());
    }

    /**
     * Adds the user's message and Lumine's command response to the conversation.
     * Closes the application when the command requests an exit.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        if (lumine == null) {
            throw new IllegalStateException("Lumine has not been connected to the main window.");
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage));
        addLumineDialog(lumine.getResponse(input));
        userInput.clear();

        if (lumine.isExitRequested()) {
            Platform.exit();
        }
    }

    /**
     * Adds Lumine's response as a left-aligned dialog box.
     */
    private void addLumineDialog(String response) {
        dialogContainer.getChildren().add(DialogBox.getLumineDialog(response, lumineImage));
    }
}
