import contactmanager.implementations.ContactManagerImpl;
import contactmanager.interfaces.ContactManager;

public class ContactManagerApp {

	public static void main(String[] args) {
		launch();
	}

	private static void launch() {
		ContactManager manager = new ContactManagerImpl("./test-contacts.txt");

		// Add contacts and meetings....

		manager.flush();
	}

}