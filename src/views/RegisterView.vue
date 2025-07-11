<template>
  <div class="register-container">
    <el-card class="register-card">
      <div slot="header" class="clearfix">
        <h2>用户注册</h2>
      </div>
      <el-form :model="registerForm" :rules="rules" ref="registerForm" label-width="80px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="registerForm.nickname" placeholder="请输入昵称" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="el-icon-message"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 图形验证码 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="图形验证码" prop="captchaCode">
              <div class="captcha-input">
                <el-input v-model="registerForm.captchaCode" placeholder="请输入图形验证码" prefix-icon="el-icon-key"></el-input>
                <div class="captcha-image" @click="getCaptcha" v-if="captchaImage">
                  <img :src="captchaImage" alt="验证码" style="cursor: pointer; height: 32px;">
                </div>
                <el-button v-else type="primary" size="small" @click="getCaptcha">获取验证码</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱验证码" prop="code">
              <div class="email-code-input">
                <el-input v-model="registerForm.code" placeholder="请输入邮箱验证码" prefix-icon="el-icon-message"></el-input>
                <el-button type="primary" size="small" @click="sendVerificationCode" :disabled="codeButtonDisabled">
                  {{ codeButtonText }}
                </el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="registerForm.gender">
                <el-radio :label="0">女</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">未知</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="校区" prop="campus">
              <el-select v-model="registerForm.campus" placeholder="请选择校区" style="width: 100%">
                <el-option label="北洋园校区" value="北洋园校区"></el-option>
                <el-option label="卫津路校区" value="卫津路校区"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>



        <el-form-item>
          <el-button type="primary" @click="submitForm('registerForm')">注册</el-button>
          <el-button @click="resetForm('registerForm')">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="links">
        <router-link to="/login">已有账号？去登录</router-link>
        <router-link to="/">返回首页</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import AccountServices from '@/service/AccountServices'

export default {
  name: 'RegisterView',
  data() {
    // 自定义验证规则
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.registerForm.confirmPassword !== '') {
          this.$refs.registerForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        nickname: '',
        password: '',
        confirmPassword: '',
        email: '',
        code: '',
        gender: 2,
        campus: '',
        captchaCode: '',
        captchaKey: ''
      },
      rules: {
        nickname: [
          { required: false, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, validator: validatePass, trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
          { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
        ],
        captchaCode: [
          { required: true, message: '请输入图形验证码', trigger: 'blur' }
        ],
        gender: [
          { required: false, message: '请选择性别', trigger: 'change' }
        ],
        campus: [
          { required: false, message: '请选择校区', trigger: 'change' }
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
    verificationCodeModel() {
      return {
        email: this.registerForm.email,
        type: 1,
        captchaKey: this.registerForm.captchaKey,
        captchaCode: this.registerForm.captchaCode
      }
    },
    registerModel() {
      return {
        email: this.registerForm.email,
        password: this.registerForm.password,
        confirmPassword: this.registerForm.confirmPassword,
        code: this.registerForm.code,
        nickname: this.registerForm.nickname,
        gender: this.registerForm.gender,
        campus: this.registerForm.campus
      }
    }
  },



  methods: {
    // 获取图形验证码
    getCaptcha() {
      AccountServices.GetCaptcha()
        .then(response => {
          if (response.success) {
            this.captchaImage = response.data.image
            this.captchaKey = response.data.key
            this.registerForm.captchaKey = response.data.key
          } else {
            this.$message.error('获取图形验证码失败')
          }
        })
        .catch(error => {
          console.error('获取图形验证码错误：', error)
          this.$message.error('获取图形验证码失败')
        })
    },
    // 发送验证码
    sendVerificationCode() {
      if (!this.registerForm.email || !this.registerForm.captchaCode) {
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
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          AccountServices.Register(this.registerModel)
            .then(response => {
              if (response.success) {
                this.$message.success('注册成功！')
                this.$router.push('/login')
              } else {
                this.$message.error(response.errorMsg || '注册失败，请稍后重试。')
              }
            })
            .catch(error => {
              console.error('注册请求错误：', error)
              this.$message.error('注册失败：' + (error.message || '网络错误或服务器无响应。'))
            })
        } else {
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.register-card {
  width: 800px;
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

.email-input {
  display: flex;
  gap: 10px;
}

.email-input .el-input {
  flex: 1;
}

.email-input .el-button {
  width: 120px;
}

.el-radio-group {
  display: flex;
  gap: 20px;
}

.el-select {
  width: 100%;
}
</style>
