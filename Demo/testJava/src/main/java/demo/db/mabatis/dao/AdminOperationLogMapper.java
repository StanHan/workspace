package demo.db.mabatis.dao;

import java.util.List;
import java.util.Map;

import demo.vo.AdminOperationLog;

public interface AdminOperationLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminOperationLog record);

    int insertSelective(AdminOperationLog record);

    AdminOperationLog selectByPrimaryKey(Long id);

    List<AdminOperationLog> selectByMap(Map<String, Object> map);

    int countByMap(Map<String, Object> map);

    int updateByPrimaryKeySelective(AdminOperationLog record);

    int updateByPrimaryKey(AdminOperationLog record);
}