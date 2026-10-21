package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Book;

/**
 * 图书信息Mapper接口
 * 
 * @author ruoyi
 * date 2026-10-21
 */
public interface BookMapper 
{
    /**
     * 查询图书信息
     *
     * @param bookId 图书信息主键
     * @return 图书信息
     */
     Book selectBookByBookId(Long bookId);

    /**
     * 查询图书信息列表
     * 
     * @param book 图书信息
     * @return 图书信息集合
     */
     List<Book> selectBookList(Book book);

    /**
     * 新增图书信息
     * 
     * @param book 图书信息
     * @return 结果
     */
     int insertBook(Book book);

    /**
     * 修改图书信息
     * 
     * @param book 图书信息
     * @return 结果
     */
     int updateBook(Book book);

    /**
     * 删除图书信息
     * 
     * @param bookId 图书信息主键
     * @return 结果
     */
     int deleteBookByBookId(Long bookId);

    /**
     * 批量删除图书信息
     * 
     * @param bookIds 需要删除的数据主键集合
     * @return 结果
     */
     int deleteBookByBookIds(Long[] bookIds);
}
