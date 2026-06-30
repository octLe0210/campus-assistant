import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/api/index.js'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  async function login(username, password) {
    const res = await request.post('/user/login', { username, password })
    token.value = res.data.token
    user.value = res.data.user
    localStorage.setItem('token', res.data.token)
  }

  async function register(username, password, nickname) {
    await request.post('/user/register', { username, password, nickname })
  }

  async function fetchUser() {
    try {
      const res = await request.get('/user/info')
      user.value = res.data
    } catch {
      token.value = ''
      user.value = null
      localStorage.removeItem('token')
    }
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
  }

  return { user, token, login, register, fetchUser, logout }
}, {
  persist: true
})
