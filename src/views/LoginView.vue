<template>
  <div class="login-container">
    <el-card class="login-card">
      <div slot="header" class="clearfix">
        <h2>用户登录</h2>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="80px" size="small">
        <el-form-item label="账号" prop="account">
          <el-input v-model="loginForm.account" placeholder="请输入账号" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="submitForm('loginForm')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="links">
        <router-link to="/register">没有账号？去注册</router-link>
        <a href="javascript:;" @click="showResetPasswordDialog">忘记密码？</a>
        <router-link to="/main/home">返回首页</router-link>
      </div>
    </el-card>

    <!-- 重置密码对话框 -->
    <el-dialog title="重置密码" :visible.sync="resetPasswordVisible" width="400px" center>
      <el-form :model="resetForm" :rules="resetRules" ref="resetForm" label-width="100px" size="small">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="resetForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="resetForm.email" placeholder="请输入注册邮箱" prefix-icon="el-icon-message"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <div class="code-input">
            <el-input v-model="resetForm.code" placeholder="请输入验证码" prefix-icon="el-icon-key"></el-input>
            <el-button type="primary" size="small" @click="sendVerificationCode" :disabled="codeButtonDisabled">
              {{ codeButtonText }}
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="resetForm.newPassword" type="password" placeholder="请输入新密码" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetForm.confirmPassword" type="password" placeholder="请再次输入新密码" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPasswordVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitResetForm('resetForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import AccountServices from '@/service/AccountServices'

export default {
  name: 'LoginView',
  data() {
    // 验证密码一致性
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.resetForm.confirmPassword !== '') {
          this.$refs.resetForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.resetForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }

    return {
      loginForm: {
        account: '',
        password: ''
      },
      rules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ]
      },
      // 重置密码相关数据
      resetPasswordVisible: false,
      resetForm: {
        username: '',
        email: '',
        code: '',
        newPassword: '',
        confirmPassword: '',
        captchaKey: '',
        captchaCode: ''
      },
      resetRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, validator: validatePass, trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ]
      },
      codeButtonText: '获取验证码',
      codeButtonDisabled: false,
      countdown: 60,
      // 图形验证码相关
      captchaImage: '',
      captchaKey: ''
    }
  },

  computed: {
    loginModel() {
      return {
        account: this.loginForm.account,
        password: this.loginForm.password
      }
    },
    verificationCodeModel() {
      return {
        email: this.resetForm.email,
        type: 0,
        captchaKey: this.resetForm.captchaKey,
        captchaCode: this.resetForm.captchaCode
      }
    },
    resetPasswordModel() {
      return {
        email: this.resetForm.email,
        code: this.resetForm.code,
        newPassword: this.resetForm.newPassword
      }
    }
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          AccountServices.Login(this.loginModel)
            .then(response => {
              if (response.success) {
                const userData = response.data
                if (userData && userData.token) {
                  localStorage.setItem('token', userData.token)
                  this.$store.dispatch('login', { token: userData.token, username: this.loginForm.account })
                  this.$message.success('登录成功！')
                  this.$router.push('/main/home')
                } else {
                  this.$message.error('登录响应数据格式错误')
                }
              } else {
                this.$message.error(response.errorMsg || '登录失败，请稍后重试。')
              }
            })
            .catch(error => {
              console.error('登录请求错误：', error)
              this.$message.error('登录失败：' + (error.message || '网络错误或服务器无响应。'))
            })
        } else {
          return false
        }
      })
    },
    // 显示重置密码对话框
    showResetPasswordDialog() {
      this.resetPasswordVisible = true
      this.resetForm = {
        username: '',
        email: '',
        code: '',
        newPassword: '',
        confirmPassword: '',
        captchaKey: '',
        captchaCode: ''
      }
      // 获取图形验证码
      this.getCaptcha()
    },
    // 获取图形验证码
    getCaptcha() {
      AccountServices.GetCaptcha()
        .then(response => {
          if (response.success) {
            this.captchaImage = response.data.image
            this.captchaKey = response.data.key
            this.resetForm.captchaKey = response.data.key
          } else {
            this.$message.error('获取验证码失败')
          }
        })
        .catch(error => {
          console.error('获取图形验证码错误：', error)
          this.$message.error('获取图形验证码失败')
        })
    },
    // 发送验证码
    sendVerificationCode() {
      if (!this.resetForm.email || !this.resetForm.captchaCode) {
        this.$message.warning('请先输入邮箱地址和图形验证码')
        return
      }

      AccountServices.SendVerificationCode(this.verificationCodeModel)
        .then(response => {
          if (response.success) {
            this.$message.success('验证码已发送到您的邮箱')
            this.startCountdown()
          } else {
            this.$message.error(response.errorMsg || '发送验证码失败，请稍后重试')
            this.getCaptcha()
          }
        })
        .catch(error => {
          console.error('发送验证码请求错误：', error)
          this.$message.error('发送验证码失败：' + (error.message || '网络错误或服务器无响应'))
          this.getCaptcha()
        })
    },
    // 开始倒计时
    startCountdown() {
      this.codeButtonDisabled = true
      this.countdown = 60
      const timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--
          this.codeButtonText = `${this.countdown}秒后重试`
        } else {
          this.codeButtonDisabled = false
          this.codeButtonText = '获取验证码'
          clearInterval(timer)
        }
      }, 1000)
    },
    // 提交重置密码表单
    submitResetForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          AccountServices.ResetPassword(this.resetPasswordModel)
            .then(response => {
              if (response.success) {
                this.$message.success('密码重置成功，请使用新密码登录')
                this.resetPasswordVisible = false
              } else {
                this.$message.error(response.errorMsg || '密码重置失败，请稍后重试')
              }
            })
            .catch(error => {
              console.error('密码重置请求错误：', error) // 调试用
              this.$message.error('密码重置失败：' + (error.message || '网络错误或服务器无响应'))
            })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-card {
  width: 400px;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.links {
  margin-top: 15px;
  text-align: center;
  display: flex;
  justify-content: space-between;
}

.links a {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
}

.links a:hover {
  color: #66b1ff;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-card__header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

h2 {
  margin: 0;
  font-size: 20px;
  color: #303133;
  text-align: center;
}

.code-input {
  display: flex;
  gap: 10px;
}

.code-input .el-input {
  flex: 1;
}

.code-input .el-button {
  width: 120px;
}

.el-dialog {
  border-radius: 8px;
}

.el-dialog__header {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.el-dialog__body {
  padding: 30px 20px;
}

.el-dialog__footer {
  padding: 20px;
  border-top: 1px solid #ebeef5;
}
</style>
