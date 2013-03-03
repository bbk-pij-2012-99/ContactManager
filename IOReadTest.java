import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.Iterator;

public class IOReadTest {
	
	private ContactManager testManager;
	private Calendar futureDate;
	private Calendar pastDate;
	private Set<Contact> testContacts1;

	
	@Test
	public void testRead() {
		testManager = new ContactManagerImpl();

		futureDate = Calendar.getInstance();
		futureDate.set(2013, 4, 1, 12, 30);
		pastDate = Calendar.getInstance();
		pastDate.set(2011, 1, 1, 10, 30);

		assertEquals(testManager.getPastMeeting(2).getDate(), pastDate);
		assertEquals(testManager.getFutureMeeting(1).getDate(), futureDate);

		Set<Contact> outputContact = testManager.getContacts(1);
		Iterator<Contact> iter = outputContact.iterator();
		assertEquals(iter.next().getNotes(), "Chess player");

		outputContact = testManager.getContacts(2);
		iter = outputContact.iterator();
		assertEquals(iter.next().getName(), "Sarah smile");

	}

}