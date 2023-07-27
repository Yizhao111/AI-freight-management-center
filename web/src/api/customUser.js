import request from '@/utils/request'

// 查询auth列表
export function listUser(query) {
  return request({
    url: '/auth/customUser/list',
    method: 'get',
    params: query
  })
}

// 查询全部
export function listUserAll() {
  return request({
    url: '/auth/customUser/listAll',
    method: 'get'
  })
}

// 查询auth详细
export function getUser(id) {
  return request({
    url: '/auth/customUser/' + id,
    method: 'get'
  })
}

// 新增auth
export function addUser(data) {
  return request({
    url: '/auth/customUser',
    method: 'post',
    data: data
  })
}

// 修改auth
export function updateUser(data) {
  return request({
    url: '/auth/customUser',
    method: 'put',
    data: data
  })
}

// 删除auth
export function delUser(id) {
  return request({
    url: '/auth/customUser/' + id,
    method: 'delete'
  })
}

