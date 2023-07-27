package vip.penint.cloud.server.busi.service.impl;

import vip.penint.cloud.server.busi.entity.Car;
import vip.penint.cloud.server.busi.mapper.ICarMapper;
import vip.penint.cloud.server.busi.service.ICarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.cloud.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 车辆管理 服务实现类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
@Service
public class CarServiceImpl extends ServiceImpl<ICarMapper, Car> implements ICarService {

        @Override
        public IPage<Car> selectCarList(Car entity, QueryRequest queryRequest) {
            IPage<Car> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
            LambdaQueryWrapper<Car> wrapper = new LambdaQueryWrapper<>();
            return baseMapper.selectPage(page, wrapper);
        }
}
