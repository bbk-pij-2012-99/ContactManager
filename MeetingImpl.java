import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
/**
* A class to represent meetings
*
* Meetings have unique IDs, scheduled date and a list of participating contacts
*/
public class MeetingImpl implements Meeting {
	private int id;
	private Calendar date;
	private Set<Contact> contacts;

	public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {

	}


	/**
	* Returns the id of the meeting.
	*
	* @return the id of the meeting.
	*/
	public int getId(){
		return 0;
	}
	/**
	* Return the date of the meeting.
	*
	* @return the date of the meeting.
	*/
	public Calendar getDate(){
		Calendar c = Calendar.getInstance();
		return c;
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
		Set<Contact> contacts = new HashSet<>();
		return contacts;
	}
}