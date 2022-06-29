package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.mybatis.query.LambdaQueryWrapperX;
import com.ruoyi.common.config.mybatis.query.QueryWrapperX;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ISysBlackService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * 人员 业务层处理
 * 
 * @author ruoyi
 */
@Service
@Slf4j
public class SysBlackServiceImpl implements ISysBlackService
{
    //private static final Logger log = LoggerFactory.getLogger(SysBlackServiceImpl.class);

    @Resource
    private SysBlackMapper sysBlackMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;


    @Override
    public List<SysBlack> selectBlackList(SysBlackInfo blackInfo) {
        return sysBlackMapper.selectBlackList(blackInfo);
    }

    @Override
    public SysBlack selectBlackBySfzhm(String sfzhm) {
        return sysBlackMapper.selectBySfzhm(sfzhm);
    }

    @Override
    public SysBlack selectBlackById(Long bId) {
        return sysBlackMapper.selectById(bId);
    }

    @Override
    public int deleteBlackById(Long bId) {
        return sysBlackMapper.deleteById(bId);
    }

    @Override
    public int deleteBlackByIds(String ids) {
        Long[] blackIds = Convert.toLongArray(ids);

        return sysBlackMapper.deleteBatchIds(Arrays.asList(blackIds));
    }

    @Override
    public int insertBlack(SysBlack black) {
        return sysBlackMapper.insert(black);
    }

    @Override
    public int updateBalck(SysBlack black) {
        return sysBlackMapper.updateById(black);
    }

    @Override
    public String checkSfzhmUnique(String sfzhm) {

        Long count = sysBlackMapper.selectCount(new QueryWrapperX<SysBlack>().eq("sfzhm", sfzhm));

        if (count>0)
        {
            return "1";//人员已存在
        }

        return "0";//人员不存在
    }

    @Override
    public String importBlack(List<SysBlack> blackList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(blackList) || blackList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysBlack black : blackList)
        {
            try
            {
                // 验证是否存在这个用户
                SysBlack b = sysBlackMapper.selectBySfzhm(black.getSfzhm());
                if (StringUtils.isNull(b))
                {
                    BeanValidators.validateWithException(validator, black);

                    black.setCreateBy(operName);
                    this.insertBlack(black);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、人员 " + black.getXm() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator,black);
                    black.setUpdateBy(operName);
                    this.updateBalck(black);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、人员 " + black.getXm() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、人员 " + black.getXm() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + black.getXm() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public int changeStatus(SysBlack black) {
        return sysBlackMapper.update(null,new LambdaQueryWrapper<SysBlack>().eq(SysBlack::getBId,black.getBId()));
    }
}
