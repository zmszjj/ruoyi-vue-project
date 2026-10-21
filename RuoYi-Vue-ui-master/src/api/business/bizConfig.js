import request from '@/utils/request'

/** 与 SaveBizConfig 中键名一致 */
export const CONFIG_KEY_SEERFAR_ACCOUNTS = 'seerfar.accounts'
export const CONFIG_KEY_WB_TOKEN = 'wb.token'

/** 查询业务配置（SysConfig 列表，configValue 为 JSON 或纯文本） */
export function listBizConfig() {
  return request({
    url: '/business/config/list',
    method: 'get'
  })
}

/** 保存业务配置（wbToken + seerfarAccounts） */
export function saveBizConfig(data) {
  return request({
    url: '/business/config/save',
    method: 'post',
    data: data
  })
}
