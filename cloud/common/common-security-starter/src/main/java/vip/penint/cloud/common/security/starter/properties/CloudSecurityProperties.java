package vip.penint.cloud.common.security.starter.properties;

import vip.penint.cloud.common.core.entity.constant.EndpointConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "penint.cloud.security")
public class CloudSecurityProperties {

    /**
     * 是否开启安全配置
     */
    private Boolean enable;
    /**
     * 配置需要认证的uri，默认为所有/**
     */
    private String authUri = EndpointConstant.ALL;
    /**
     * 免认证资源路径，支持通配符
     * 多个值时使用逗号分隔
     */
    private String anonUris;
    /**
     * 是否只能通过网关获取资源
     */
    private Boolean onlyFetchByGateway = Boolean.TRUE;

    /**
     * scopeUri 访问权限范围，针对某些特殊的情况处理，例如多用户类型登录小程序、app接口
     */
    private String scopeUris;

    private String scopeName;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getAnonUris() {
        return anonUris;
    }

    public void setAnonUris(String anonUris) {
        this.anonUris = anonUris;
    }

    public Boolean getOnlyFetchByGateway() {
        return onlyFetchByGateway;
    }

    public void setOnlyFetchByGateway(Boolean onlyFetchByGateway) {
        this.onlyFetchByGateway = onlyFetchByGateway;
    }

    public String getScopeUris() {
        return scopeUris;
    }

    public void setScopeUris(String scopeUris) {
        this.scopeUris = scopeUris;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    @Override
    public String toString() {
        return "CloudSecurityProperties{" +
                "enable=" + enable +
                ", authUri='" + authUri + '\'' +
                ", anonUris='" + anonUris + '\'' +
                ", onlyFetchByGateway=" + onlyFetchByGateway +
                '}';
    }
}
