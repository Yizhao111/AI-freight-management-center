import request from '@/utils/request'

export function listCategory(query) {
  return request({
    url: '/busi/shopCategory/list',
    method: 'get',
    params: query
  })
}

export function optionsCategory() {
    return request({
      url: '/busi/shopCategory/options',
      method: 'get'
    })
  }

export function listL1() {
  return request({
    url: '/busi/shopCategory/l1Options',
    method: 'get'
  })
}

export function createCategory(data) {
  return request({
    url: '/busi/shopCategory',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/busi/shopCategory',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: '/busi/shopCategory/'+ id,
    method: 'DELETE'
  })
}