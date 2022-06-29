package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysBlack;
import com.ruoyi.system.domain.SysBlackInfo;
import com.ruoyi.system.domain.SysUserRole;

import java.util.List;

/**
 * 追逃人员 业务层
 * 
 * @author ruoyi
 */
public interface ISysBlackService
{
    /**
     * 根据条件分页查询追逃人员列表
     * 
     * @param blackInfo 追逃人员信息
     * @return 追逃人员信息集合信息
     */
    public List<SysBlack> selectBlackList(SysBlackInfo blackInfo);

    /**
     * 通过姓名查询用户
     * 
     * @param Sfzhm 身份证号码
     * @return 追逃人员对象信息
     */
    public SysBlack selectBlackBySfzhm(String Sfzhm);

    /**
     * 通过人员ID查询用户
     * 
     * @param bId 人员ID
     * @return 追逃人员对象信息
     */
    public SysBlack selectBlackById(Long bId);



    /**
     * 通过人员ID删除用户
     * 
     * @param bId 人员ID
     * @return 结果
     */
    public int deleteBlackById(Long bId);

    /**
     * 批量删除人员信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteBlackByIds(String ids);

    /**
     * 保存人员信息
     * 
     * @param black 人员信息
     * @return 结果
     */
    public int insertBlack(SysBlack black);



    /**
     * 修改人员信息
     * 
     * @param black 人员信息
     * @return 结果
     */
    public int updateBalck(SysBlack black);



    /**
     * 校验shzhm是否唯一
     * 
     * @param sfzhm 身份证号码
     * @return 结果
     */
    public String checkSfzhmUnique(String sfzhm);


    /**
     * 导入人员数据
     * 
     * @param blackList 人员数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importBlack(List<SysBlack> blackList, Boolean isUpdateSupport, String operName);

    /**
     * 人员状态修改
     * 
     * @param black 人员信息
     * @return 结果
     */
    public int changeStatus(SysBlack black);
}
