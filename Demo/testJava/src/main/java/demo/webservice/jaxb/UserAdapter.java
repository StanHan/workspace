package demo.webservice.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UserAdapter extends XmlAdapter<UserImpl, User>{

	@Override
	public User unmarshal(UserImpl v) throws Exception {
		return v;
	}

	@Override
	public UserImpl marshal(User v) throws Exception {
		if(v instanceof UserImpl){
			return (UserImpl)v;
		}
		return new UserImpl(v.getName());
	}
}
