package com.example.logindemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户表")
public class User {
    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Schema(description = "电话号")
    private String phone;
    @Schema(description="用户昵称")
    private String nickname;
    @Schema(description="创建时间")
    private LocalDateTime createTime;
    @Schema(description="更新时间")
    private LocalDateTime updateTime;
    @Schema(description="状态")
    private Integer status;
}
