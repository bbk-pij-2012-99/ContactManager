import java.util.Calendar;
import java.util.Set;
import java.io.Serializable;
/**
* A meeting that was held in the past.
*
* It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting, Serializable {
	private String notes;

	public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
		notes = "";
	}

	public PastMeetingImpl() {

	}

	/**
	* Returns the notes from the meeting.
	*
	* If there are no notes, the empty string is returned.
	*
	* @return the notes from the meeting.
	*/
	public String getNotes(){
		return notes;
	}

	/**
	* Adds notes to the meeting. If notes already exist, the new notes are appended.
	*
	* @param text messages to be added about the meeting.
	* @throws NullPointerException if the notes are null.
	*/
	public void addNotes(String text) {
		notes = notes + "\n" + text;
	}
}