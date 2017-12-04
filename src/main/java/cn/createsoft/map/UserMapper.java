package cn.createsoft.map;

import cn.createsoft.model.User;
import cn.createsoft.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends MyMapper<User>{

    User selectById(@Param("userId")int userId);

}
