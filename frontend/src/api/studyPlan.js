import request from './index.js'

export function generatePlan(goal) {
  return request.post('/study-plans/generate', { goal })
}

export function getPlanList() {
  return request.get('/study-plans')
}

export function updatePlan(id, data) {
  return request.put(`/study-plans/${id}`, data)
}

export function deletePlan(id) {
  return request.delete(`/study-plans/${id}`)
}
