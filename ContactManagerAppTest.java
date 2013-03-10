import java.util.Calendar;

public class ContactManagerAppTest {

	private ContactManager testManager;
	private Calendar date;
	private Calendar pastDate;

	@Before
	public void buildUp() {
		testManager = new ContactManagerImpl();
		date = Calendar.getInstance();
		date.set(2013, 4, 1, 12, 30);
		pastDate = Calendar.getInstance();
		pastDate.set(2011, 1, 1, 10, 30);
	}

	@After
	public void afterTest() {
		testManager.flush();
		testManager = null;
	}

	@Test
	public void testAddNewContactGetContactByName() {

		testManager.addNewContact("Jed Richards", "Flash developer."); // 1st contact therefore Id = 1 
		testManager.addNewContact("Sarah Small", "Princess."); // Id = 2
		Set<Contact> output = testManager.getContacts("Jed");
		assertEquals(output.size(), 1);
		for (Contact c : output) {
			assertEquals(c.getId(), 1);	
		}
		
	}

	@Test
	public void testGetContactByNameMultiple() {
		testManager.addNewContact("Fred Smith", "Notes."); // Id = 3
		testManager.addNewContact("John Jones", "More notes."); // Id = 4
		testManager.addNewContact("Mary Smith", "Notes."); // Id = 5
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
		Set<Contact> output = testManager.getContacts(1, 3);
		assertEquals(output.size(), 2);
		Iterator<Contact> iter = output.iterator();
		String firstName = iter.next().getName();
		String secondName = iter.next().getName();
		if (firstName.equals("Jed Richards")) {
			assertEquals(secondName, "Fred Smith");
		}
		else if (firstName.equals("Fred Smith")) {
			assertEquals(secondName, "Jed Richards");
		}
		else {
			fail();
		}
	}

	@Test
	public void testGetContactsByIdEx() {
		try {
			Set<Contact> output = testManager.getContacts(1, 6);
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
		}
	}

	@Test
	public void testAddFutureMeetingId() {
		testManager.addNewContact("Jim", "notes."); // Id = 6
		int id = testManager.addFutureMeeting(testManager.getContacts(6), date); // Id = 1
		assertEquals(id, 1);
	}
	
	@Test
	public void testAddFutureMeetingDate() {
		Set<Contact> contacts = testManager.getContacts(1);
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
		Contact Bob = new ContactImpl("Bob", 14);
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
		Contact Bob = new ContactImpl("Bob", 17);
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
		Set<Contact> testContacts = testManager.getContacts(2);
		testManager.addNewPastMeeting(testContacts, pastDate, "Meeting notes."); // Id = 2
		PastMeeting pastMeeting = testManager.getPastMeeting(2);
		Calendar outputDate = pastMeeting.getDate();
		assertEquals(outputDate, pastDate);
		Set<Contact> outputContacts = pastMeeting.getContacts();
		assertEquals(outputContacts, testContacts);
	}


	@Test
	public void testGetMeeting() {
		Set<Contact> testContacts = testManager.getContacts(4);
		testManager.addNewPastMeeting(testContacts, pastDate, "Notes."); // Id = 3
		int id = testManager.addFutureMeeting(testContacts, date); // Id = 4 
		Meeting pastMeeting = testManager.getMeeting(3);
		assertEquals(pastMeeting.getId(), 3);
		Meeting futureMeeting = testManager.getMeeting(id);
		assertEquals(futureMeeting.getId(), id);
		Meeting nullMeeting = testManager.getMeeting(12);
		assertNull(nullMeeting);
	}

	@Test
	public void testGetPastMeetingDateEx() {
		try {
			PastMeeting pastMeeting = testManager.getPastMeeting(1);	
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
		Calendar date = Calendar.getInstance();
		date.set(2013, 5, 1, 12, 30);
		testManager.addNewContact("Sally", "Her notes."); // Id = 7
		int id = testManager.addFutureMeeting(testManager.getContacts(7), date); // Id = 5
		FutureMeeting futureMeeting = testManager.getFutureMeeting(id);
		assertEquals(futureMeeting.getDate(), date);
	}

	@Test
	public void testGetFutureMeetingDateEx() {
		try {
			FutureMeeting futureMeeting = testManager.getFutureMeeting(3);	
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
		testManager.addMeetingNotes(3, "More notes.");
		String output = testManager.getPastMeeting(3).getNotes();
		assertEquals("\nMore notes.", output);
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
		try {
			testManager.addMeetingNotes(1, "Notes.");
			fail();
		}
		catch(IllegalStateException e) {
			assertEquals("Meeting occurs in the future.", e.getMessage());
		}
	}

	@Test
	public void testAddMeetingNotesEx() {
		String notes = null;
		try {
			testManager.addMeetingNotes(3, notes);	
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

		testManager.addNewContact("Gill Gall", "Good girl"); // Id = 8
		testManager.addNewContact("Bob", "Good boy"); // Id = 9
		Set<Contact> testContacts = testManager.getContacts(8);
		Iterator<Contact> iter = testContacts.iterator();
		Contact contact1 = iter.next();

		List<Meeting> emptyList = testManager.getFutureMeetingList(contact1);
		assertTrue(emptyList.isEmpty());

		int id1 = testManager.addFutureMeeting(testContacts, newDate); // Id = 6
		int id2 = testManager.addFutureMeeting(testManager.getContacts(8, 9), date); // Id = 7
		
		Meeting meeting1 = testManager.getMeeting(id1);
		Meeting meeting2 = testManager.getMeeting(id2);
		
		List<Meeting> outputList = testManager.getFutureMeetingList(contact1);
		assertEquals(outputList.get(0), meeting2);
		assertEquals(outputList.get(1), meeting1);
		assertEquals(outputList.size(), 2);
	}

	@Test
	public void testGetFutureMeetingListByContactEx() {
		Contact Bob = new ContactImpl("Bob", 42);
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
		Calendar newDate = Calendar.getInstance();
		newDate.set(2014, 4, 4, 9, 30);
		List<Meeting> emptyList = testManager.getFutureMeetingList(newDate);
		assertTrue(emptyList.isEmpty());

		Calendar earlierTime = Calendar.getInstance();
		earlierTime.set(2013, 4, 1, 10, 0);

		testManager.addNewContact("Fred List", "Long list"); // Id = 10
		Set<Contact> testContacts = testManager.getContacts(10);

		int id1 = testManager.addFutureMeeting(testContacts, date); // Id = 8
		int id2 = testManager.addFutureMeeting(testContacts, earlierTime); // Id = 9
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

		Set<Contact> testContacts = testManager.getContacts(10);

		Meeting pastMeeting = testManager.getMeeting(3);
		List<Meeting> pastMeetingList = testManager.getFutureMeetingList(pastDate);
		assertEquals(pastMeetingList.get(0), pastMeeting);
		assertEquals(pastMeetingList.size(), 1);
	}


	@Test
	public void testGetPastMeetingList() {
		Calendar newDate = Calendar.getInstance();
		newDate.set(2012, 11, 14, 11, 0);
		
		testManager.addNewContact("Mr fixit", "Fixes things"); // Id = 11
		testManager.addNewContact("Mrs fixit", "Also fixes things"); // Id = 12
		Set<Contact> testContacts = testManager.getContacts(11);
		Iterator<Contact> iter = testContacts.iterator();
		Contact contact1 = iter.next();

		List<PastMeeting> emptyList = testManager.getPastMeetingList(contact1);
		assertTrue(emptyList.isEmpty());

		testManager.addNewPastMeeting(testContacts, pastDate, "Notes."); // Id = 10
		testManager.addNewPastMeeting(testManager.getContacts(11, 12), newDate, "More notes."); // Id = 11
		Meeting meeting1 = testManager.getPastMeeting(10);
		Meeting meeting2 = testManager.getPastMeeting(11);
		
		List<PastMeeting> outputList = testManager.getPastMeetingList(contact1);
		assertEquals(outputList.get(0), meeting1);
		assertEquals(outputList.get(1), meeting2);
		assertEquals(outputList.size(), 2);
	}


	@Test
	public void testGetPastMeetingListByEx() {
		Contact Bob = new ContactImpl("Bob", 29);
		try {
			List<PastMeeting> exList = testManager.getPastMeetingList(Bob);
			fail();
		}
		catch(IllegalArgumentException e) {
			assertEquals("Contact does not exist.", e.getMessage());
		}
	}
	
}