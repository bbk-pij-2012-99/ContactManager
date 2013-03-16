package test;

import contactmanager.interfaces.*;
import contactmanager.implementations.*;

import org.junit.*;
import static org.junit.Assert.*;

public class ContactTest {

	private Contact testContact;

	@Before
	public void buildUp() {
		testContact = new ContactImpl("Jane Smith", 10);
	}


	@Test
	public void testContactAccessors() {
		int id = testContact.getId();
		assertEquals(id, 10);
		String name = testContact.getName();
		assertEquals(name, "Jane Smith");
	}

	@Test
	public void testContactNotes() {
		testContact.addNotes("Test notes.");
		testContact.addNotes("More notes.");
		String expected = "\nTest notes.\nMore notes.";
		String output = testContact.getNotes();
		assertEquals(output, expected);
	}

}