package duke.task;

import duke.DukeException;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalTime;



public class Deadline extends Task {

    /** Date of the duke.task **/
    protected LocalDate byDate;
    /** Time of the duke.task **/
    protected LocalTime byTime;

    private Deadline(String taskName, String taskDateTime) throws DukeException {
        super(taskName);
        this.tag = "D";
        parseTime(taskDateTime.trim().replace("/", "-"));
    }

    /**
     * Creates duke.task.Deadline duke.task
     *
     * @param taskDescription description of the duke.task
     * @return duke.task.Deadline duke.task
     * @throws DukeException if the format of the duke.task description is wrong
     */
    public static Deadline create(String taskDescription) throws DukeException {
        if (!taskDescription.contains("/by")) {
            throw new DukeException("Please include '/by' in front of the deadline");
        }
        String[] nameTimePair = taskDescription.split(" /by");
        String taskName = nameTimePair[0];
        String taskDateTime = nameTimePair[1];
        return new Deadline(taskName, taskDateTime);
    }

    public static Deadline create(String taskName, String taskTime) throws DukeException {
        return new Deadline(taskName, taskTime);
    }

    /**
     * Converts string date/time in LocalDate and LocalTime
     * Stores them into the LocalDate and LocalTime variable
     *
     * @param taskDateTime the string representation of the time and date
     * @throws DukeException if the format of the duke.task date/time is wrong
     */
    private void parseTime(String taskDateTime) throws DukeException {
        String[] dateTime = taskDateTime.replace("/", "-").split(" ", 2);
        try {
            this.byDate = LocalDate.parse(dateTime[0]);
        } catch (DateTimeParseException e) {
            throw new DukeException("please enter a valid yyyy-mm-dd format");
        }
        try {
            if (dateTime.length == 2) {
                this.byTime = LocalTime.parse(dateTime[1]);
            }
        } catch (DateTimeParseException e) {
            throw new DukeException("please enter a valid HH:MM format");
        }
    }
    /**
     *  * return the summarised form of the duke.task
     *
     * @return String format of the summarised details of the duke.task
     */
    @Override
    public String toString() {
        String symbol = isDone ? "\u2713" : "\u2718";
        if (byTime != null) {
            return String.format("[%s][%s] %s (by: %s %s)\n", tag, symbol, taskName,
                    byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")), byTime.toString());
        } else {
            return String.format("[%s][%s] %s (by: %s)\n", tag, symbol, taskName,
                    byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
        }
    }

    /**
     * return the summarised form of the duke.task in the format to be saved
     *
     * @return String format of the summarised details of the duke.task to be saved
     */
    @Override
    public String safeFileFormat() {
        int done = isDone ? 1 : 0;
        if (byTime == null) {
            return String.format("%s | %d | %s | %s \n", tag, done, taskName, byDate.toString());
        } else {
            return String.format("%s | %d | %s | %s %s \n", tag, done, taskName, byDate.toString(), byTime.toString());
        }
    }
}