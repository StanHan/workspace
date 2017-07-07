package demo.javax.servlet.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ContactsServlet extends HttpServlet {

	protected ActionFactory factory = new ActionFactory();

	public ContactsServlet() {
		super();
	}

	protected String getActionName(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.substring(1, path.lastIndexOf("."));
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionName = getActionName(request);
		Action action = factory.create(actionName);
		String url = action.perform(request, response);
		if (url != null) {
			getServletContext().getRequestDispatcher(url).forward(request, response);
		}
		// response.sendRedirect("http://...");
	}
}

class ActionFactory {
	protected Map<String, Class> map = defaultMap();

	public ActionFactory() {
		super();
	}

	public Action create(String actionName) {
		Class klass = map.get(actionName);
		if (klass == null) {
			throw new RuntimeException(getClass() + " was unable to find an action named '" + actionName + "'.");
		}

		Action actionInstance = null;
		try {
			actionInstance = (Action) klass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return actionInstance;
	}

	protected Map<String, Class> defaultMap() {
		Map<String, Class> map = new HashMap<String, Class>();

		map.put("index", BootstrapAction.class);
		map.put("addContactAction", AddContactAction.class);
		map.put("removeContactAction", RemoveContactAction.class);

		return map;
	}
}

interface Action {
	public String perform(HttpServletRequest request, HttpServletResponse response);

	public void writeToResponseStream(HttpServletResponse response, String output);
}

abstract class ContactsAction implements Action {

	@Override
	public abstract String perform(HttpServletRequest request, HttpServletResponse response);

	@Override
	public void writeToResponseStream(HttpServletResponse response, String output) {

	}

}

class BootstrapAction extends ContactsAction {
	public String perform(HttpServletRequest request, HttpServletResponse response) {
		return "/" + "contactList.jsp";
	}
}

class AddContactAction extends ContactsAction {

	public String perform(HttpServletRequest request, HttpServletResponse response) {
		Contact newContact = createContact(request);

		HttpSession session = request.getSession();
		ContactList contacts = (ContactList) session.getAttribute("contacts");
		contacts.addContact(newContact);
		session.setAttribute("contacts", contacts);

		return "/contactList.jsp";
	}

	protected Contact createContact(HttpServletRequest request) {
		Contact contact = new Contact();
		contact.setFirstname(request.getParameter(RequestParameters.FIRSTNAME));
		contact.setLastname(request.getParameter(RequestParameters.LASTNAME));
		contact.setStreet(request.getParameter(RequestParameters.STREET));
		contact.setCity(request.getParameter(RequestParameters.CITY));
		contact.setState(request.getParameter(RequestParameters.STATE));
		contact.setZip(request.getParameter(RequestParameters.ZIP));
		contact.setType(request.getParameter(RequestParameters.TYPE));

		return contact;
	}
}

class ContactList{
	
	
	private List<Contact> contactList = new ArrayList<Contact>();
	private List<Contact> contactList2 = new LinkedList<Contact>();
	
	public synchronized void addContact(Contact contact){
		contactList.add(contact);
	}
	
	public synchronized void removeContact(int contactId){
		for (int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);
			if(contact.getId() == contactId){
				contactList.remove(i);
			}
		}
	}
}

class RequestParameters {
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String STREET = "street";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String ZIP = "zip";
	public static final String TYPE = "type";
}

class Contact {
	private int id;
	private String firstname;
	private String lastname;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

class RemoveContactAction extends ContactsAction {

	public String perform(HttpServletRequest request, HttpServletResponse response) {
		int contactId = Integer.parseInt(request.getParameter("id"));

		HttpSession session = request.getSession();
		ContactList contacts = (ContactList) session.getAttribute("contacts");
		contacts.removeContact(contactId);
		session.setAttribute("contacts", contacts);

		return "/contactList.jsp";
	}
}

class ContactsUser {

    protected String username = "";
    protected String password = "";
    protected List<Contact> contactList = new ArrayList<Contact>();

    public ContactsUser() {
    }

    public ContactsUser(String username, String password, List<Contact> contactList) {
         this.username = username;
         this.password = password;
         this.contactList.addAll(contactList);
    }

    public boolean hasContacts() {
         return !contactList.isEmpty();
    }

    public void addContact(Contact aContact) {
         contactList.add(aContact);
    }

    public void removeContact(Contact aContact) {
         contactList.remove(aContact);
    }

    public void removeContact(int id) {
         Contact toRemove = findContact(id);
         contactList.remove(toRemove);
    }

    protected Contact findContact(int id) {
         Contact found = null;

         Iterator<Contact> iterator = contactList.iterator();
         while (iterator.hasNext()) {
              Contact current = (Contact) iterator.next();
              if (current.getId() == id){
            	  found = current;
              }
                   
         }
         return found;
    }

//    accessors...
}

class LoginAction implements Action {
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	public String perform(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);

		ContactsUser user = UserDatabase.getSingleton().get(username, password);
		if (user != null) {
			ContactsUser contactsUser = (ContactsUser) user;
			request.getSession().setAttribute("user", contactsUser);
			return "/contactList.jsp";
		} else
			request.getSession().setAttribute("errorMessage", "Invalid username/password.");
		return "/error.jsp";
	}

	@Override
	public void writeToResponseStream(HttpServletResponse response, String output) {
		// TODO Auto-generated method stub

	}

}

class LogoutAction implements Action {

	public String perform(HttpServletRequest request, HttpServletResponse response) {
		UserDatabase.getSingleton().shutDown();
		return "/goodbye.jsp";
	}

	@Override
	public void writeToResponseStream(HttpServletResponse response, String output) {
		// TODO Auto-generated method stub

	}

}