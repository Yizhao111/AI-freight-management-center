package vip.penint.cloud.server.busi.service;

import vip.penint.cloud.server.busi.entity.ShopGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.cloud.common.core.entity.QueryRequest;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface IShopGoodsService extends IService<ShopGoods> {

        /**
         * 分页查询商品表列表
         *
         * @param entity 商品表
         * @param queryRequest 封装的公共查询条件
         * @return 商品表集合
         */
        IPage<ShopGoods> selectShopGoodsList(ShopGoods entity, QueryRequest queryRequest);
}
