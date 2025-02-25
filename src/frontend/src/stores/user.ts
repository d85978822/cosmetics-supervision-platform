import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, logout } from '@/api/auth'
import type { LoginRequest } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref<number>(0)
  const username = ref('')
  const realName = ref('')
  const avatar = ref('')
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  // 登录
  const loginAction = async (loginData: LoginRequest) => {
    try {
      const res = await login(loginData)
      token.value = res.token
      userId.value = res.userId
      username.value = res.username
      realName.value = res.realName
      avatar.value = res.avatar
      roles.value = res.roles
      permissions.value = res.permissions
      localStorage.setItem('token', res.token)
      return true
    } catch (error) {
      return false
    }
  }

  // 登出
  const logoutAction = async () => {
    try {
      await logout()
      resetState()
      return true
    } catch (error) {
      return false
    }
  }

  // 重置状态
  const resetState = () => {
    token.value = ''
    userId.value = 0
    username.value = ''
    realName.value = ''
    avatar.value = ''
    roles.value = []
    permissions.value = []
    localStorage.removeItem('token')
  }

  return {
    token,
    userId,
    username,
    realName,
    avatar,
    roles,
    permissions,
    login: loginAction,
    logout: logoutAction,
    resetState
  }
}) 