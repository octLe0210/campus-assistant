import request from './index.js'

// 新闻管理
export const createNews = (data) => request.post('/admin/news', data)
export const updateNews = (id, data) => request.put(`/admin/news/${id}`, data)
export const deleteNews = (id) => request.delete(`/admin/news/${id}`)

// 课程管理
export const createCourse = (data) => request.post('/admin/courses', data)
export const updateCourse = (id, data) => request.put(`/admin/courses/${id}`, data)
export const deleteCourse = (id) => request.delete(`/admin/courses/${id}`)

// 用户管理
export const getUserList = () => request.get('/admin/users')

// 统计
export const getStatistics = () => request.get('/admin/statistics')
