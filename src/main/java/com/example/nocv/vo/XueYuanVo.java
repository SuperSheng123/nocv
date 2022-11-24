package com.example.nocv.vo;

import com.example.nocv.entity.XueYuan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class XueYuanVo extends XueYuan {
    private Integer page ;
    private Integer limit ;
}
