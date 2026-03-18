package seedu.address.testutil;

import seedu.address.model.person.CourseId;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TGroup;
import seedu.address.model.person.Tele;


/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_STUDENT_ID = "A0123456X";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_COURSE_ID = "CS2103T";
    public static final String DEFAULT_TUT_GROUP = "T01";
    public static final String DEFAULT_TELE = "@amybee";

    private Name name;
    private StudentId studentId;
    private Email email;
    private CourseId courseId;
    private TGroup tutGroup;
    private Tele tele;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        studentId = new StudentId(DEFAULT_STUDENT_ID);
        email = new Email(DEFAULT_EMAIL);
        courseId = new CourseId(DEFAULT_COURSE_ID);
        tutGroup = new TGroup(DEFAULT_TUT_GROUP);
        tele = new Tele(DEFAULT_TELE);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        studentId = personToCopy.getStudentId();
        email = personToCopy.getEmail();
        courseId = personToCopy.getCourseId();
        tutGroup = personToCopy.getTGroup();
        tele = personToCopy.getTele();
    }

    /**
     * Sets the {@link Name} of the {@code Person} being built.
     *
     * @param name The name to set.
     * @return This {@code PersonBuilder} instance for method chaining.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@link StudentId} of the {@code Person} being built.
     *
     * @param studentId The student ID to set.
     * @return This {@code PersonBuilder} instance for method chaining.
     */
    public PersonBuilder withStudentId(String studentId) {
        this.studentId = new StudentId(studentId);
        return this;
    }

    /**
     * Sets the {@link Email} of the {@code Person} being built.
     *
     * @param email The email to set.
     * @return This {@code PersonBuilder} instance for method chaining.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@link CourseId} of the {@code Person} being built.
     *
     * @param courseId The course ID to set.
     * @return This {@code PersonBuilder} instance for method chaining.
     */
    public PersonBuilder withCourseId(String courseId) {
        this.courseId = new CourseId(courseId);
        return this;
    }

    /**
     * Sets the {@link TGroup} of the {@code Person} being built.
     *
     * @param tutGroup The tutorial group to set.
     * @return This {@code PersonBuilder} instance for method chaining.
     */
    public PersonBuilder withTutGroup(String tutGroup) {
        this.tutGroup = new TGroup(tutGroup);
        return this;
    }

    /**
     * Sets the {@link Tele} (Telegram handle) of the {@code Person} being built.
     *
     * @param tele The Telegram handle to set.
     * @return This {@code PersonBuilder} instance for method chaining.
     */
    public PersonBuilder withTele(String tele) {
        this.tele = new Tele(tele);
        return this;
    }

    public Person build() {
        return new Person(name, courseId, email, studentId, tutGroup, tele);
    }
}
