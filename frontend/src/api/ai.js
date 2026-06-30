import request from './index.js'

export function sendMessage(data) {
  return request.post('/ai/chat', data)
}

export function getChatHistory() {
  return request.get('/ai/history')
}
