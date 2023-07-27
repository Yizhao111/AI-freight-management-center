package vip.penint.cloud.common.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import vip.penint.cloud.common.core.entity.system.SysRole;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *  当前登陆用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements Serializable {

    private static long serialVersionUID = 761748087824726463L;

    @JsonIgnore
    private String password;
    private String username;
    @JsonIgnore
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Long userId;
    private String avatar;
    private String email;
    private String mobile;
    private Integer ssex;
    private Integer deptId;
    private String deptName;
    private String roleId;
    private String roleName;
    @JsonIgnore
    private Date lastLoginTime;
    private String description;
    private String status;
    private String deptIds;
    private List<SysRole> roles;
}
