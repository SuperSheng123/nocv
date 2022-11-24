package com.example.nocv.vo;

import com.example.nocv.entity.BanJi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BanJiVo extends BanJi {
    private Integer page ;
    private Integer limit ;
}
