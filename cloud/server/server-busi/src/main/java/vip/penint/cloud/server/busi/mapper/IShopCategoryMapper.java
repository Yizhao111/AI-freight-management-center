package vip.penint.cloud.server.busi.mapper;

import vip.penint.cloud.server.busi.entity.ShopCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类目表 Mapper 接口
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface IShopCategoryMapper extends BaseMapper<ShopCategory> {

    List<Map<String, Object>> findCateGoods();

}
