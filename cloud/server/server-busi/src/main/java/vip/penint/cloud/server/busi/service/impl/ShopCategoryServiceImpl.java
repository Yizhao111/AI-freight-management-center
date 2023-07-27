package vip.penint.cloud.server.busi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.busi.entity.ShopCategory;
import vip.penint.cloud.server.busi.mapper.IShopCategoryMapper;
import vip.penint.cloud.server.busi.service.IShopCategoryService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
@Service
public class ShopCategoryServiceImpl extends ServiceImpl<IShopCategoryMapper, ShopCategory> implements IShopCategoryService {

    @Override
    public List<ShopCategory> selectShopCategoryList(ShopCategory entity) {
        LambdaQueryWrapper<ShopCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopCategory::getLevel, "l1")
                .eq(StringUtils.isNotNull(entity.getId()), ShopCategory::getId, entity.getId())
                .eq(StringUtils.isNotBlank(entity.getName()), ShopCategory::getName, entity.getName());
        List<ShopCategory> shopCategories = baseMapper.selectList(wrapper);
        for (ShopCategory shopCategory : shopCategories) {
            LambdaQueryWrapper<ShopCategory> wra = new LambdaQueryWrapper<>();
            wra.eq(ShopCategory::getPid, shopCategory.getId());
            shopCategory.setChildren(baseMapper.selectList(wra));
        }
        return shopCategories;
    }

    @Override
    public List<ShopCategory> selectL1() {
        LambdaQueryWrapper<ShopCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopCategory::getLevel, "l1");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> findCateGoods() {
        return baseMapper.findCateGoods();
    }
}
