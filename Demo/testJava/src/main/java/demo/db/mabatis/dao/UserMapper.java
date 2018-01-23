package demo.db.mabatis.dao;

import java.util.List;
import java.util.Map;

import demo.vo.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    
    List<User> selectByMap(Map<String, Object> map);

    int countByMap(Map<String, Object> map);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}