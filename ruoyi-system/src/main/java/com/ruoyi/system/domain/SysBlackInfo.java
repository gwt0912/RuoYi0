package com.ruoyi.system.domain;


import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysBlackInfo  {

    private static final long serialVersionUID = 1L;

    /** 追逃人员序号ID */
    @Excel(name = "追逃人员序号", cellType = Excel.ColumnType.NUMERIC, prompt = "人员编号")
    @TableId
    private Long bId;

    /** 人员名称 */
    @Excel(name = "姓名")
    private String xm;

    /** 人员性别 */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String sfzhm;

    /** 人员状态（0正常 1列控） */
    @Excel(name = "列控状态", readConverterExp = "0=正常,1=列控")
    private String lkzt;

    /** 人员状态（0正常 1管控） */
    @Excel(name = "管控状态", readConverterExp = "0=正常,1=管控")
    private String gkzt;

    /** 人员状态（0正常 1重点人员） */
    @Excel(name = "人员状态", readConverterExp = "0=正常,1=重点人员")
    private String status;

    @DateTimeFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private Date beginTime;

    @DateTimeFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private Date endTime;

}
