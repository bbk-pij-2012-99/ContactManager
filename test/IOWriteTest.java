package test;

import contactmanager.interfaces.*;
import contactmanager.implementations.*;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Iterator;

public class IOWriteTest {
	
	private ContactManager testManager;
	private Calendar futureDate;
	private Calendar pastDate;
	private Set<Contact> testContacts1;

	@Before
	public void buildUp() {
		testManager = new ContactManagerImpl();

		futureDate = Calendar.getInstance();
		futureDate.set(2013, 4, 1, 12, 30);
		pastDate = Calendar.getInstance();
		pastDate.set(2011, 1, 1, 10, 30);
		
		testManager.addNewContact("Gary", "Chess player"); // 1st contact therefore Id = 1
		testManager.addNewContact("Sarah smile", "notes and notes."); // 2nd contact therefore Id = 2

		testContacts1 = testManager.getContacts(1, 2);
		Set<Contact> testContacts2 = testManager.getContacts(2);

		testManager.addFutureMeeting(testContacts1, futureDate); // 1st meeting therefore Id = 1
		testManager.addNewPastMeeting(testContacts2, pastDate, "Notes from the meeting"); // 2nd meeting therefore Id = 2

		testManager.addMeetingNotes(2, "Notes I forgot the first time.");

	}

	@Test
	public void testFlush() throws ClassNotFoundException, FileNotFoundException {
		testManager.flush();

		String filename = "./contacts.txt";
		File file = new File(filename);
		List<Meeting> outputMeetings = null;
		Set<Contact> outputContacts = null;


		try(ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));) {
			outputMeetings = (ArrayList<Meeting>) objectIn.readObject();
			outputContacts = (HashSet<Contact>) objectIn.readObject();		
		}
		catch(IOException e) {

		}

		assertEquals(outputMeetings.size(),2);
		assertEquals(outputContacts.size(), 2);

		Iterator<Contact> iterRead = outputContacts.iterator();
		Contact readContact1 = iterRead.next();
		Contact readContact2 = iterRead.next();
		assertTrue(readContact1.getName().equals("Gary") || readContact1.getName().equals("Sarah smile"));
		assertTrue(readContact2.getName().equals("Gary") || readContact2.getName().equals("Sarah smile"));

		assertEquals(outputMeetings.get(0).getDate(), pastDate);
		assertEquals(outputMeetings.get(1).getDate(), futureDate);
		assertEquals(outputMeetings.get(0).getId(), 2);
		assertEquals(outputMeetings.get(1).getId(), 1);
	}

}