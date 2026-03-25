package seedu.address.model.person;

/**
 * Collection of student attendance each week
 */
public interface WeeklyAttendanceList extends Comparable<WeeklyAttendanceList> , Trackable {
    /**
     * Marks as attended for specific week.
     * @param index the zero-based index of the week.
     */
    public void markWeekAsAttended(int index);

    /**
     * Marks as unattended for specific week.
     * @param index the zero-based index of the week.
     */
    public void markWeekAsAbsent(int index);

    /**
     * Calculates the week attendance rate as a percentage.
     * @return the attendance percentage.
     */
    public double calculateWeekAttendance();

    /**
     * @return attendence each week
     */
    WeeklyAttendance[] getWeeks();
}
