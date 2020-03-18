package com.taylor.hadoop_springboot.mapper;

import com.taylor.hadoop_springboot.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * InnoDB free: 3072 kB Mapper 接口
 * </p>
 *
 * @author taylor
 * @since 2020-03-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
