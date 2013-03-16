import java.util.Iterator;
import java.util.Set;

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