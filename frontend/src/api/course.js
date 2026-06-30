import request from './index.js'

export function searchCourses(params) {
  return request.get('/courses', { params })
}
