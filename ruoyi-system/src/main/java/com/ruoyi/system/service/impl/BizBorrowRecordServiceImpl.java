package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.Student;
import com.ruoyi.system.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizBorrowRecordMapper;
import com.ruoyi.system.domain.BizBorrowRecord;
import com.ruoyi.system.service.IBizBorrowRecordService;

/**
 * 借阅记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-10-21
 */
@Service
public class BizBorrowRecordServiceImpl implements IBizBorrowRecordService 
{
    @Autowired
    private IStudentService studentService;
    @Autowired
    private BizBorrowRecordMapper bizBorrowRecordMapper;

    /**
     * 查询借阅记录
     * 
     * @param recordId 借阅记录主键
     * @return 借阅记录
     */
    @Override
    public BizBorrowRecord selectBizBorrowRecordByRecordId(Long recordId)
    {
        return bizBorrowRecordMapper.selectBizBorrowRecordByRecordId(recordId);
    }

    /**
     * 查询借阅记录列表
     * 
     * @param bizBorrowRecord 借阅记录
     * @return 借阅记录
     */
    @Override
    public List<BizBorrowRecord> selectBizBorrowRecordList(BizBorrowRecord bizBorrowRecord) {
        // 先查出所有借阅记录
        List<BizBorrowRecord> list = bizBorrowRecordMapper.selectBizBorrowRecordList(bizBorrowRecord);

        // 遍历记录，把学生名字查出来塞进去
        for (BizBorrowRecord record : list) {
            if (record.getStudentId() != null) {
                Student student = studentService.selectStudentByStudentId(record.getStudentId());
                if (student != null) {
                    // 注意：因为你生成的实体类里可能没有 studentName 字段
                    // 只要你在 domain/BizBorrowRecord.java 里加上 private String studentName; 和 get/set 方法就行
                     record.setStudentName(student.getStudentName());
                }
            }
        }
        return list;
    }

    /**
     * 新增借阅记录
     * 
     * @param bizBorrowRecord 借阅记录
     * @return 结果
     */
    @Override
    public int insertBizBorrowRecord(BizBorrowRecord bizBorrowRecord)
    {
        return bizBorrowRecordMapper.insertBizBorrowRecord(bizBorrowRecord);
    }

    /**
     * 修改借阅记录
     * 
     * @param bizBorrowRecord 借阅记录
     * @return 结果
     */
    @Override
    public int updateBizBorrowRecord(BizBorrowRecord bizBorrowRecord)
    {
        return bizBorrowRecordMapper.updateBizBorrowRecord(bizBorrowRecord);
    }

    /**
     * 批量删除借阅记录
     * 
     * @param recordIds 需要删除的借阅记录主键
     * @return 结果
     */
    @Override
    public int deleteBizBorrowRecordByRecordIds(Long[] recordIds)
    {
        return bizBorrowRecordMapper.deleteBizBorrowRecordByRecordIds(recordIds);
    }

    /**
     * 删除借阅记录信息
     * 
     * @param recordId 借阅记录主键
     * @return 结果
     */
    @Override
    public int deleteBizBorrowRecordByRecordId(Long recordId)
    {
        return bizBorrowRecordMapper.deleteBizBorrowRecordByRecordId(recordId);
    }
}
