package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.BizBorrowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BookMapper;
import com.ruoyi.system.domain.Book;
import com.ruoyi.system.service.IBookService;
import com.ruoyi.system.service.IBizBorrowRecordService;
/**
 * 图书信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-10-21
 */
@Service
public class BookServiceImpl implements IBookService 
{
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private IBizBorrowRecordService bizBorrowRecordService;
    /**
     * 查询图书信息
     * 
     * @param bookId 图书信息主键
     * @return 图书信息
     */
    @Override
    public Book selectBookByBookId(Long bookId)
    {
        return bookMapper.selectBookByBookId(bookId);
    }

    /**
     * 查询图书信息列表
     * 
     * @param book 图书信息
     * @return 图书信息
     */
    @Override
    public List<Book> selectBookList(Book book)
    {
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增图书信息
     * 
     * @param book 图书信息
     * @return 结果
     */
    @Override
    public int insertBook(Book book)
    {
        book.setCreateTime(DateUtils.getNowDate());
        return bookMapper.insertBook(book);
    }

    /**
     * 修改图书信息
     * 
     * @param book 图书信息
     * @return 结果
     */
    @Override
    public int updateBook(Book book)
    {
        book.setUpdateTime(DateUtils.getNowDate());
        return bookMapper.updateBook(book);
    }
    /**
     * 借阅图书
     *
     * @param bookId 图书信息
     * @return 结果
     */

    @Override
    public int borrowBook(Long bookId) {
        // 1. 查书
        Book book = bookMapper.selectBookByBookId(bookId);
        if (book == null) {
            throw new ServiceException("图书不存在！");
        }
        if (book.getStock() == null || book.getStock() <= 0) {
            throw new ServiceException("库存不足，无法借阅！");
        }

        // 2. 更新库存
        book.setStock(book.getStock() - 1);
        int result = bookMapper.updateBook(book);

        // 3. 插入借阅记录（多表联动核心！）
        if (result > 0) {
            BizBorrowRecord record = new BizBorrowRecord();
            record.setBookId(bookId);
            record.setBorrowTime(new Date()); // 记录当前时间
            record.setStatus("0");            // 0代表借出中
            record.setStudentId(1L);          // 先写死一个学生ID为1
            bizBorrowRecordService.insertBizBorrowRecord(record);
        }
        return result;
    }


    /**
     *归还图书
     *
     * @param bookId 图书信息
     * @return 结果
     */

    @Override
    public int returnBook(Long bookId) {
        // 1. 查书
        Book book = bookMapper.selectBookByBookId(bookId);
        if (book == null) {
            throw new ServiceException("图书不存在！");
        }

        // 2. 查借阅记录，看看这本书是不是真的借出去了
        // 构造查询条件：bookId 是当前书，status 是 0（借出中）
        BizBorrowRecord queryRecord = new BizBorrowRecord();
        queryRecord.setBookId(bookId);
        queryRecord.setStatus("0");
        List<BizBorrowRecord> recordList = bizBorrowRecordService.selectBizBorrowRecordList(queryRecord);

        if (recordList == null || recordList.isEmpty()) {
            throw new ServiceException("这本书没有被借出，无法归还！");
        }

        // 取出最新的一条借阅记录（假设只查一条）
        BizBorrowRecord record = recordList.get(0);

        // 3. 更新库存
        book.setStock(book.getStock() + 1);
        int result = bookMapper.updateBook(book);

        // 4. 更新借阅记录（变成已归还，填入归还时间）
        if (result > 0) {
            record.setStatus("1"); // 1代表已归还
            record.setReturnTime(new Date()); // 记录归还时间
            bizBorrowRecordService.updateBizBorrowRecord(record);
        }

        return result;
    }


    /**
     * 批量删除图书信息
     * 
     * @param bookIds 需要删除的图书信息主键
     * @return 结果
     */
    @Override
    public int deleteBookByBookIds(Long[] bookIds)
    {
        return bookMapper.deleteBookByBookIds(bookIds);
    }

    /**
     * 删除图书信息信息
     * 
     * @param bookId 图书信息主键
     * @return 结果
     */
    @Override
    public int deleteBookByBookId(Long bookId)
    {
        return bookMapper.deleteBookByBookId(bookId);
    }
}
