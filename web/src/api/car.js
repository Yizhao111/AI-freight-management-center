import request from '@/utils/request'

// 查询车辆管理列表
export function listCar(query) {
  return request({
    url: '/busi/car/list',
    method: 'get',
    params: query
  })
}

export function listCarAll() {
  return request({
    url: '/busi/car/listAll',
    method: 'get'
  })
}


// 查询车辆管理详细
export function getCar(id) {
  return request({
    url: '/busi/car/' + id,
    method: 'get'
  })
}

// 新增车辆管理
export function addCar(data) {
  return request({
    url: '/busi/car',
    method: 'post',
    data: data
  })
}

// 修改车辆管理
export function updateCar(data) {
  return request({
    url: '/busi/car',
    method: 'put',
    data: data
  })
}

// 删除车辆管理
export function delCar(id) {
  return request({
    url: '/busi/car/' + id,
    method: 'delete'
  })
}

