package demo.webservice.cxf.demo3;

import javax.jws.WebService;

@WebService
public interface HelloService {

	public void save(Customer c1,Customer c2);
	
	public void test(String args);
	
	public Customer get(int id);
}
