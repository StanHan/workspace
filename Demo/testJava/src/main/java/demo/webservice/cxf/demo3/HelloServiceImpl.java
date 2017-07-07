package demo.webservice.cxf.demo3;

import javax.jws.WebService;

@WebService
public class HelloServiceImpl implements HelloService {

	@Override
	public void save(Customer c1, Customer c2) {
		System.out.println(c1.getAge()+"_________"+c2.getAge());
		System.out.println(c1.getName()+"_________"+c2.getName());

	}

	@Override
	public void test(String args) {
		System.out.println(args);

	}

	@Override
	public Customer get(int id) {
		Customer customer = new Customer();
		customer.setAge(id);
		customer.setName("Stan");
		return customer;
	}

}
