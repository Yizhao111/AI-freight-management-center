package vip.penint.cloud.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
//import com.yuanzhankeji.oa.business.hr.entity.UserInfo;
//import com.yuanzhankeji.oa.common.entity.common.QueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import vip.penint.cloud.common.core.entity.QueryRequest;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser extends QueryRequest {


    /**
     * 用户状态：有效
     */
    public static final Integer STATUS_VALID = 1;
    /**
     * 用户状态：锁定
     */
    public static final Integer STATUS_LOCK = 0;
    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "default.jpg";
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "1234qwer";
    /**
     * 性别男
     */
    public static final Integer SEX_MALE = 0;
    /**
     * 性别女
     */
    public static final Integer SEX_FEMALE = 1;
    /**
     * 性别保密
     */
    public static final Integer SEX_UNKNOW = 2;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称/真实姓名
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 最近访问时间
     */
    private String lastLoginIp;

    /**
     * 最近访问时间
     */
    private Date lastLoginTime;

    /**
     * 性别 0男 1女 2保密
     */
    private Integer ssex;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 状态 0锁定 1有效,默认为1
     */
    private Integer status;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField(exist = false)
    private List<SysRole> roles;
    @TableField(exist = false)
    private String roleId;
    @TableField(exist = false)
    private String roleName;
    @TableField(exist = false)
    private Integer[] roleIds;
    @TableField(exist = false)
    private String postId;
    @TableField(exist = false)
    private String postName;
    @TableField(exist = false)
    private Integer[] postIds;
}
