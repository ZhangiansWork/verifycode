package cn.createsoft.map;

import cn.createsoft.model.User;
import cn.createsoft.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends MyMapper<User>{

    User selectById(@Param("userId")int userId);
    List<User> selectByPhoneNum(@Param("phoneNum")String phoneNum);
}
