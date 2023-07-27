package vip.penint.cloud.auth.service;

import vip.penint.cloud.auth.entity.OauthClientDetails;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.exception.PenintException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface OauthClientDetailsService extends IService<OauthClientDetails> {

    /**
     * 查询（分页）
     *
     * @param request            QueryRequest
     * @param oauthClientDetails oauthClientDetails
     * @return IPage<OauthClientDetails>
     */
    IPage<OauthClientDetails> findOauthClientDetails(QueryRequest request, OauthClientDetails oauthClientDetails);

    /**
     * 根据主键查询
     *
     * @param clientId clientId
     * @return OauthClientDetails
     */
    OauthClientDetails findById(String clientId);

    /**
     * 新增
     *
     * @param oauthClientDetails oauthClientDetails
     * @throws PenintException PenintException
     */
    void createOauthClientDetails(OauthClientDetails oauthClientDetails) throws PenintException;

    /**
     * 修改
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void updateOauthClientDetails(OauthClientDetails oauthClientDetails);

    /**
     * 删除
     *
     * @param clientIds clientIds
     */
    void deleteOauthClientDetails(String[] clientIds);
}
