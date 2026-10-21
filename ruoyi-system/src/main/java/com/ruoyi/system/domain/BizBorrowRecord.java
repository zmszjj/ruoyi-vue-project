package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 借阅记录对象 biz_borrow_record
 * 
 * @author ruoyi
 * @date 2026-10-21
 */
public class BizBorrowRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 图书ID */
    @Excel(name = "图书ID")
    private Long bookId;

    /** 借阅学生ID */
    @Excel(name = "借阅学生ID")
    private Long studentId;
    private String studentName; // 新增一个专门装学生名字的字段

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /** 借出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借出时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date borrowTime;

    /** 归还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "归还时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnTime;

    /** 状态（0借出中 1已归还） */
    @Excel(name = "状态", readConverterExp = "0=借出中,1=已归还")
    private String status;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setBookId(Long bookId) 
    {
        this.bookId = bookId;
    }

    public Long getBookId() 
    {
        return bookId;
    }

    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }

    public void setBorrowTime(Date borrowTime) 
    {
        this.borrowTime = borrowTime;
    }

    public Date getBorrowTime() 
    {
        return borrowTime;
    }

    public void setReturnTime(Date returnTime) 
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() 
    {
        return returnTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("bookId", getBookId())
            .append("studentId", getStudentId())
            .append("borrowTime", getBorrowTime())
            .append("returnTime", getReturnTime())
            .append("status", getStatus())
            .toString();
    }
}
