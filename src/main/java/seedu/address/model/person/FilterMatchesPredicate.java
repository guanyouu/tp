package seedu.address.model.person;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} matches the given course ID and/or tutorial group, case-insensitively.
 * If both are provided, the person must match both. If only one is provided, the person must match that one.
 */
public class FilterMatchesPredicate implements Predicate<Person> {
    private final Optional<CourseId> courseId;
    private final Optional<TGroup> tGroup;
    private final Optional<Progress> progress;

    /**
     * Creates a {@code FilterMatchesPredicate} with the given course ID and tutorial group.
     *
     * @param courseId The course ID to match, or empty if not filtering by course.
     * @param tGroup The tutorial group to match, or empty if not filtering by group.
     */
    public FilterMatchesPredicate(Optional<CourseId> courseId, Optional<TGroup> tGroup, Optional<Progress> progress) {
        this.courseId = courseId;
        this.tGroup = tGroup;
        this.progress = progress;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesCourse = courseId.isEmpty() || person.getCourseId().value.equalsIgnoreCase(courseId.get().value);
        boolean matchesTGroup = tGroup.isEmpty() || person.getTGroup().value.equalsIgnoreCase(tGroup.get().value);
        boolean matchesProgress = progress.isEmpty() || person.getProgress().equals(progress.get());
        return matchesCourse && matchesTGroup && matchesProgress;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterMatchesPredicate)) {
            return false;
        }

        FilterMatchesPredicate otherPredicate = (FilterMatchesPredicate) other;
        return courseId.equals(otherPredicate.courseId)
                && tGroup.equals(otherPredicate.tGroup)
                && progress.equals(otherPredicate.progress);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("courseId", courseId)
                .add("tGroup", tGroup)
                .add("progress", progress)
                .toString();
    }
}
