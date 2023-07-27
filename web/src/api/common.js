import request from '@/utils/request'

// 表单校验 新增用户时验证库里是否有重复用户名
export function checkUsername(username) {
  return request({
    url: '/system/adminApi/common/checkUsername/' + username,
    method: 'get'
  })
}

// 表单校验 修改密码时验证旧密码是否正确
export function checkPassword(password) {
  return request({
    url: '/system/adminApi/common/checkPassword/' + password,
    method: 'get'
  })
}

// 首页接口
export function getIndexData() {
  return request({
    url: '/auth/customUser/findLastTenDaysVisitCount',
    method: 'get'
  })
}

export function getIndexData1() {
  return request({
    url: '/busi/stat/count',
    method: 'get'
  })
}


// 获取用户列表
export function selectUserList() {
  return request({
    url: '/system/adminApi/common/selectUserList',
    method: 'get'
  })
}

// 获取角色列表
export function selectRole() {
  return request({
    url: '/system/common/selectRole',
    method: 'get'
  })
}


// 根据角色id获取用户列表
export function userByRole(roleId) {
  return request({
    url: `/system/adminApi/sysUser/userByRole?roleId=${roleId}`,
    method: 'get'
  })
}