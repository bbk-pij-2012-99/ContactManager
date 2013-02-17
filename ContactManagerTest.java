import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

public class ContactManagerTest {

	private ContactManager testManager;
	private Set<Contact> contacts;
	private Calendar date;
	private Calendar pastDate;

	@Before
	public void buildUp() {
		testManager = new ContactManagerImpl();
		Contact contact = new ContactImpl("Jane", 1);
		contacts = new HashSet<>();
		contacts.add(contact);
		date = Calendar.getInstance();
		date.set(2013, 4, 1, 12, 30);
		pastDate = Calendar.getInstance();
		pastDate.set(2011, 1, 1, 10, 30);
	}

	@After
	public void afterTest() {
		testManager = null;
	}

	@Test
	public void testAddFutureMeetingId() {
		int id = testManager.addFutureMeeting(contacts, date);
		assertEquals(id, 1);
	}
	
	@Test
	public void testAddFutureMeetingDate() {
		try {
			testManager.addFutureMeeting(contacts, pastDate);
			fail();			
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting occurs in the past.", e.getMessage());
		}
	}

	@Test
	public void testAddPastMeetingContacts() {
		Set<Contact> emptyContacts = new HashSet<>();
		try {
			testManager.addNewPastMeeting(emptyContacts, pastDate, "Some notes.");
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("No contacts given.", e.getMessage());
		}
	}

	@Test
	public void testAddPastMeetingNullContact() {
		Set<Contact> nullContacts = null;
		try {
			testManager.addNewPastMeeting(nullContacts, pastDate, "Some notes.");
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No contacts given.", e.getMessage());
		}
	}

	@Test
	public void testAddPastMeetingNullDate() {
		Calendar nullDate = null;
		try {
			testManager.addNewPastMeeting(contacts, nullDate, "Some notes.");
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No date given.", e.getMessage());
		}
	}

	@Test
	public void testAddPastMeetingNullNotes() {
		String notes = null;
		try {
			testManager.addNewPastMeeting(contacts, pastDate, notes);
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No notes given.", e.getMessage());
		}
	}

	@Test
	public void testGetPastMeeting() {
		testManager.addNewPastMeeting(contacts, pastDate, "Some notes."); // First meeting therefore Id = 1
		PastMeeting pastMeeting = testManager.getPastMeeting(1);
		Calendar outputDate = pastMeeting.getDate();
		assertEquals(outputDate, pastDate);
		String outputNotes = pastMeeting.getNotes();
		assertEquals(outputNotes, "Some notes.");
	}

	@Test
	public void testGetPastMeetingDateEx() {
		int id = testManager.addFutureMeeting(contacts, date);
		try {
			PastMeeting pastMeeting = testManager.getPastMeeting(id);	
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting occurs in the future.", e.getMessage());
		}
	}

	@Test
	public void testGetPastMeetingEx() {
		try {
			PastMeeting pastMeeting = testManager.getPastMeeting(10);	
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("Meeting does not exist.", e.getMessage());
		}
	}

	@Test
	public void testAddMeetingNotes() {
		testManager.addNewPastMeeting(contacts, pastDate, "Some notes."); // First meeting therefore Id = 1
		testManager.addMeetingNotes(1, "More notes.");
		PastMeeting pastMeeting = testManager.getPastMeeting(1);
		String output = pastMeeting.getNotes();
		assertEquals("\nSome notes.\nMore notes.", output);
	}

	@Test
	public void testAddMeetingNotesIdEx() {
		try {
			testManager.addMeetingNotes(10, "More notes.");	
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting does not exisit.", e.getMessage());
		}
	}

	@Test
	public void testAddMeetingNotesDateEx() {
		int id = testManager.addFutureMeeting(contacts, date);
		try {
			testManager.addMeetingNotes(id, "Notes.");
			fail();
		}
		catch(IllegalStateException e) {
			assertEquals("Meeting occurs in the future.", e.getMessage());
		}
	}

	@Test
	public void testAddMeetingNotesEx() {
		testManager.addNewPastMeeting(contacts, pastDate, "Some notes."); // First meeting therefore Id = 1
		String notes = null;
		try {
			testManager.addMeetingNotes(1, notes);	
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No notes given.", e.getMessage());
		}
	}



}