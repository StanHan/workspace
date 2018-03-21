package demo.webservice.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlType(name="UserMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserMap {
	
	@XmlElement(nillable=false,name="entry")
	List<UserEntry> entries = new ArrayList<UserEntry>();
	
	public List<UserEntry> getEntries() {
		return entries;
	}
	public void addEntry(UserEntry entry){
		entries.add(entry);
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name="UserEntry")
	static class UserEntry{
		public UserEntry(){
			super();
		}
		
		public UserEntry(Map.Entry<Integer, User> entry){
			super();
			this.id = entry.getKey();
			this.user = entry.getValue();
		}
		public UserEntry(Integer id,User user){
			super();
			this.id = id;
			this.user = user;
		}
		@XmlElement(required=true,nillable=false)
		Integer id;
		User user;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
	}
}
