package demo.webservice.cxf.demo4;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface IService {

	public void save(@WebParam(name="dto") UserInfoDTO userInfoDto,@WebParam(name="flag")boolean flag);
	
	public void delete(@WebParam(name="id") int id);
	
	public void update(@WebParam(name="info") String xml);
	
	public @WebResult(name="String") String get(@WebParam(name="id") int id);
}
