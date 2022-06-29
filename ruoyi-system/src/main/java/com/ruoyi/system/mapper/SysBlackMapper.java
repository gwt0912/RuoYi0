package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.config.mybatis.mapper.BaseMapperX;
import com.ruoyi.common.config.mybatis.query.LambdaQueryWrapperX;
import com.ruoyi.system.domain.SysBlack;
import com.ruoyi.system.domain.SysBlackInfo;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表 数据层
 * 
 * @author ruoyi
 */

@Mapper
public interface SysBlackMapper extends BaseMapperX<SysBlack>
{

    default SysBlack selectBySfzhm(String sfzhm) {
        return selectOne(new LambdaQueryWrapperX<SysBlack>().eq(SysBlack::getSfzhm, sfzhm));
    }

    default List<SysBlack> selectBlackList(SysBlackInfo blackInfo) {
        return selectList(new LambdaQueryWrapperX<SysBlack>()
                .likeIfPresent(SysBlack::getXm, blackInfo.getXm())
                .likeIfPresent(SysBlack::getSfzhm, blackInfo.getSfzhm())
                .eqIfPresent(SysBlack::getStatus, blackInfo.getStatus())
                .eqIfPresent(SysBlack::getLkzt, blackInfo.getLkzt())
                .eqIfPresent(SysBlack::getGkzt, blackInfo.getGkzt())
                .betweenIfPresent(SysBlack::getCreateTime, blackInfo.getBeginTime(),blackInfo.getEndTime() )
                .orderByDesc(SysBlack::getBId));
    }

    default List<SysBlack> selectListByStatus(Integer status) {
        return selectList(SysBlack::getStatus, status);
    }


}
