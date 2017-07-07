package demo.webservice.jaxWs;

import java.util.Map;
import java.util.Map.Entry;

public class HelloWorldImpl implements HelloWorld {

	@Override
	public Map<String, String> sayHi(Map<String, String> map) {
		for(Entry<String, String> entry:map.entrySet()){
			map.put(entry.getKey(), "hello " + entry.getValue());
		}
		return map;
	}

}
