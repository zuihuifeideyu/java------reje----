package com.itheima.reje.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;


// 员工实体类
@Data
// lombok提供的注解，用来简化get和set等函数的书写，通过Data注解就能表示这是一个实体类并自动生成
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;  // 身份证号码，驼峰命名法，对应的数据库名称为 id_number

    private Integer status;


    // @TableField时MP提供的注解，用于公共内容自动填充
    @TableField(fill = FieldFill.INSERT) // 插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
