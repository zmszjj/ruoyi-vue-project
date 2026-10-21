<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="书名" prop="bookName">
        <el-input
          v-model="queryParams.bookName"
          placeholder="请输入书名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input
          v-model="queryParams.author"
          placeholder="请输入作者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ISBN" prop="isbn">
        <el-input
          v-model="queryParams.isbn"
          placeholder="请输入ISBN"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库存" prop="stock">
        <el-input
          v-model="queryParams.stock"
          placeholder="请输入库存"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:book:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:book:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:book:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:book:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

  <el-table v-loading="loading" :data="bookList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图书ID" align="center" prop="bookId" />
      <el-table-column label="书名" align="center" prop="bookName" />
      <el-table-column label="作者" align="center" prop="author" />
      <el-table-column label="ISBN" align="center" prop="isbn" />
      <el-table-column label="库存" align="center" prop="stock" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-minus" @click="handleBorrow(scope.row)">借出</el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleReturn(scope.row)">归还</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改图书信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="书名" prop="bookName">
              <el-input v-model="form.bookName" placeholder="请输入书名" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="ISBN" prop="isbn">
              <el-input v-model="form.isbn" placeholder="请输入ISBN" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="库存" prop="stock">
              <el-input v-model="form.stock" placeholder="请输入库存" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="LINK" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBook, getBook, delBook, addBook, updateBook,borrowBook,returnBook } from "@/api/system/book"

export default {
  name: "Book",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 图书信息表格数据
      bookList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookName: null,
        author: null,
        isbn: null,
        stock: null,
      },
      // 表单参数
      form: {},
      // 表单校验
            rules: {
        bookName: [
          { required: true, message: "书名不能为空", trigger: "blur" }
        ],
        stock: [
          { required: true, message: "库存不能为空", trigger: "blur" },
          { pattern: /^[0-9]+$/, message: "库存必须是非负整数", trigger: "blur" }
        ]
      },
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询图书信息列表 */
    getList() {
      this.loading = true
      listBook(this.queryParams).then(response => {
        this.bookList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        bookId: null,
        bookName: null,
        author: null,
        isbn: null,
        stock: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.bookId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加图书信息"
    },
      /** 借出按钮操作 */
    handleBorrow(row) {
      this.$modal.confirm('确认要借出《' + row.bookName + '》吗？').then(() => {
        return borrowBook(row.bookId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("借出成功");
      }).catch((error) => {
        console.error("借出报错了：", error);
      });
    },
     /** 归还按钮操作 */
    handleReturn(row) {
      this.$modal.confirm('确认要归还《' + row.bookName + '》吗？').then(() => {
        return returnBook(row.bookId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("归还成功");
      }).catch((error) => {
        console.error("归还报错了：", error);
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const bookId = row.bookId || this.ids
      getBook(bookId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改图书信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bookId != null) {
            updateBook(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBook(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const bookIds = row.bookId || this.ids
      this.$modal.confirm('是否确认删除图书信息编号为"' + bookIds + '"的数据项？').then(function() {
        return delBook(bookIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/book/export', {
        ...this.queryParams
      }, `book_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
