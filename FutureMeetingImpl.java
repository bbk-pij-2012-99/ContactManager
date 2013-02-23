import java.util.Calendar;
import java.util.Set;
import java.io.Serializable;
/**
* A meeting to be held in the future
*/
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting, Serializable {

	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
	}

	public FutureMeetingImpl() {
		
	}

}