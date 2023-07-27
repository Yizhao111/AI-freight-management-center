package vip.penint.cloud.server.ques.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import vip.penint.cloud.server.ques.entity.Qu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目表 Mapper 接口
 * </p>
 *
 * @author Penint
 * @since 2022-11-03
 */
public interface IQuMapper extends BaseMapper<Qu> {

    List<Map<String, Object>> quCount();

    Integer selectListByDay(@Param("day") String day);
}
