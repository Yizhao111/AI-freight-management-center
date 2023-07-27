import request from '@/utils/request'

// 查询订单列表
export function listOrders(query) {
  return request({
    url: '/busi/orders/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrders(orderId) {
  return request({
    url: '/busi/orders/' + orderId,
    method: 'get'
  })
}

// 新增订单
export function addOrders(data) {
  return request({
    url: '/busi/orders',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrders(data) {
  return request({
    url: '/busi/orders',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delOrders(orderId) {
  return request({
    url: '/busi/orders/' + orderId,
    method: 'delete'
  })
}

// 获取所有的经纬度坐标，完成点聚合地图
export function getLnglat() {
  return request({
    url: '/busi/orders/getLnglat',
    method: 'get'
  })
}
