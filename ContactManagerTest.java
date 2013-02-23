import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

public class ContactManagerTest {

	private ContactManager testManager;
	private Set<Contact> contacts;
	private Calendar date;
	private Calendar pastDate;

	@Before
	public void buildUp() {
		testManager = new ContactManagerImpl();
		contacts = new HashSet<>();
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
	public void testAddNewContactGetContactByName() {
		assertTrue(testManager.getContacts("Jed").isEmpty());

		testManager.addNewContact("Jed Richards", "Flash developer."); // 1st contact therefore Id = 1 
		testManager.addNewContact("Sarah Small", "Princess.");
		Set<Contact> output = testManager.getContacts("Jed");
		assertEquals(output.size(), 1);
		for (Contact c : output) {
			assertEquals(c.getId(), 1);	
		}
		
	}

	@Test
	public void testGetContactByNameMultiple() {
		testManager.addNewContact("Fred Smith", "Notes.");
		testManager.addNewContact("John Jones", "More notes.");
		testManager.addNewContact("Mary Smith", "Notes.");
		Set<Contact> output = testManager.getContacts("Smith");
		assertEquals(output.size(), 2);
		Iterator<Contact> iter = output.iterator();
		String firstName = iter.next().getName();
		String secondName = iter.next().getName();
		if (firstName.equals("Fred Smith")) {
			assertEquals(secondName, "Mary Smith");
		}
		else if (firstName.equals("Mary Smith")) {
			assertEquals(secondName, "Fred Smith");
		}
		else {
			fail();
		}
	}

	@Test
	public void testGetContactByNameNull() {
		String name = null;
		try {
			testManager.getContacts(name);
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No name given.", e.getMessage());
		}
	}

	@Test
	public void testAddNewContactNullName() {
		String name = null;
		try {
			testManager.addNewContact(name, "Notes.");
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No name given.", e.getMessage());
		}
	}

	@Test
	public void testAddNewContactNullNotes() {
		String notes = null;
		try {
			testManager.addNewContact("Jed Richards", notes);
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No notes given.", e.getMessage());
		}
	}

	@Test
	public void testGetContactsById() {
		testManager.addNewContact("Jed Richards", "Notes.");
		testManager.addNewContact("Clare Matthews", "Other notes.");
		testManager.addNewContact("Willy Wonka", "Chocolate maker.");
		Set<Contact> output = testManager.getContacts(1, 3);
		assertEquals(output.size(), 2);
		Iterator<Contact> iter = output.iterator();
		String firstName = iter.next().getName();
		String secondName = iter.next().getName();
		if (firstName.equals("Jed Richards")) {
			assertEquals(secondName, "Willy Wonka");
		}
		else if (firstName.equals("Willy Wonka")) {
			assertEquals(secondName, "Jed Richards");
		}
		else {
			fail();
		}
	}

	@Test
	public void testGetContactsByIdEx() {
		testManager.addNewContact("Jed Richards", "Notes.");
		try {
			Set<Contact> output = testManager.getContacts(1, 2);
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
		}
	}

	@Test
	public void testAddFutureMeetingId() {
		testManager.addNewContact("Jim", "notes."); // 1st contact therefore Id = 1
		int id = testManager.addFutureMeeting(testManager.getContacts(1), date);
		assertEquals(id, 1);
	}
	
	@Test
	public void testAddFutureMeetingDate() {
		testManager.addNewContact("Jim", "notes."); // 1st contact therefore Id = 1
		contacts = testManager.getContacts(1);
		try {
			testManager.addFutureMeeting(contacts, pastDate);
			fail();			
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting occurs in the past.", e.getMessage());
		}
	}

	@Test
	public void testAddFutureMeetingContact() {
		Contact Bob = new ContactImpl("Bob", 1);
		Set<Contact> newContacts = new HashSet<>();
		newContacts.add(Bob);
		try {
			testManager.addFutureMeeting(newContacts, date);
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
		}
	}

	@Test
	public void testAddPastMeetingNoContacts() {
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
	public void testAddPastMeetingContacts() {
		Contact Bob = new ContactImpl("Bob", 1);
		Set<Contact> newContacts = new HashSet<>();
		newContacts.add(Bob);
		try{
			testManager.addNewPastMeeting(newContacts, pastDate, "Some notes.");
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
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
	public void testAddAndGetPastMeeting() {
		testManager.addNewContact("Jim", "Some notes."); // 1st contact therefore Id = 1
		Set<Contact> testContacts = testManager.getContacts(1);
		testManager.addNewPastMeeting(testContacts, pastDate, "Meeting notes."); // 1st meeting therefore Id = 1
		PastMeeting pastMeeting = testManager.getPastMeeting(1);
		Calendar outputDate = pastMeeting.getDate();
		assertEquals(outputDate, pastDate);
		Set<Contact> outputContacts = pastMeeting.getContacts();
		assertEquals(outputContacts, testContacts);
	}


	@Test
	public void testGetMeeting() {
		testManager.addNewContact("Jim", "Some notes."); // 1st contact therefore Id = 1
		Set<Contact> testContacts = testManager.getContacts(1);
		testManager.addNewPastMeeting(testContacts, pastDate, "Notes."); // First meeting therefore Id = 1
		int id = testManager.addFutureMeeting(testContacts, date);
		Meeting pastMeeting = testManager.getMeeting(1);
		assertEquals(pastMeeting.getId(), 1);
		Meeting futureMeeting = testManager.getMeeting(id);
		assertEquals(futureMeeting.getId(), id);
		Meeting nullMeeting = testManager.getMeeting(12);
		assertNull(nullMeeting);
	}

	@Test
	public void testGetPastMeetingDateEx() {
		testManager.addNewContact("Jim", "Notes."); // 1st contact therefore Id = 1
		int id = testManager.addFutureMeeting(testManager.getContacts(1), date);
		try {
			PastMeeting pastMeeting = testManager.getPastMeeting(id);	
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting occurs in the future.", e.getMessage());
		}
	}

	@Test
	public void testGetPastMeetingNull() {
		PastMeeting pastMeeting = testManager.getPastMeeting(10);	
		assertEquals(pastMeeting, null);
	}

	@Test
	public void testGetFutureMeeting() {
		testManager.addNewContact("Sally", "Her notes."); // 1st contact therefore Id = 1
		int id = testManager.addFutureMeeting(testManager.getContacts(1), date);
		FutureMeeting futureMeeting = testManager.getFutureMeeting(id);
		assertEquals(futureMeeting.getDate(), date);
	}

	@Test
	public void testGetFutureMeetingDateEx() {
		testManager.addNewContact("Sally", "Her notes."); // 1st contact therefore Id = 1
		testManager.addNewPastMeeting(testManager.getContacts(1), pastDate, "Some notes."); // First meeting therefore Id = 1
		try {
			FutureMeeting futureMeeting = testManager.getFutureMeeting(1);	
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting occurs in the past.", e.getMessage());
		}
	}

	@Test
	public void testGetFutureMeetingNull() {
		FutureMeeting futureMeeting = testManager.getFutureMeeting(10);	
		assertEquals(futureMeeting, null);
	}


	@Test
	public void testAddMeetingNotes() {
		testManager.addNewContact("Jim", "Some notes."); // 1st contact therefore Id = 1
		testManager.addNewPastMeeting(testManager.getContacts(1), pastDate, "Some notes."); // First meeting therefore Id = 1
		testManager.addMeetingNotes(1, "More notes.");
		String output = testManager.getPastMeeting(1).getNotes();
		assertEquals("\nSome notes.\nMore notes.", output);
	}

	@Test
	public void testAddMeetingNotesIdEx() {
		try {
			testManager.addMeetingNotes(10, "More notes.");	
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Meeting does not exist.", e.getMessage());
		}
	}

	@Test
	public void testAddMeetingNotesDateEx() {
		testManager.addNewContact("Boy", "Text."); // 1st contact therefore Id = 1
		int id = testManager.addFutureMeeting(testManager.getContacts(1), date);
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
		testManager.addNewContact("Girl", "Her notes."); // 1st contact therefore Id = 1
		testManager.addNewPastMeeting(testManager.getContacts(1), pastDate, "Some notes."); // 1st meeting therefore Id = 1
		String notes = null;
		try {
			testManager.addMeetingNotes(1, notes);	
			fail();
		}
		catch(NullPointerException e) {
			assertEquals("No notes given.", e.getMessage());
		}
	}

	@Test
	public void testGetFutureMeetingListByContact() {
		Calendar newDate = Calendar.getInstance();
		newDate.set(2013, 5, 4, 11, 0);

		testManager.addNewContact("Gill Gall", "Good girl"); // 1st contact therefore Id = 1
		testManager.addNewContact("Bob", "Good boy"); // 2nd contact therefore Id = 2
		Set<Contact> testContacts = testManager.getContacts(1);
		Iterator<Contact> iter = testContacts.iterator();
		Contact contact1 = iter.next();

		List<Meeting> emptyList = testManager.getFutureMeetingList(contact1);
		assertTrue(emptyList.isEmpty());

		int id1 = testManager.addFutureMeeting(testContacts, newDate);
		int id2 = testManager.addFutureMeeting(testManager.getContacts(1, 2), date);
		
		Meeting meeting1 = testManager.getMeeting(id1);
		Meeting meeting2 = testManager.getMeeting(id2);
		
		List<Meeting> outputList = testManager.getFutureMeetingList(contact1);
		assertEquals(outputList.get(0), meeting2);
		assertEquals(outputList.get(1), meeting1);
		assertEquals(outputList.size(), 2);
	}

	@Test
	public void testGetFutureMeetingListByContactEx() {
		Contact Bob = new ContactImpl("Bob", 2);
		try {
			List<Meeting> exList = testManager.getFutureMeetingList(Bob);
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
		}
	}

	@Test
	public void testGetFutureMeetingListByDate() {
		List<Meeting> emptyList = testManager.getFutureMeetingList(date);
		assertTrue(emptyList.isEmpty());

		Calendar earlierTime = Calendar.getInstance();
		earlierTime.set(2013, 4, 1, 10, 0);

		testManager.addNewContact("Fred List", "Long list"); // 1st contact therefore Id = 1
		Set<Contact> testContacts = testManager.getContacts(1);

		int id1 = testManager.addFutureMeeting(testContacts, date);
		int id2 = testManager.addFutureMeeting(testContacts, earlierTime);
		Meeting meeting1 = testManager.getMeeting(id1);
		Meeting meeting2 = testManager.getMeeting(id2);

		List<Meeting> outputList = testManager.getFutureMeetingList(date);
		assertEquals(outputList.get(0), meeting2);
		assertEquals(outputList.get(1), meeting1);
		assertEquals(outputList.size(), 2);

		Calendar shortDate = Calendar.getInstance();
		shortDate.set(2013, 4, 1);
		List<Meeting> sameList = testManager.getFutureMeetingList(shortDate);
		assertEquals(outputList, sameList);
	}

	@Test
	public void testGetFurtureMeetingListByPastDate() {		

		testManager.addNewContact("Fred List", "Long list"); // 1st contact therefore Id = 1
		Set<Contact> testContacts = testManager.getContacts(1);

		testManager.addNewPastMeeting(testContacts, pastDate, "Notes."); // 1st meeting therefore Id = 1
		Meeting pastMeeting = testManager.getMeeting(1);
		List<Meeting> pastMeetingList = testManager.getFutureMeetingList(pastDate);
		assertEquals(pastMeetingList.get(0), pastMeeting);
		assertEquals(pastMeetingList.size(), 1);
	}


	@Test
	public void testGetPastMeetingList() {
		Calendar newDate = Calendar.getInstance();
		newDate.set(2012, 11, 14, 11, 0);
		
		testManager.addNewContact("Mr fixit", "Fixes things"); // 1st contact therefore Id = 1
		testManager.addNewContact("Mrs fixit", "Also fixes things"); // 2nd contact therefore Id = 2
		Set<Contact> testContacts = testManager.getContacts(1);
		Iterator<Contact> iter = testContacts.iterator();
		Contact contact1 = iter.next();

		List<PastMeeting> emptyList = testManager.getPastMeetingList(contact1);
		assertTrue(emptyList.isEmpty());

		testManager.addNewPastMeeting(testContacts, pastDate, "Notes."); // 1st meeting therefore Id = 1
		testManager.addNewPastMeeting(testManager.getContacts(1, 2), newDate, "More notes."); // 2nd meeting therefore Id = 2
		Meeting meeting1 = testManager.getPastMeeting(1);
		Meeting meeting2 = testManager.getPastMeeting(2);
		
		List<PastMeeting> outputList = testManager.getPastMeetingList(contact1);
		assertEquals(outputList.get(0), meeting1);
		assertEquals(outputList.get(1), meeting2);
		assertEquals(outputList.size(), 2);
	}


	@Test
	public void testGetPastMeetingListByEx() {
		Contact Bob = new ContactImpl("Bob", 2);
		try {
			List<PastMeeting> exList = testManager.getPastMeetingList(Bob);
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
		}
	}

	@Test
	public void testConvertFutureToPastMeeting() {
		testManager.addNewContact("Rambo", "hello Rambo"); // 1st contact therefore Id = 1
		Set<Contact> testContacts = testManager.getContacts(1);
		int id = testManager.addFutureMeeting(testContacts, date); // 1st meeting therefore Id = 1
		
		((ContactManagerImpl) testManager).checkForMeetingHeld();
		PastMeeting meeting = testManager.getPastMeeting(1);
		assertEquals(meeting.getContacts(), testContacts);
	}

	
}