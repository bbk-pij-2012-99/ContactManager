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

		Calendar outputDate = testManager.getPastMeeting(2).getDate();
		assertEquals(outputDate.YEAR, pastDate.YEAR);
		assertEquals(outputDate.DAY_OF_YEAR, pastDate.DAY_OF_YEAR);
		assertEquals(outputDate.HOUR_OF_DAY, pastDate.HOUR_OF_DAY);
		assertEquals(outputDate.MINUTE, pastDate.MINUTE);

		outputDate = testManager.getFutureMeeting(1).getDate();
		assertEquals(outputDate.YEAR, futureDate.YEAR);
		assertEquals(outputDate.DAY_OF_YEAR, futureDate.DAY_OF_YEAR);
		assertEquals(outputDate.HOUR_OF_DAY, futureDate.HOUR_OF_DAY);
		assertEquals(outputDate.MINUTE, futureDate.MINUTE);

		Set<Contact> outputContact = testManager.getContacts(1);
		Iterator<Contact> iter = outputContact.iterator();
		String outputNotes = iter.next().getNotes(); 
		assertEquals(outputNotes, "\nChess player");

		outputContact = testManager.getContacts(2);
		iter = outputContact.iterator();
		assertEquals(iter.next().getName(), "Sarah smile");

	}

}