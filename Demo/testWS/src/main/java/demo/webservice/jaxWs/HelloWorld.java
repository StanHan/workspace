package demo.webservice.jaxWs;

import java.util.Map;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService()
public interface HelloWorld {

	public Map<String,String> sayHi(@WebParam(name="map") Map<String,String> map);
}
