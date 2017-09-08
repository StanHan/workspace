package demo.db.mabatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.db.mabatis.dao.AdminOperationLogMapper;
import demo.vo.AdminOperationLog;

@Service
public class ServiceA {

    @Autowired
    private AdminOperationLogMapper mapper;
    
    public AdminOperationLog selectAdminOperationLog(){
        AdminOperationLog log = mapper.selectByPrimaryKey(1L);
        System.err.println(log);
        return log;
    }
}
