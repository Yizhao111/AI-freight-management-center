package vip.penint.cloud.server.busi.service.impl;

import vip.penint.cloud.server.busi.entity.ShopGoods;
import vip.penint.cloud.server.busi.mapper.IShopGoodsMapper;
import vip.penint.cloud.server.busi.service.IShopGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.cloud.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
@Service
public class ShopGoodsServiceImpl extends ServiceImpl<IShopGoodsMapper, ShopGoods> implements IShopGoodsService {

        @Override
        public IPage<ShopGoods> selectShopGoodsList(ShopGoods entity, QueryRequest queryRequest) {
            IPage<ShopGoods> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
            LambdaQueryWrapper<ShopGoods> wrapper = new LambdaQueryWrapper<>();
            return baseMapper.selectPage(page, wrapper);
        }
}
