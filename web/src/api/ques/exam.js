import request from '@/utils/request'
import { get } from 'sortablejs'

// 查询题目列表
export function addExam(data) {
    return request({
        url: '/ques/exam/addExam',
        method: 'post',
        data: data
    })
}


export function listExam(query) {
    return request({
        url: '/ques/exam/list',
        method: 'get',
        params: query
    })
}

export function myList(query) {
    return request({
        url: '/ques/exam/myList',
        method: 'get',
        params: query
    })
}

export function myAnswer(query) {
    return request({
        url: '/ques/exam/myAnswer',
        method: 'get',
        params: query
    })
}

export function myAnswerInfo(answerId) {
    return request({
        url: `/ques/exam/myAnswerInfo/${answerId}`,
        method: 'get'
    })
}



export function getExam(examId) {
    return request({
        url: `/ques/exam/info/${examId}`,
        method: 'get'
    })
}

export function delExam(examId) {
    return request({
        url: `/ques/exam/del/${examId}`,
        method: 'delete'
    })
}

export function editStatus(examId, status) {
    return request({
        url: `/ques/exam/editStatus/${examId}/${status}`,
        method: 'put'
    })
}

export function updateTheme(data) {
    return request({
        url: `/ques/exam/updateTheme`,
        method: 'put',
        data: data
    })
}



export function previewInfo(examId) {
    return request({
        url: `/ques/exam/previewInfo/${examId}`,
        method: 'get'
    })
}


export function examAnswerList(examId, query) {
    return request({
        url: `/ques/exam/examAnswerList/${examId}`,
        method: 'get',
        params: query
    })
}


export function examAnswerStat(examId, quId) {
    return request({
        url: `/ques/exam/examAnswerStat/${examId}/${quId}`,
        method: 'get'
    })
}