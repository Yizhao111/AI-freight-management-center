import request from '@/utils/request'

// 查询题目列表
export function add(data) {
    return request({
        url: '/ques/examAnswer',
        method: 'post',
        data: data
    })
}
