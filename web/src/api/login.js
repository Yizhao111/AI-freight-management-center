import request from '@/utils/request'

// 登录方法
export function login(username, password, code,key,grant_type) {
  const data = {
    username,
    password,
    code,
    key,
    grant_type,
    userType: 'sys_login'
  }
  return request({
    url: '/auth/oauth/token',
    method: 'post',
    params: data
  })
}

// 获取用户详细信息
export function register(data) {
  return request({
    url: '/system/common/register',
    method: 'post',
    data
  })
}

// 获取用户详细信息
export function getUser() {
  return request({
    url: '/auth/user',
    method: 'get'
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/adminApi/common/userInfo',
    method: 'get'
  })
}

// 登录成功调用
export function success() {
  return request({
    url: '/system/adminApi/common/success',
    method: 'get'
  })
}


// 退出方法
export function logout() {
  return request({
    url: '/auth/signout',
    method: 'delete'
  })
}

// 获取验证码
export function getCodeImg(query) {
  return request({
    url: '/auth/captcha',
    method: 'get',
    params: query
  })
}