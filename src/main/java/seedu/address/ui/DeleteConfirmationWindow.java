package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for a delete confirmation popup.
 */
public class DeleteConfirmationWindow extends UiPart<Stage> {

    private static final String FXML = "DeleteConfirmationWindow.fxml";

    private boolean isConfirmed = false;

    @FXML
    private Label confirmationMessage;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    /**
     * Creates a new DeleteConfirmationWindow.
     *
     * @param root Stage to use as the root of the window.
     * @param owner Owner stage of the popup.
     */
    public DeleteConfirmationWindow(Stage root, Stage owner) {
        super(FXML, root);
        root.initOwner(owner);
    }

    /**
     * Creates a new DeleteConfirmationWindow.
     *
     * @param owner Owner stage of the popup.
     */
    public DeleteConfirmationWindow(Stage owner) {
        this(new Stage(), owner);
    }

    /**
     * Sets the message displayed in the confirmation popup.
     *
     * @param message Message to display in the popup.
     */
    public void setMessage(String message) {
        confirmationMessage.setText(message);
    }

    /**
     * Shows the confirmation popup and waits for the user to respond.
     *
     * @return true if the user confirms the deletion, false otherwise.
     */
    public boolean showAndWait() {
        getRoot().centerOnScreen();
        getRoot().showAndWait();
        return isConfirmed;
    }

    @FXML
    private void handleOk() {
        isConfirmed = true;
        getRoot().close();
    }

    @FXML
    private void handleCancel() {
        isConfirmed = false;
        getRoot().close();
    }
}
