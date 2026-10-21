package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BizBorrowRecord;
import com.ruoyi.system.service.IBizBorrowRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 借阅记录Controller
 * 
 * @author ruoyi
 * @date 2026-10-21
 */
@RestController
@RequestMapping("/system/record")
public class BizBorrowRecordController extends BaseController
{
    @Autowired
    private IBizBorrowRecordService bizBorrowRecordService;

    /**
     * 查询借阅记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizBorrowRecord bizBorrowRecord)
    {
        startPage();
        List<BizBorrowRecord> list = bizBorrowRecordService.selectBizBorrowRecordList(bizBorrowRecord);
        return getDataTable(list);
    }

    /**
     * 导出借阅记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "借阅记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizBorrowRecord bizBorrowRecord)
    {
        List<BizBorrowRecord> list = bizBorrowRecordService.selectBizBorrowRecordList(bizBorrowRecord);
        ExcelUtil<BizBorrowRecord> util = new ExcelUtil<BizBorrowRecord>(BizBorrowRecord.class);
        util.exportExcel(response, list, "借阅记录数据");
    }

    /**
     * 获取借阅记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(bizBorrowRecordService.selectBizBorrowRecordByRecordId(recordId));
    }

    /**
     * 新增借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "借阅记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizBorrowRecord bizBorrowRecord)
    {
        return toAjax(bizBorrowRecordService.insertBizBorrowRecord(bizBorrowRecord));
    }

    /**
     * 修改借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "借阅记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizBorrowRecord bizBorrowRecord)
    {
        return toAjax(bizBorrowRecordService.updateBizBorrowRecord(bizBorrowRecord));
    }

    /**
     * 删除借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "借阅记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(bizBorrowRecordService.deleteBizBorrowRecordByRecordIds(recordIds));
    }
}
