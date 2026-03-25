package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label studentId;
    @FXML
    private Label courseId;
    @FXML
    private Label tGroup;
    @FXML
    private Label email;
    @FXML
    private Label tele;
    @FXML
    private Label progress;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        studentId.setText(person.getStudentId().value);
        courseId.setText(person.getCourseId().value);
        tGroup.setText(person.getTGroup().value);
        email.setText(person.getEmail().value);
        tele.setText(person.getTele() == null ? "-" : person.getTele().value);

        // clear old style classes
        progress.getStyleClass().removeAll("progress-on-track", "progress-needs-attention", "progress-at-risk");

        if (person.getProgress() == Progress.NOT_SET) {
            progress.setVisible(false);
            progress.setManaged(false);
        } else {
            progress.setVisible(true);
            progress.setManaged(true);

            switch (person.getProgress()) {
            case ON_TRACK:
                progress.setText("On Track");
                progress.getStyleClass().add("progress-on-track");
                break;
            case NEEDS_ATTENTION:
                progress.setText("Needs Attention");
                progress.getStyleClass().add("progress-needs-attention");
                break;
            case AT_RISK:
                progress.setText("At Risk");
                progress.getStyleClass().add("progress-at-risk");
                break;
            default:
                break;
            }
        }
    }
}
