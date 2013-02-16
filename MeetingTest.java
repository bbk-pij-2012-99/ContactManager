import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

public class MeetingTest {

	private Meeting testMeeting;
	private Calendar date;
	private Set<Contact> contacts;

	@Before
	public void buildUp() {
		date = Calendar.getInstance();
		date.set(2013, 0, 1, 13, 30);
		Contact contact1 = new ContactImpl("Jane Smith", 1);
		Contact contact2 = new ContactImpl("John Jones", 2);
		contacts = new HashSet<>();
		contacts.add(contact1);
		contacts.add(contact2);
		testMeeting = new MeetingImpl(1, date, contacts);
	}


	@Test
	public void testIdAccessor() {
		int id = testMeeting.getId();
		assertEquals(id, 1);
	}

	@Test
	public void testDateAccessor() {
		Calendar output = testMeeting.getDate();
		assertEquals(date, output);
	}

	@Test
	public void testContactsAccessor() {
		Set<Contact> output = testMeeting.getContacts();
		assertEquals(contacts, output);
	}

}