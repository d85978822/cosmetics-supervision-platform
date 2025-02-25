import request from '@/utils/request'

export interface LoginRequest {
  username: string
  password: string
  captcha?: string
  captchaKey?: string
}

export interface LoginResponse {
  token: string
  userId: number
  username: string
  realName: string
  avatar: string
  roles: string[]
  permissions: string[]
}

export interface CaptchaResponse {
  key: string
  image: string
}

// 登录
export function login(data: LoginRequest) {
  return request<LoginResponse>({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCaptcha() {
  return request<CaptchaResponse>({
    url: '/auth/captcha',
    method: 'get'
  })
}

// 刷新token
export function refreshToken() {
  return request<string>({
    url: '/auth/refresh',
    method: 'post'
  })
} 