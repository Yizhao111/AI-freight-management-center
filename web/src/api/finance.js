import request from '@/utils/request'

// 查询财务列表
export function listFinance(query) {
  return request({
    url: '/busi/finance/list',
    method: 'get',
    params: query
  })
}