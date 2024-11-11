package z.han.userService.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import z.han.userService.model.User;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (username, password, timezone, email ) VALUES (#{user.username}, #{user.password}, " +
            "#{user.timezone}, #{user.email})")
    void insertUser(User user);

    @Select("SELECT * FROM users where username = #{username} LIMIT 1")
    User findUserByUsername(String username);

    User modifyUser(User user);

    @Select("SELECT * FROM users WHERE id = #{userId} LIMIT 1")
    User findUserByUserId(Integer userId);
}
