package vip.penint.cloud.server.busi.service;

import vip.penint.cloud.server.busi.entity.Car;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.cloud.common.core.entity.QueryRequest;

/**
 * <p>
 * 车辆管理 服务类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface ICarService extends IService<Car> {

        /**
         * 分页查询车辆管理列表
         *
         * @param entity 车辆管理
         * @param queryRequest 封装的公共查询条件
         * @return 车辆管理集合
         */
        IPage<Car> selectCarList(Car entity, QueryRequest queryRequest);
}
