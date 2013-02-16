/**
* A contact is a person we are making business with or may do in the future.
*
* Contacts have an ID (unique), a name (probably unique, but maybe
* not), and notes that the user may want to save about them.
*/
public class ContactImpl implements Contact {
	private String name;
	private int id;
	private String notes;


	public ContactImpl(String name, int id) {
		this.name = name;
		this.id = id;
		notes = "";
	}

	/**
	* Returns the ID of the contact.
	*
	* @return the ID of the contact.
	*/
	public int getId(){
		return id;

	}
	/**
	* Returns the name of the contact.
	*
	* @return the name of the contact.
	*/
	public String getName(){
		return name;

	}
	/**
	* Returns our notes about the contact, if any.
	*
	* If we have not written anything about the contact, the empty
	* string is returned.
	*
	* @return a string with notes about the contact, maybe empty.
	*/
	public String getNotes(){
		return notes;

	}
	/**
	* Add notes about the contact.
	*
	* @param note the notes to be added
	*/
	public void addNotes(String note){
		notes = notes + "\n" + note;
	}
}