package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizBorrowRecord;

/**
 * 借阅记录Service接口
 * 
 * @author ruoyi
 * @date 2026-10-21
 */
public interface IBizBorrowRecordService 
{
    /**
     * 查询借阅记录
     * 
     * @param recordId 借阅记录主键
     * @return 借阅记录
     */
    public BizBorrowRecord selectBizBorrowRecordByRecordId(Long recordId);

    /**
     * 查询借阅记录列表
     * 
     * @param bizBorrowRecord 借阅记录
     * @return 借阅记录集合
     */
    public List<BizBorrowRecord> selectBizBorrowRecordList(BizBorrowRecord bizBorrowRecord);

    /**
     * 新增借阅记录
     * 
     * @param bizBorrowRecord 借阅记录
     * @return 结果
     */
    public int insertBizBorrowRecord(BizBorrowRecord bizBorrowRecord);

    /**
     * 修改借阅记录
     * 
     * @param bizBorrowRecord 借阅记录
     * @return 结果
     */
    public int updateBizBorrowRecord(BizBorrowRecord bizBorrowRecord);

    /**
     * 批量删除借阅记录
     * 
     * @param recordIds 需要删除的借阅记录主键集合
     * @return 结果
     */
    public int deleteBizBorrowRecordByRecordIds(Long[] recordIds);

    /**
     * 删除借阅记录信息
     * 
     * @param recordId 借阅记录主键
     * @return 结果
     */
    public int deleteBizBorrowRecordByRecordId(Long recordId);
}
