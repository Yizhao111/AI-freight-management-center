package vip.penint.cloud.server.busi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.server.busi.entity.ShopCategory;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类目表 服务类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface IShopCategoryService extends IService<ShopCategory> {

    /**
     * 分页查询类目表列表
     *
     * @param entity 类目表
     * @return 类目表集合
     */
    List<ShopCategory> selectShopCategoryList(ShopCategory entity);

    List<ShopCategory> selectL1();

    List<Map<String,Object>> findCateGoods();

}
