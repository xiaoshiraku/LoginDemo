<template>
     <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">手机号登录</h2>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="phone">手机号</label>
          <input
            type="tel"
            id="phone"
            v-model="phone"
            placeholder="请输入手机号"
            :class="{ 'invalid': phoneError , 'phone-input': true}"
            required
          >
          <p class="error-message" v-if="phoneError">{{ phoneError }}</p>
        </div>
        
        <div class="form-group">
          <label for="code">验证码</label>
          <div class="code-input-group">
            <input
              type="text"
              id="code"
              v-model="code"
              placeholder="请输入验证码"
              :class="{ 'invalid': codeError }"
              required
            >
            <button
              type="button"
              class="send-code-btn"
              @click="sendVerificationCode"
              :disabled="isSending || countDown > 0"
            >
              {{ countDown > 0 ? `${countDown}s后重发` : '发送验证码' }}
            </button>
          </div>
          <p class="error-message" v-if="codeError">{{ codeError }}</p>
        </div>
        
        <button type="submit" class="login-btn" :disabled="isLoading">
          <span v-if="isLoading">登录中...</span>
          <span v-else>登录</span>
        </button>
      </form>
    </div>
  </div>
</template>


<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import {login, sendCode} from '../api/loginApi'; // 假设你有一个 auth.js 文件处理 API 请求
import { ElMessage } from 'element-plus';

// 表单数据
const phone = ref('');
const code = ref('');
const countDown = ref(0);
const isSending = ref(false);
const isLoading = ref(false);
const phoneError = ref('');
const codeError = ref('');

const router = useRouter();

// 手机号验证
const validatePhone = () => {
  const reg = /^1[3-9]\d{9}$/;
  if (!phone.value) {
    phoneError.value = '请输入手机号';
    return false;
  } else if (!reg.test(phone.value)) {
    phoneError.value = '请输入正确的手机号格式';
    return false;
  } else {
    phoneError.value = '';
    return true;
  }
};

// 验证码验证
const validateCode = () => {
  if (!code.value) {
    codeError.value = '请输入验证码';
    return false;
  } else if (code.value.length !== 6) {
    codeError.value = '验证码长度为6位';
    return false;
  } else {
    codeError.value = '';
    return true;
  }
};

// 发送验证码
const sendVerificationCode = async () => {
  // 验证手机号格式
  if (!validatePhone()) return;
  
  isSending.value = true;
  
  try {
    // 调用发送验证码接口
    const response = await sendCode({ phone: phone.value });
    
    if (response.success) {
      ElMessage.success('验证码发送成功，请注意查收');
      
      // 开始倒计时
      countDown.value = 60;
      const timer = setInterval(() => {
        countDown.value--;
        if (countDown.value <= 0) {
          clearInterval(timer);
        }
      }, 1000);
    } else {
      ElMessage.error(response.message || '验证码发送失败');
    }
  } catch (error) {
    console.error('发送验证码失败:', error);
    ElMessage.error('网络异常，发送失败，请稍后重试');
  } finally {
    isSending.value = false;
  }
};

// 处理登录
const handleLogin = async () => {
 // 验证表单
 if (!validatePhone() || !validateCode()) return;
  
  isLoading.value = true;
  
  try {
    // 调用登录接口
    const response = await login({
      phone: phone.value,
      code: code.value
    });
    
    if (response.success) {
      ElMessage.success('登录成功');
      // 保存用户信息和token
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
      // 跳转到首页或之前的页面
      router.push('/home').catch(err => console.log(err));
    } else {
      ElMessage.error(response.message || '登录失败');
    }
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error('网络异常，登录失败，请稍后重试');
  } finally {
    isLoading.value = false;
  }
};

// 监听输入，实时验证
watch(phone, validatePhone);
watch(code, validateCode);
</script>



<style scoped>
.login-container {
    width: 100%;
    height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 10px;
    background-color: #f5f5f5;
}

.login-card {
    width: 100%;
    max-width: 400px;
    padding: 2rem;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.login-title {
    text-align: center;
    margin-bottom: 1.5rem;
    color: #333;
}

.form-group {
    margin-bottom: 1.5rem;
}

label {
    display: block;
    margin-bottom: 0.5rem;
    color: #666;
    font-size: 0.9rem;
}

/* 单独调整手机号输入框宽度 */
.phone-input {
  width: 94%;  /* 比默认100%略小，实现"略微减少"的效果 */
}

input {
    width: 100%;
    padding: 0.8rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
}

input.invalid {
    border-color: #ff4d4f;
}

.code-input-group {
    display: flex;
    gap: 0.5rem;
}

.send-code-btn {
    padding: 0 1rem;
    background-color: #42b983;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    white-space: nowrap;
}

.send-code-btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}

.login-btn {
    width: 100%;
    padding: 0.8rem;
    background-color: #42b983;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
}

.login-btn:hover:not(:disabled) {
    background-color: #359e75;
}

.login-btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}

.error-message {
    margin-top: 0.3rem;
    color: #ff4d4f;
    font-size: 0.8rem;
    margin-bottom: 0;
}
</style>
