package vip.penint.cloud.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_custom_user")
public class CustomUser implements Serializable {

    private static final long serialVersionUID = -3890998115990166651L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String companyName;
    private String phone;
    private String tel;
    private String idCard;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
