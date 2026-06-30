import request from './index.js'

export function getNewsList(params) {
  return request.get('/news', { params })
}

export function getNewsDetail(id) {
  return request.get(`/news/${id}`)
}
