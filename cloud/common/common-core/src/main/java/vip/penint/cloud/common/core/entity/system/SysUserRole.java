package vip.penint.cloud.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author penint
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Integer roleId;


}
