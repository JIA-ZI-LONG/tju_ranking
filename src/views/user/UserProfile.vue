<template>
  <div class="user-profile">
    <!-- 顶部用户信息卡片 -->
    <el-card class="user-card">
      <div class="user-header">
        <div class="user-avatar">
          <el-avatar :size="80" :src="userInfo.avatar || defaultAvatar">
            <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
          </el-avatar>
          <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess">
            <el-button size="small" type="text">更换头像</el-button>
          </el-upload>
        </div>
        <div class="user-info">
          <h2>{{ userInfo.nickname || userInfo.username }}</h2>
          <p class="user-id">ID: {{ userInfo.username }}</p>
          <div class="user-stats">
            <div class="stat-item">
              <span class="stat-num">{{ userInfo.reviewCount || 0 }}</span>
              <span class="stat-label">点评</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ userInfo.followCount || 0 }}</span>
              <span class="stat-label">关注</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ userInfo.fansCount || 0 }}</span>
              <span class="stat-label">粉丝</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 快捷导航集成在用户卡片内部 -->
      <el-row :gutter="10" class="quick-nav-integrated">
        <el-col :span="6">
          <router-link to="/main/user/disheslist" class="nav-item-small">
            <i class="el-icon-dish"></i>
            <span>菜品</span>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/main/user/canteenslist" class="nav-item-small">
            <i class="el-icon-food"></i>
            <span>食堂</span>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/main/user/blogslist" class="nav-item-small">
            <i class="el-icon-notebook-2"></i>
            <span>博客</span>
          </router-link>
        </el-col>
        <el-col :span="6">
          <router-link to="/main/home" class="nav-item-small">
            <i class="el-icon-house"></i>
            <span>首页</span>
          </router-link>
        </el-col>
      </el-row>
    </el-card>

    <!-- 用户详细信息 -->
    <el-card class="info-card">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="userInfo" label-width="80px" class="info-form" size="small">
            <el-form-item label="用户名">
              <span>{{ userInfo.username }}</span>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userInfo.nickname" placeholder="请输入昵称" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userInfo.gender">
                <el-radio :label="0">女</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">未知</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="校区">
              <el-select v-model="userInfo.campus" placeholder="请选择校区" style="width: 100%">
                <el-option label="北洋园校区" value="北洋园校区"></el-option>
                <el-option label="卫津路校区" value="卫津路校区"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userInfo.email" placeholder="请输入邮箱" prefix-icon="el-icon-message"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveUserInfo" size="small">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="我的点评" name="reviews">
          <div class="review-list-compact">
            <div v-for="review in limitedUserReviews" :key="review.id" class="review-item-compact">
              <div class="review-header-compact">
                <span class="restaurant-name-compact">{{ review.restaurantName }}</span>
                <span class="review-time-compact">{{ review.createTime }}</span>
              </div>
              <div class="review-content-compact">{{ review.content }}</div>
              <div class="review-images-compact" v-if="review.images && review.images.length">
                <el-image 
                  v-for="(img, index) in review.images" 
                  :key="index"
                  :src="img"
                  :preview-src-list="review.images"
                  fit="cover"
                  class="review-image-compact">
                </el-image>
              </div>
            </div>
            <div v-if="userReviews.length > limitedReviewCount" class="view-more-reviews">
                <el-button type="text" @click="viewAllReviews">查看全部点评</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserProfile',
  data() {
    return {
      activeTab: 'basic',
      defaultAvatar: 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png',
      userInfo: {
        username: 'user123',
        nickname: '美食达人',
        gender: 1,
        campus: '北洋园校区',
        email: 'user@example.com',
        avatar: '',
        reviewCount: 28,
        followCount: 156,
        fansCount: 89
      },
      userReviews: [
        {
          id: 1,
          restaurantName: '学一食堂',
          createTime: '2024-03-15',
          content: '今天的红烧肉特别好吃，肉质鲜嫩，入口即化！',
          images: [
            'https://example.com/image1.jpg',
            'https://example.com/image2.jpg'
          ]
        },
        {
          id: 2,
          restaurantName: '学二食堂',
          createTime: '2024-03-14',
          content: '推荐这里的麻辣香锅，料足味美！',
          images: []
        },
        { // 增加一个模拟点评用于演示限定数量
            id: 3,
            restaurantName: '学三食堂',
            createTime: '2024-03-13',
            content: '窗口的包子很不错，每次来都排队！',
            images: []
        }
      ],
      limitedReviewCount: 2 // 默认只显示2条点评
    }
  },
  computed: {
    limitedUserReviews() {
        return this.userReviews.slice(0, this.limitedReviewCount);
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.userInfo.avatar = URL.createObjectURL(file.raw)
    },
    saveUserInfo() {
      // TODO: 调用保存用户信息接口
      this.$message.success('保存成功')
    },
    viewAllReviews() {
        // TODO: 实现查看全部点评的逻辑，例如跳转到单独的点评列表页
        this.$message.info('查看全部点评功能待实现')
    }
  }
}
</script>

<style scoped>
.user-profile {
  padding: 15px; /* 减小页面内边距 */
  background-color: #f5f7fa;
  /* min-height 属性可以移除或根据需要调整，让内容自适应 */
}

.user-card {
  margin-bottom: 15px; /* 减小卡片间距 */
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08); /* 更轻的阴影 */
}

.user-header {
  display: flex;
  align-items: center;
  padding: 20px; /* 减小头像区域内边距 */
}

.user-avatar {
  margin-right: 20px; /* 减小头像右侧间距 */
  text-align: center;
}

.user-info {
  flex: 1;
}

.user-info h2 {
  margin: 0 0 8px 0; /* 减小标题间距 */
  font-size: 24px; /* 略微减小字体 */
  color: #303133;
}

.user-id {
  color: #909399;
  margin: 0 0 15px 0; /* 减小间距 */
  font-size: 13px; /* 略微减小字体 */
}

.user-stats {
  display: flex;
  gap: 25px; /* 减小统计项间距 */
}

.stat-item {
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 20px; /* 略微减小字体 */
  font-weight: bold;
  color: #303133;
}

.stat-label {
  color: #909399;
  font-size: 13px; /* 略微减小字体 */
  margin-top: 3px; /* 减小间距 */
}

/* 快捷导航集成样式 */
.quick-nav-integrated {
    padding: 10px 0;
    border-top: 1px solid #ebeef5; /* 添加分隔线 */
    margin-top: 10px;
}

.nav-item-small {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  color: #606266; /* 颜色更柔和 */
  padding: 8px 0; /* 减小内边距 */
  transition: all 0.3s ease;
  border-radius: 4px;
}

.nav-item-small:hover {
  background-color: #ecf5ff; /* 悬停背景更浅 */
  color: #409EFF;
  transform: translateY(-1px); /* 悬停效果更小 */
}

.nav-item-small i {
  font-size: 22px; /* 略微减小图标尺寸 */
  margin-bottom: 5px; /* 减小图标与文字间距 */
}

.nav-item-small span {
  font-size: 13px; /* 减小字体 */
}


.info-card {
  margin-bottom: 15px; /* 减小卡片间距 */
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08); /* 更轻的阴影 */
}

.info-form {
  max-width: 550px; /* 减小表单最大宽度 */
  margin: 20px auto; /* 减小表单外边距 */
  padding: 0 15px; /* 减小表单内边距 */
}

.el-form-item {
    margin-bottom: 15px; /* 减小表单项间距 */
}

/* 点评列表紧凑样式 */
.review-list-compact {
  padding: 15px; /* 减小内边距 */
}

.review-item-compact {
  padding: 15px 0; /* 减小内边距 */
  border-bottom: 1px solid #ebeef5;
}

.review-item-compact:last-child {
  border-bottom: none;
}

.review-header-compact {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px; /* 减小间距 */
}

.restaurant-name-compact {
  font-weight: bold;
  color: #303133;
  font-size: 15px; /* 略微减小字体 */
}

.review-time-compact {
  color: #909399;
  font-size: 13px; /* 略微减小字体 */
}

.review-content-compact {
  margin-bottom: 10px; /* 减小间距 */
  line-height: 1.5; /* 略微减小行高 */
  color: #606266;
  font-size: 14px;
}

.review-images-compact {
  display: flex;
  gap: 8px; /* 减小图片间距 */
  margin-top: 10px; /* 减小图片顶部间距 */
}

.review-image-compact {
  width: 90px; /* 减小图片尺寸 */
  height: 90px; /* 减小图片尺寸 */
  border-radius: 6px; /* 略微减小圆角 */
  object-fit: cover;
  transition: all 0.3s;
}

.review-image-compact:hover {
  transform: scale(1.03); /* 悬停效果更小 */
}

.view-more-reviews {
    text-align: center;
    padding-top: 10px;
}

/* 覆盖 Element UI 默认样式以实现紧凑设计 */
.el-tabs--border-card {
  box-shadow: none;
  border: none;
}

.el-tabs__header {
  background-color: #fbfbfb; /* 标签页头部背景更浅 */
  border-bottom: 1px solid #dcdfe6;
}

.el-tabs__item {
  height: 45px; /* 略微减小标签页高度 */
  line-height: 45px;
  font-size: 14px; /* 略微减小字体 */
}

.el-tabs__item.is-active {
  color: #409EFF;
  font-weight: bold;
}
</style> 