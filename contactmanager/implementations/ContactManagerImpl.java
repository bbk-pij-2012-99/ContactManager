package contactmanager.implementations;

import contactmanager.interfaces.*;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
/**
* A class to manage your contacts and meetings.
*/
public class ContactManagerImpl implements ContactManager {
	private final String filename; 
	private Set<Contact> allContacts;
	private List<Meeting> allMeetings;

	public ContactManagerImpl(String filename) {
		this.filename = filename;
		if (new File(filename).exists()) {
			try(ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
				allMeetings = castReadObject(objectIn.readObject());
				allContacts = castReadObject(objectIn.readObject());
			}
			catch(ClassNotFoundException | IOException e) {
				System.err.println("Error in reading from file: " + e);
			}
		}
		else {
			allMeetings = new ArrayList<>();
			allContacts = new HashSet<>();
		}
	}

	public ContactManagerImpl() {
		this("./contacts.txt");
	}

	/**
	* Cast objects read in by ObjectInputStream
	* Suppresses unchecked cast warning
	*
	* @param obj the object read from file
	* @return the object cast to appropriate type
	*/
	@SuppressWarnings("unchecked")
	private static <T> T castReadObject(Object obj) {
  		
  		return (T) obj;
	}
	
	/**
	* Generate a unique ID for a new contact, based on the number of existing contacts
	*
	* @return the ID for a new contact
	*/
	private int generateContactId() {

		return allContacts.size() + 1;
	}
	
	/**
	* Generate a unique ID for a new meeting, based on the number of existing meetings
	*
	* @return the ID for a new meeting
	*/
	private int generateMeetingId() {
		
		return allMeetings.size() + 1;
	}
	
	/** 
	* Return TRUE if meeting occurs in the past.
	*
	* @param meeting a meeting to check whether in the past
	* @return true if meeting occurs in the past, false otherwise
	*/
	private boolean isPastMeeting(Meeting meeting) {
		Calendar currentDate = Calendar.getInstance();
		return meeting.getDate().before(currentDate);
	}
	
	/**
	* Returns true if the calendars represent the same date and false otherwise
	*
	* @param date1 the first calendar object to compare
	* @param date2 the second calendar object to compare
	* @return boolean logical of whether the dates are the same
	*/
	private boolean compareDates(Calendar date1, Calendar date2) {

		if (date1.get(date1.YEAR) == date2.get(date2.YEAR) && date1.get(date1.DAY_OF_YEAR) == date2.get(date2.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}
	
	/**
	* Create a new contact with the specified name and notes.
	*
	* @param name the name of the contact.
	* @param notes notes to be added about the contact.
	* @throws NullPointerException if the name or the notes are null
	*/
	public void addNewContact(String name, String notes){

		if(name == null) {
			throw new NullPointerException("No name given.");
		}
		if(notes == null) {
			throw new NullPointerException("No notes given.");
		}

		Contact contact = new ContactImpl(name, generateContactId());
		contact.addNotes(notes);
		allContacts.add(contact);
	}
	
	/**
	* Returns a list containing the contacts that correspond to the IDs.
	*
	* @param ids an arbitrary number of contact IDs
	* @return a list containing the contacts that correspond to the IDs.
	* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
	*/
	public Set<Contact> getContacts(int... ids){

		Set<Contact> contacts = new HashSet<>();
		for (int i : ids) {
			int exists = 0;
			for (Contact c : allContacts) {
				if (c.getId() == i) {
					contacts.add(c);
					exists = 1;
					break;
				}
			}
			if (exists == 0) {
				throw new IllegalArgumentException("Contact does not exist.");
			}
		}
		
		return contacts;
	}
	
	/**
	* Returns a list with the contacts whose name contains that string.
	*
	* @param name the string to search for
	* @return a list with the contacts whose name contains that string.
	* @throws NullPointerException if the parameter is null
	*/
	public Set<Contact> getContacts(String name){

		if (name == null) {
			throw new NullPointerException("No name given.");
		}

		Set<Contact> contacts = new HashSet<>();
		for (Contact c : allContacts) {
			if(c.getName().contains(name)) {
				contacts.add(c);
			}
		}
		return contacts;
	}
	
	/**
	* Add a new meeting to be held in the future.
	*
	* @param contacts a list of contacts that will participate in the meeting
	* @param date the date on which the meeting will take place
	* @return the ID for the meeting
	* @throws IllegalArgumentException if the meeting is set for a time in the past,
	* of if any contact is unknown / non-existent
	*/
	public int addFutureMeeting(Set<Contact> contacts, Calendar date){

		Calendar currentDate = Calendar.getInstance();
		if (date.before(currentDate)) {
			throw new IllegalArgumentException("Meeting occurs in the past.");
		}

		for (Contact c : contacts) {
			if (!allContacts.contains(c)) {
				throw new IllegalArgumentException("Contact does not exist.");
			}
		}

		int id = generateMeetingId();
		Meeting meeting = new MeetingImpl(id, date, contacts);
		allMeetings.add(meeting);
		return id;
	}
	
	/**
	* Create a new record for a meeting that took place in the past.
	*
	* @param contacts a list of participants
	* @param date the date on which the meeting took place
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the list of contacts is empty, 
	* or any of the contacts does not exist
	* @throws NullPointerException if any of the arguments is null
	*/
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){

		if (contacts == null) {
			throw new NullPointerException("No contacts given.");
		}
		if (date == null) {
			throw new NullPointerException("No date given.");
		}
		if (text == null) {
			throw new NullPointerException("No notes given.");
		}

		if (contacts.isEmpty()) {
			throw new IllegalArgumentException("No contacts given.");
		}

		for (Contact c : contacts) {
			if (!allContacts.contains(c)) {
				throw new IllegalArgumentException("Contact does not exist.");
			}
		}

		int id = generateMeetingId();
		Meeting meeting = new MeetingImpl(id, date, contacts, text);
		allMeetings.add(meeting);
	}
	
	/**
	* Add notes to a meeting.
	*
	* This method is used when a future meeting takes place, and is
	* then converted to a past meeting (with notes).
	*
	* It can be also used to add notes to a past meeting at a later date.
	*
	* @param id the ID of the meeting
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the meeting does not exist
	* @throws IllegalStateException if the meeting is set for a date in the future
	* @throws NullPointerException if the notes are null
	*/
	public void addMeetingNotes(int id, String text){

		if (text == null) {
			throw new NullPointerException("No notes given.");
		}
		
		Meeting meeting = getMeeting(id);

		if (meeting == null) {
			throw new IllegalArgumentException("Meeting does not exist.");
		}

		if (!isPastMeeting(meeting)) {
			throw new IllegalStateException("Meeting occurs in the future.");
		}

		((MeetingImpl) meeting).addNotes(text);
	}
	
	/**
	* Returns the meeting with the requested ID, or null if it there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	*/
	public Meeting getMeeting(int id){

		for (Meeting meeting : allMeetings) {
			if (meeting.getId() == id) {
				return meeting;
			}
		}
		
		return null;
	}
	
	/**
	* Returns the PAST meeting with the requested ID, or null if it there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the future
	*/
	public PastMeeting getPastMeeting(int id){

		Meeting meeting = getMeeting(id);
		if (meeting == null) {
			return null;
		}

		if (isPastMeeting(meeting)) {
			return (PastMeeting) meeting;
		}
		else {
			throw new IllegalArgumentException("Meeting occurs in the future.");
		}
	}
	
	/**
	* Returns the FUTURE meeting with the requested ID, or null if there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the past
	*/
	public FutureMeeting getFutureMeeting(int id){
		
		Meeting meeting = getMeeting(id);
		if (meeting == null) {
			return null;
		}

		if (isPastMeeting(meeting)) {
			throw new IllegalArgumentException("Meeting occurs in the past.");
		}
		else {
			return (FutureMeeting) meeting;
		}
	}
	
	/**
	* Returns the list of future meetings scheduled with this contact.
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param contact one of the user’s contacts
	* @return the list of future meeting(s) scheduled with this contact (maybe empty).
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<Meeting> getFutureMeetingList(Contact contact){
	
		// Checks contact exists	
		getContacts(contact.getId());

		List<Meeting> contactMeetings = new ArrayList<>();

		for (Meeting meeting : allMeetings) {
			if (meeting.getContacts().contains(contact) && !isPastMeeting(meeting)) {
				contactMeetings.add(meeting);	
			}
		}

		return contactMeetings;
	}
	
	/**
	* Returns the list of meetings that are scheduled for, or that took
	* place on, the specified date
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param date the date
	* @return the list of meetings
	*/
	public List<Meeting> getFutureMeetingList(Calendar date){

		List<Meeting> dateMeetings = new ArrayList<>();

		for (Meeting meeting : allMeetings) {
			if (compareDates(date, meeting.getDate())) {
				dateMeetings.add(meeting);
			}	
		}

		return dateMeetings;
	}
	
	/**
	* Returns the list of past meetings in which this contact has participated.
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param contact one of the user’s contacts
	* @return the list of future meeting(s) scheduled with this contact (maybe empty).
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<PastMeeting> getPastMeetingList(Contact contact){
		
		// Checks contact exists	
		getContacts(contact.getId());

		List<PastMeeting> contactMeetings = new ArrayList<>();

		for (Meeting meeting : allMeetings) {
				if (meeting.getContacts().contains(contact) && isPastMeeting(meeting)) {
					contactMeetings.add((PastMeeting) meeting);
				}	
		}
		
		return contactMeetings;
	}
	
	/**
	* Save all data to disk.
	*
	* This method must be executed when the program is
	* closed and when/if the user requests it.
	*/
	public void flush(){

		try(ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
			objectOut.writeObject(allMeetings);
			objectOut.writeObject(allContacts);		
		}
		catch(IOException e) {
			System.err.println("Error in writing to file: " + e);
		}
	}
}