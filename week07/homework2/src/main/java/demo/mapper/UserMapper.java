package demo.mapper;

import demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Created by diandian
 * @date 2021/8/8.
 */
@Mapper
public interface UserMapper {

    @Select("select id,name from user where id=#{id}")
    User findById(@Param("fid") int id);

    @Insert("insert into user (id, name) values (#{id}, #{name})")
    Integer add(@Param("id") int id, @Param("name") String name);
}
