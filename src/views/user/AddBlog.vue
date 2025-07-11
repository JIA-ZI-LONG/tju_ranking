<template>
  <div class="add-blog-container">
    <el-form :model="blogForm" :rules="rules" ref="blogForm" label-width="100px" class="blog-form">
      <!-- 标题 -->
      <el-form-item label="标题" prop="title">
        <el-input v-model="blogForm.title" placeholder="请输入标题"></el-input>
      </el-form-item>

      <!-- 位置信息 -->
      <el-form-item label="位置" prop="location">
        <el-cascader
          v-model="blogForm.location"
          :options="locationModel"
          :props="{ checkStrictly: true }"
          placeholder="请选择食堂和窗口"
          :loading="!locationModel.length">
        </el-cascader>
      </el-form-item>

      <!-- 图片上传 -->
      <el-form-item label="封面图片" prop="imageUrl">
        <el-upload
          class="blog-uploader"
          action="/api/upload"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload">
          <img v-if="blogForm.imageUrl" :src="blogForm.imageUrl" class="uploaded-image">
          <i v-else class="el-icon-plus uploader-icon"></i>
        </el-upload>
        <div class="upload-tip">建议上传尺寸为 800x400 的图片</div>
      </el-form-item>

      <!-- 评分 -->
      <el-form-item label="评分" prop="rating">
        <el-rate
          v-model="blogForm.rating"
          :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
          show-text>
        </el-rate>
      </el-form-item>

      <!-- 价格 -->
      <el-form-item label="人均价格" prop="price">
        <el-input-number 
          v-model="blogForm.price" 
          :min="0" 
          :max="1000"
          :step="1"
          placeholder="请输入人均价格">
        </el-input-number>
        <span class="price-unit">元</span>
      </el-form-item>

      <!-- 内容 -->
      <el-form-item label="内容" prop="content">
        <el-input
          type="textarea"
          v-model="blogForm.content"
          :rows="6"
          placeholder="请详细描述您的用餐体验..."
        ></el-input>
      </el-form-item>

      <!-- 标签 -->
      <el-form-item label="标签" prop="tags">
        <el-select
          v-model="blogForm.tags"
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="请选择或输入标签">
          <el-option
            v-for="tag in tagOptions"
            :key="tag"
            :label="tag"
            :value="tag">
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm" :loading="loading">发布</el-button>
        <el-button @click="resetForm" :disabled="loading">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import BlogServices from '@/service/BlogServices'
import CanteenServices from '@/service/CanteenServices'

export default {
  name: 'AddBlog',
  data() {
    return {
      loading: false,
      blogForm: {
        title: '',
        location: [],
        imageUrl: '',
        rating: 0,
        price: 0,
        content: '',
        tags: [],
        date: new Date().toISOString().split('T')[0]
      },
      locationOptions: [],
      tagOptions: ['美食', '实惠', '环境好', '服务好', '排队少', '特色菜'],
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请选择位置', trigger: 'change' }
        ],
        imageUrl: [
          { required: true, message: '请上传封面图片', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },
          { min: 10, message: '内容不能少于10个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    blogModel() {
      const [canteenId, stallId] = this.blogForm.location
      return {
        title: this.blogForm.title,
        content: this.blogForm.content,
        imageUrl: this.blogForm.imageUrl,
        rating: this.blogForm.rating,
        price: this.blogForm.price,
        tags: this.blogForm.tags,
        canteenId,
        stallId,
        date: this.blogForm.date
      }
    },
    locationModel() {
      return this.locationOptions.map(canteen => ({
        value: canteen.id,
        label: canteen.name,
        children: canteen.stalls.map(stall => ({
          value: stall.id,
          label: stall.name
        }))
      }))
    }
  },
  created() {
    this.loadLocationOptions()
  },
  methods: {
    loadLocationOptions() {
      this.loading = true
      CanteenServices.GetAllCanteens()
        .then(response => {
          if (!response.success || !response.data) {
            throw new Error(response.errorMsg || '获取位置信息失败')
          }
          this.locationOptions = response.data
        })
        .catch(error => {
          console.error('获取位置信息失败:', error)
          this.$message.error('获取位置信息失败：' + (error.message || '网络错误'))
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleUploadSuccess(res) {
      if (!res.success || !res.data?.url) {
        throw new Error(res.errorMsg || '图片上传失败')
      }
      this.blogForm.imageUrl = res.data.url
      this.$message.success('图片上传成功')
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    submitForm() {
      this.$refs.blogForm.validate((valid) => {
        if (!valid) {
          this.$message.error('请完善必填信息！')
          return false
        }

        this.loading = true
        BlogServices.CreateBlog(this.blogModel)
          .then(response => {
            if (!response.success) {
              throw new Error(response.errorMsg || '发布失败')
            }
            this.$message.success('发布成功！')
            this.$router.push('/main/user/blogslist')
          })
          .catch(error => {
            console.error('发布博客失败:', error)
            this.$message.error('发布失败：' + (error.message || '网络错误'))
          })
          .finally(() => {
            this.loading = false
          })
      })
    },
    resetForm() {
      this.$refs.blogForm.resetFields()
      this.blogForm.imageUrl = ''
    }
  }
}
</script>

<style scoped>
.add-blog-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.nav-buttons {
  display: flex;
  gap: 10px;
}

.blog-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.blog-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 300px;
  height: 150px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.blog-uploader:hover {
  border-color: #409EFF;
}

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 300px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.price-unit {
  margin-left: 10px;
  color: #606266;
}
</style>
