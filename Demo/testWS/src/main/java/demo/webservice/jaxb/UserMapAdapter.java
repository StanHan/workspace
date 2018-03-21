package demo.webservice.jaxb;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UserMapAdapter extends XmlAdapter<UserMap, Map<Integer,User>>{

	@Override
	public Map<Integer, User> unmarshal(UserMap v) throws Exception {
		Map<Integer, User> map = new LinkedHashMap<Integer, User>();
		for(UserMap.UserEntry e : v.getEntries()){
			map.put(e.getId(), e.getUser());
		}
		return map;
	}

	@Override
	public UserMap marshal(Map<Integer, User> v) throws Exception {
		UserMap map = new UserMap();
		for(Map.Entry<Integer, User> e : v.entrySet()){
			UserMap.UserEntry u = new UserMap.UserEntry();
			u.setId(e.getKey());
			u.setUser(e.getValue());
			map.getEntries().add(u);
		}
		return map;
	}
}
