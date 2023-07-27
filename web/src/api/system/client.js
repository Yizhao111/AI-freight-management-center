import request from '@/utils/request'

// 查询客户端列表
export function list(query) {
    return request({
        url: '/auth/client',
        method: 'get',
        params: query
    })
}

// 查询客户端密钥
export function viewClientSecret(clientId) {
    return request({
        url: '/auth/client/secret/' + clientId,
        method: 'get'
    })
}

// 删除
export function del(clientIds) {
    return request({
        url: '/auth/client/' + clientIds,
        method: 'delete'
    })
}


export function check(clientIds) {
    return request({
        url: '/auth/client/check/' + clientIds,
        method: 'get'
    })
}

export function addClient(data) {
    return request({
        url: '/auth/client',
        method: 'post',
        data: data
    })
}

export function updateClient(data) {
    return request({
        url: '/auth/client',
        method: 'put',
        data: data
    })
}

