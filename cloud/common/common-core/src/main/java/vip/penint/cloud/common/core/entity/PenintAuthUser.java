package vip.penint.cloud.common.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import vip.penint.cloud.common.core.entity.system.SysRole;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 
 */
@Data
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
public class PenintAuthUser extends User {

    private static final long serialVersionUID = -6411066541689297219L;

    private Long userId;

    private String avatar;

    private String email;

    private String mobile;

    private Integer ssex;

    private Integer deptId;

    private String deptName;

    private String roleId;

    private String roleName;

    private Date lastLoginTime;

    private String description;

    private Integer status;

    private String deptIds;

    private List<SysRole> roles;

    public PenintAuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public PenintAuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
