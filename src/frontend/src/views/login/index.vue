<template>
  <div class="login-container">
    <el-form
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      autocomplete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">化妆品监管平台</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          placeholder="用户名"
          type="text"
          tabindex="1"
          autocomplete="on"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          placeholder="密码"
          :type="passwordVisible ? 'text' : 'password'"
          tabindex="2"
          autocomplete="on"
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
          <template #suffix>
            <el-icon class="cursor-pointer" @click="passwordVisible = !passwordVisible">
              <component :is="passwordVisible ? 'View' : 'Hide'" />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="captcha" v-if="showCaptcha">
        <div class="captcha-container">
          <el-input
            v-model="loginForm.captcha"
            placeholder="验证码"
            type="text"
            tabindex="3"
          >
            <template #prefix>
              <el-icon><Picture /></el-icon>
            </template>
          </el-input>
          <img :src="captchaUrl" class="captcha-img" @click="refreshCaptcha" />
        </div>
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        class="login-button"
        @click="handleLogin"
      >
        登录
      </el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getCaptcha } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

interface LoginForm {
  username: string
  password: string
  captcha?: string
  captchaKey?: string
}

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const passwordVisible = ref(false)
const showCaptcha = ref(false)
const captchaKey = ref('')
const captchaUrl = ref('')

const loginForm = reactive<LoginForm>({
  username: '',
  password: '',
  captcha: '',
  captchaKey: ''
})

const loginRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

const refreshCaptcha = async () => {
  try {
    const res = await getCaptcha()
    loginForm.captchaKey = res.key
    captchaUrl.value = res.image
  } catch (error) {
    console.error('获取验证码失败:', error)
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    const success = await userStore.login(loginForm)
    if (success) {
      ElMessage.success('登录成功')
      router.push('/')
    }
  } catch (error) {
    console.error('登录失败:', error)
    if (error instanceof Error) {
      ElMessage.error(error.message)
    }
    showCaptcha.value = true
    await refreshCaptcha()
  } finally {
    loading.value = false
  }
}

// 初始化时获取验证码
refreshCaptcha()
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100%;
  width: 100%;
  background-color: #2d3a4b;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;

  .login-form {
    width: 400px;
    padding: 40px 35px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .title-container {
      text-align: center;
      margin-bottom: 30px;

      .title {
        font-size: 26px;
        color: #333;
        margin: 0;
      }
    }

    .captcha-container {
      display: flex;
      align-items: center;
      gap: 10px;

      .el-input {
        flex: 1;
      }

      .captcha-img {
        height: 40px;
        cursor: pointer;
      }
    }

    .login-button {
      width: 100%;
      margin-top: 10px;
    }
  }
}

.cursor-pointer {
  cursor: pointer;
}
</style> 