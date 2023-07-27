import request from '@/utils/request'

// 查询商品列表
export function listShopGoods(query) {
    return request({
        url: '/busi/shopGoods/list',
        method: 'get',
        params: query
    })
}

// 查询商品详细
export function getShopGoods(id) {
    return request({
        url: '/busi/shopGoods/' + id,
        method: 'get'
    })
}

// 新增商品
export function addShopGoods(data) {
    return request({
        url: '/busi/shopGoods',
        method: 'post',
        data: data
    })
}

// 修改商品
export function updateShopGoods(data) {
    return request({
        url: '/busi/shopGoods',
        method: 'put',
        data: data
    })
}

// 删除商品
export function delShopGoods(id) {
    return request({
        url: '/busi/shopGoods/' + id,
        method: 'delete'
    })
}

// 获取所有的商品
export function getShopGoodsAll() {
    return request({
        url: '/busi/shopGoods/listAll',
        method: 'get'
    })
}