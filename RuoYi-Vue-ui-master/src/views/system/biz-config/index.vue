<template>
  <div class="app-container">
    <el-form ref="elForm" v-loading="loading" :model="formData" :rules="rules" size="medium" label-width="150px">
      <el-form-item label="WB 平台 Token" prop="wbToken">
        <el-input
          v-model="formData.wbToken"
          placeholder="请输入 WB 平台 Token"
          clearable
          show-password
        />
      </el-form-item>

      <el-form-item label="Seerfar 账户">
        <el-row :gutter="10" class="mb8">
          <el-col :span="24">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAddAccount"
              v-hasPermi="['system:bizConfig:save']"
            >新增账户</el-button>
          </el-col>
        </el-row>
        <el-table :data="formData.seerfarAccounts" border size="small" style="width: 100%; max-width: 720px">
          <el-table-column min-width="160">
            <template slot="header">
              <span class="cell-label-required">账户</span>
            </template>
            <template slot-scope="scope">
              <el-input v-model="scope.row.user" placeholder="账户" clearable />
            </template>
          </el-table-column>
          <el-table-column min-width="180">
            <template slot="header">
              <span class="cell-label-required">密码</span>
            </template>
            <template slot-scope="scope">
              <el-input v-model="scope.row.password" placeholder="密码" clearable show-password />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                icon="el-icon-delete"
                @click="handleRemoveAccount(scope.$index)"
                v-hasPermi="['system:bizConfig:save']"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

      <el-form-item size="large">
        <el-button
          type="primary"
          :loading="submitLoading"
          @click="submitForm"
          v-hasPermi="['system:bizConfig:save']"
        >保存</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { listBizConfig, saveBizConfig, CONFIG_KEY_SEERFAR_ACCOUNTS, CONFIG_KEY_WB_TOKEN } from '@/api/business/bizConfig'

function emptyForm() {
  return {
    wbToken: '',
    seerfarAccounts: []
  }
}

/** 将 list 返回的 SysConfig 行转为表单模型 */
function parseConfigRows(rows) {
  const form = emptyForm()
  if (!rows || !rows.length) {
    return form
  }
  rows.forEach((item) => {
    const key = item.configKey
    const val = item.configValue
    if (key === CONFIG_KEY_WB_TOKEN) {
      form.wbToken = val != null ? String(val) : ''
    } else if (key === CONFIG_KEY_SEERFAR_ACCOUNTS) {
      if (val != null && String(val).trim() !== '') {
        try {
          const arr = JSON.parse(val)
          if (Array.isArray(arr)) {
            form.seerfarAccounts = arr.map((a) => ({
              user: a && a.user != null ? String(a.user) : '',
              password: a && a.password != null ? String(a.password) : ''
            }))
          }
        } catch (e) {
          console.error('解析 seerfar.accounts JSON 失败', e)
          form.seerfarAccounts = []
        }
      }
    }
  })
  return form
}

export default {
  name: 'BizConfig',
  data() {
    return {
      loading: false,
      submitLoading: false,
      formData: emptyForm(),
      rules: {
        wbToken: [
          { required: true, message: '请输入 WB 平台 Token', trigger: 'blur', whitespace: true }
        ]
      }
    }
  },
  created() {
    this.loadConfig()
  },
  methods: {
    /** 从 BizConfigController list 拉取并填充 formData */
    loadConfig() {
      this.loading = true
      listBizConfig()
      .then((res) => {
        const rows = res.rows || []
        this.formData = parseConfigRows(rows)
      })
      .finally(() => {
        this.loading = false
      })
    },
    handleAddAccount() {
      this.formData.seerfarAccounts.push({ user: '', password: '' })
    },
    handleRemoveAccount(index) {
      this.formData.seerfarAccounts.splice(index, 1)
    },
    /**
     * Seerfar 账户：至少一条有效记录；每一行账户、密码均必填（整行空白也判错）。
     */
    buildSeerfarAccountsPayload() {
      const rows = this.formData.seerfarAccounts || []
      const accounts = []
      for (let i = 0; i < rows.length; i++) {
        const r = rows[i]
        const user = r.user != null ? String(r.user).trim() : ''
        const pwd = r.password != null ? String(r.password).trim() : ''
        if (!user || !pwd) {
          this.$modal.msgError(`第 ${i + 1} 行：账户和密码均为必填`)
          return null
        }
        accounts.push({ user, password: pwd })
      }
      if (accounts.length === 0) {
        this.$modal.msgError('请至少添加一条 Seerfar 账户')
        return null
      }
      return accounts
    },
    submitForm() {
      this.$refs.elForm.validate((valid) => {
        if (!valid) return
        const accounts = this.buildSeerfarAccountsPayload()
        if (accounts === null) {
          return
        }
        const wbToken = this.formData.wbToken != null ? String(this.formData.wbToken).trim() : ''
        const payload = {
          wbToken,
          seerfarAccounts: accounts
        }
        this.submitLoading = true
        saveBizConfig(payload)
        .then(() => {
          this.$modal.msgSuccess('保存成功')
          this.loadConfig()
        })
        .finally(() => {
          this.submitLoading = false
        })
      })
    },
    /** 从服务端重新加载，放弃未保存修改 */
    resetForm() {
      this.loadConfig()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}

/* 与 el-form 必填项星号风格一致 */
.cell-label-required::before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}
</style>
