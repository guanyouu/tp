package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} matches the given filter criteria.
 *
 * <p>Matching rules:
 * <ul>
 *     <li>course ID and tutorial group are compared using their normalized values. {@code CourseId}
 *     and {@code TGroup} store uppercase values, hence the comparison is effectively
 *     case-insensitive.</li>
 *     <li>progress is compared by equality.</li>
 *     <li>absence count matches when the person's absence count is greater than or equal to
 *     the provided threshold.</li>
 * </ul>
 *
 * <p>The predicate treats each filter field as optional; a missing field does not affect
 * the overall conjunction of tests.
 */
public class FilterMatchesPredicate implements Predicate<Person> {
    private final Optional<CourseId> courseId;
    private final Optional<TGroup> tGroup;
    private final Optional<Progress> progress;
    private final Optional<Integer> absenceCount;

    /**
     * Creates a {@code FilterMatchesPredicate} with the given filter criteria.
     *
     * @param courseId The course ID to match, or empty if not filtering by course ID.
     * @param tGroup The tutorial group to match, or empty if not filtering by tutorial group.
     * @param progress The progress to match, or empty if not filtering by progress.
     * @param absenceCount The minimum absence count to match, or empty if not filtering by absences.
     */
    public FilterMatchesPredicate(Optional<CourseId> courseId, Optional<TGroup> tGroup,
                                  Optional<Progress> progress, Optional<Integer> absenceCount) {
        requireNonNull(courseId);
        requireNonNull(tGroup);
        requireNonNull(progress);
        requireNonNull(absenceCount);

        this.courseId = courseId;
        this.tGroup = tGroup;
        this.progress = progress;
        this.absenceCount = absenceCount;
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        boolean matchesCourse =
                courseId.isEmpty() || person.getCourseId().value.equals(courseId.get().value);
        boolean matchesTGroup =
                tGroup.isEmpty() || person.getTGroup().value.equals(tGroup.get().value);
        boolean matchesProgress =
                progress.isEmpty() || person.getProgress().equals(progress.get());
        boolean matchesAbsenceCount =
                absenceCount.isEmpty() || person.getAbsenceCount() >= absenceCount.get();

        return matchesCourse && matchesTGroup && matchesProgress && matchesAbsenceCount;
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
                && progress.equals(otherPredicate.progress)
                && absenceCount.equals(otherPredicate.absenceCount);
    }

    /**
     * Returns a hash code value for this predicate. This is required because {@link #equals(Object)}
     * is overridden. The hash code is computed from all filter fields so that equal predicates
     * produce the same hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseId, tGroup, progress, absenceCount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("courseId", courseId)
                .add("tGroup", tGroup)
                .add("progress", progress)
                .add("absenceCount", absenceCount)
                .toString();
    }
}
