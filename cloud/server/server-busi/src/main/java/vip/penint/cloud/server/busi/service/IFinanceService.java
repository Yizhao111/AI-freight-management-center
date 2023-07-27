package vip.penint.cloud.server.busi.service;

import vip.penint.cloud.server.busi.entity.Finance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.cloud.common.core.entity.QueryRequest;

/**
 * <p>
 * 财务表 服务类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-19
 */
public interface IFinanceService extends IService<Finance> {

        /**
         * 分页查询财务表列表
         *
         * @param entity 财务表
         * @param queryRequest 封装的公共查询条件
         * @return 财务表集合
         */
        IPage<Finance> selectFinanceList(Finance entity, QueryRequest queryRequest);
}
