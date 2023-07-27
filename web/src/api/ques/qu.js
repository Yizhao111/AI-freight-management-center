import request from '@/utils/request'

// 查询题目列表
export function listQu(query) {
  return request({
    url: '/ques/qu/list',
    method: 'get',
    params: query
  })
}

// 查询题目详细
export function getQu(id) {
  return request({
    url: '/ques/qu/' + id,
    method: 'get'
  })
}

// 新增题目
export function addQu(data) {
  return request({
    url: '/ques/qu',
    method: 'post',
    data: data
  })
}

// 修改题目
export function updateQu(data) {
  return request({
    url: '/ques/qu',
    method: 'put',
    data: data
  })
}

// 删除题目
export function delQu(id) {
  return request({
    url: '/ques/qu/' + id,
    method: 'delete'
  })
}
