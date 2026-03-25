package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.CourseId;
import seedu.address.model.person.FilterMatchesPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Progress;
import seedu.address.model.person.TGroup;
import seedu.address.testutil.PersonBuilder;

public class FilterCommandTest {

    @Test
    public void execute_filterByCourseId_success() throws Exception {
        Person personWithMatchingCourse = new PersonBuilder().withCourseId("CS2103T").build();
        Person personWithDifferentCourse = new PersonBuilder().withCourseId("CS2101").build();

        ModelStubWithPersons modelStub = new ModelStubWithPersons(personWithMatchingCourse, personWithDifferentCourse);
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals("There are 1 students matching this filter.", commandResult.getFeedbackToUser());
        assertEquals(List.of(personWithMatchingCourse), modelStub.filteredPersons);
    }

    @Test
    public void execute_filterByTGroup_success() throws Exception {
        Person personWithMatchingTGroup = new PersonBuilder().withTGroup("T01").build();
        Person personWithDifferentTGroup = new PersonBuilder().withTGroup("T02").build();

        ModelStubWithPersons modelStub = new ModelStubWithPersons(personWithMatchingTGroup, personWithDifferentTGroup);
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.of(new TGroup("T01")), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals("There are 1 students matching this filter.", commandResult.getFeedbackToUser());
        assertEquals(List.of(personWithMatchingTGroup), modelStub.filteredPersons);
    }

    @Test
    public void execute_filterByProgress_success() throws Exception {
        Person personWithMatchingProgress = new PersonBuilder().withProgress(Progress.valueOf("ON_TRACK")).build();
        Person personWithDifferentProgress = new PersonBuilder().withProgress(Progress.valueOf("AT_RISK")).build();

        ModelStubWithPersons modelStub = new ModelStubWithPersons(personWithMatchingProgress,
                personWithDifferentProgress);
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.empty(), Optional.empty(), Optional.of(Progress.ON_TRACK));
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals("There are 1 students matching this filter.", commandResult.getFeedbackToUser());
        assertEquals(List.of(personWithMatchingProgress), modelStub.filteredPersons);
    }

    @Test
    public void execute_filterByBothCourseAndTGroup_success() throws Exception {
        Person personMatchingBoth = new PersonBuilder().withCourseId("CS2103T").withTGroup("T01").build();
        Person personMatchingCourseOnly = new PersonBuilder().withCourseId("CS2103T").withTGroup("T02").build();
        Person personMatchingTGroupOnly = new PersonBuilder().withCourseId("CS2101").withTGroup("T01").build();

        ModelStubWithPersons modelStub = new ModelStubWithPersons(
                personMatchingBoth, personMatchingCourseOnly, personMatchingTGroupOnly);
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.of(new TGroup("T01")), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals("There are 1 students matching this filter.", commandResult.getFeedbackToUser());
        assertEquals(List.of(personMatchingBoth), modelStub.filteredPersons);
    }

    @Test
    public void execute_filterByAllThree_success() throws Exception {
        Person personMatchingAll = new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build();
        Person personWrongProgress = new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T01")
                .withProgress(Progress.valueOf("AT_RISK"))
                .build();
        Person personWrongTGroup = new PersonBuilder()
                .withCourseId("CS2103T")
                .withTGroup("T02")
                .withProgress(Progress.valueOf("ON_TRACK"))
                .build();

        ModelStubWithPersons modelStub = new ModelStubWithPersons(
                personMatchingAll, personWrongProgress, personWrongTGroup);
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")),
                Optional.of(new TGroup("T01")),
                Optional.of(Progress.ON_TRACK));
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals("There are 1 students matching this filter.", commandResult.getFeedbackToUser());
        assertEquals(List.of(personMatchingAll), modelStub.filteredPersons);
    }

    @Test
    public void execute_zeroMatches_success() throws Exception {
        Person personWithDifferentCourse = new PersonBuilder().withCourseId("CS2101").build();

        ModelStubWithPersons modelStub = new ModelStubWithPersons(personWithDifferentCourse);
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("NONEXISTENT")), Optional.empty(), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);

        CommandResult commandResult = filterCommand.execute(modelStub);

        assertEquals("There are 0 students matching this filter.", commandResult.getFeedbackToUser());
        assertEquals(List.of(), modelStub.filteredPersons);
    }

    @Test
    public void equals() {
        FilterMatchesPredicate firstPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty());
        FilterMatchesPredicate secondPredicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2101")), Optional.empty(), Optional.empty());

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        assertFalse(filterFirstCommand.equals(1));
        assertFalse(filterFirstCommand.equals(null));
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void toStringMethod() {
        FilterMatchesPredicate predicate = new FilterMatchesPredicate(
                Optional.of(new CourseId("CS2103T")), Optional.empty(), Optional.empty());
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator, Predicate<Person> predicateShowAllPersons) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a list of persons and tracks filtered results.
     */
    private class ModelStubWithPersons extends ModelStub {
        private final List<Person> persons;
        private List<Person> filteredPersons;

        ModelStubWithPersons(Person... persons) {
            this.persons = List.of(persons);
            this.filteredPersons = new ArrayList<>();
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return javafx.collections.FXCollections.observableArrayList(filteredPersons);
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            requireNonNull(predicate);
            filteredPersons = persons.stream()
                    .filter(predicate)
                    .toList();
        }
    }
}
