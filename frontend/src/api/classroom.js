import request from './index.js'

export function getAvailableClassrooms(params) {
  return request.get('/classrooms/available', { params })
}
