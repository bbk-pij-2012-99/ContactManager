import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
/**
* A class to represent meetings
*
* Meetings have unique IDs, scheduled date and a list of participating contacts. Past meetings
* may have notes.
*/
public class MeetingImpl implements Meeting, PastMeeting, FutureMeeting, Serializable {
	private int id;
	private Calendar date;
	private Set<Contact> contacts;
	private String notes;

	public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		this.id = id;
		this.date = date;
		this.contacts = contacts;
	}

	public MeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
		this(id, date, contacts);
		this.notes = notes;
	}

	public MeetingImpl() {

	}

	/**
	* Returns the id of the meeting.
	*
	* @return the id of the meeting.
	*/
	public int getId(){
		return id;
	}
	
	/**
	* Return the date of the meeting.
	*
	* @return the date of the meeting.
	*/
	public Calendar getDate(){
		return date;
	}
	
	/**
	* Return the details of people that attended the meeting.
	*
	* The list contains a minimum of one contact (if there were
	* just two people: the user and the contact) and may contain an
	* arbitraty number of them.
	*
	* @return the details of people that attended the meeting.
	*/
	public Set<Contact> getContacts(){
		return contacts;
	}
	
	/**
	* Returns the notes from the meeting if meeting occurred in the past.
	*
	* If there are no notes, the empty string is returned.
	*
	* @return the notes from the meeting.
	*/
	public String getNotes(){
		return notes == null ? "" : notes;
	}
	
	/**
	* Adds notes to the meeting if meeting occurred in the past. If notes already exist, the new notes are appended.
	*
	* @param text messages to be added about the meeting.
	* @throws NullPointerException if the notes are null.
	*/
	public void addNotes(String text) {
		notes = getNotes() + "\n" + text;
	}
}