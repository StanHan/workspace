package demo.webservice.cxf.demo4;

import javax.jws.WebService;

@WebService
public class ServiceImpl implements IService {

	@Override
	public void save(UserInfoDTO userInfoDto, boolean flag) {
		System.out.println("method save()"+userInfoDto.getName());

	}

	@Override
	public void delete(int id) {
		System.out.println("delete id is "+ id);

	}

	@Override
	public void update(String xml) {
		System.out.println("update "+xml);

	}

	@Override
	public String get(int id) {
		return "Stan "+ id;
	}

}
