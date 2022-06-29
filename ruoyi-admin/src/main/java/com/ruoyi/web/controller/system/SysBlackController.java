package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysBlack;
import com.ruoyi.system.domain.SysBlackInfo;
import com.ruoyi.system.service.ISysBlackService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/black")
public class SysBlackController extends BaseController
{
    private String prefix = "system/black";

    @Autowired
    private ISysBlackService blackService;

    @RequiresPermissions("system:black:view")
    @GetMapping()
    public String black()
    {
        return prefix + "/black";
    }

    @RequiresPermissions("system:black:list")
    @PostMapping("/list")
    public TableDataInfo list(SysBlackInfo black)
    {
        startPage();
        List<SysBlack> list = blackService.selectBlackList(black);
        return getDataTable(list);
    }

    @Log(title = "追逃人员管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:black:export")
    @PostMapping("/export")
    public AjaxResult export(SysBlack black)
    {
        SysBlackInfo blackInfo = new SysBlackInfo();
        try {
            BeanUtils.copyProperties(blackInfo,black );
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ServiceException(e.getMessage());
        }
        List<SysBlack> list = blackService.selectBlackList(blackInfo);
        ExcelUtil<SysBlack> util = new ExcelUtil<SysBlack>(SysBlack.class);
        return util.exportExcel(list, "追逃人员数据");
    }

    @Log(title = "追逃人员管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:black:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysBlack> util = new ExcelUtil<SysBlack>(SysBlack.class);
        List<SysBlack> blackList = util.importExcel(file.getInputStream());
        String message = blackService.importBlack(blackList, updateSupport, getLoginName());
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:black:view")
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysBlack> util = new ExcelUtil<SysBlack>(SysBlack.class);
        return util.importTemplateExcel("追逃人员数据");
    }

    /**
     * 新增追逃人员
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        return prefix + "/add";
    }

    /**
     * 新增保存追逃人员
     */
    @RequiresPermissions("system:black:add")
    @Log(title = "追逃人员管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@Validated SysBlack black)
    {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(blackService.checkSfzhmUnique(black.getSfzhm())))
        {
            return error("新增人员信息'" + black.getXm() + "'失败，人员信息已存在");
        }

        black.setCreateBy(getLoginName());
        black.setCreateTime(new Date());
        return toAjax(blackService.insertBlack(black));
    }

    /**
     * 修改人员信息
     */
    @RequiresPermissions("system:black:edit")
    @GetMapping("/edit/{bId}")
    public String edit(@PathVariable("bId") Long bId, ModelMap mmap)
    {


        mmap.put("black", blackService.selectBlackById(bId));

        return prefix + "/edit";
    }

    /**
     * 修改保存追逃人员
     */
    @RequiresPermissions("system:black:edit")
    @Log(title = "追逃人员管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated SysBlack black)
    {
        black.setUpdateBy(getLoginName());
        black.setUpdateTime(new Date());
        return toAjax(blackService.updateBalck(black));
    }









    @RequiresPermissions("system:black:remove")
    @Log(title = "追逃人员管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(String ids)
    {

        return toAjax(blackService.deleteBlackByIds(ids));
    }

    /**
     * 校验追逃人员身份证
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkSfzhmUnique(SysBlack black)
    {
        return blackService.checkSfzhmUnique(black.getSfzhm());
    }



    /**
     * 追逃人员状态修改
     */
    @Log(title = "追逃人员管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:black:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysBlack black)
    {

        return toAjax(blackService.changeStatus(black));
    }
}