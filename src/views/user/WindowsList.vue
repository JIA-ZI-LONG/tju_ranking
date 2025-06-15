<template>
  <div class="window-details-container">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button icon="el-icon-arrow-left" @click="$router.push({ name: 'canteendetails', query: { id: parentCanteenId } })">返回食堂详情</el-button>
    </div>

    <!-- 窗口简介卡片 -->
    <el-card class="window-intro-card" :body-style="{ padding: '0px' }">
      <img :src="window.imageUrl" :alt="window.name" class="window-image-intro">
      <div class="window-content-intro">
        <h1 class="window-name-intro">{{ window.name }}</h1>
        <div class="window-rating-intro">
          <el-rate
            v-model="window.rating"
            disabled
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            show-score>
          </el-rate>
          <span class="window-price-intro">人均 ¥{{ window.averagePrice }}</span>
        </div>
        <p class="window-specialty-intro">特色：{{ window.specialty }}</p>
        <p class="window-description-intro">{{ window.description }}</p>
      </div>
    </el-card>

    <!-- 窗口内所有菜品 -->
    <div class="section-title">窗口菜品</div>
    <div class="dishes-container">
      <el-card
        v-for="dish in window.dishes"
        :key="dish.id"
        class="dish-card"
        :body-style="{ padding: '0px' }">
        <div class="dish-image">
          <img :src="dish.imageUrl" :alt="dish.name">
        </div>
        <div class="dish-info">
          <h3 class="dish-name">{{ dish.name }}</h3>
          <div class="dish-rating">
            <el-rate
              v-model="dish.rating"
              disabled
              :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              show-score>
            </el-rate>
            <span class="dish-price">¥{{ dish.price }}</span>
          </div>
          <p class="dish-description">{{ dish.description }}</p>
          <div class="dish-tags">
            <el-tag
              v-for="tag in dish.tags"
              :key="tag"
              size="small"
              type="info">
              {{ tag }}
            </el-tag>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 用户评价 -->
    <div class="section-title">用户评价</div>
    <div class="reviews-container">
      <el-card v-for="review in window.reviews" :key="review.id" class="review-card">
        <div class="review-header">
          <span class="username">{{ review.username }}</span>
          <el-rate v-model="review.rating" disabled></el-rate>
        </div>
        <div class="review-content">
          <p>{{ review.comment }}</p>
          <span class="review-date">{{ review.date }}</span>
        </div>
      </el-card>
      <div v-if="window.reviews.length === 0" class="no-data-tip">暂无用户评价</div>
    </div>

    <!-- 相关博客 -->
    <div class="section-title">相关博客</div>
    <div class="related-blogs-container">
      <el-card v-for="blog in window.relatedBlogs" :key="blog.id" class="blog-card">
        <div class="blog-header">
          <h3 class="blog-title">{{ blog.title }}</h3>
          <span class="blog-author">{{ blog.author }}</span>
        </div>
        <p class="blog-summary">{{ blog.summary }}</p>
        <div class="blog-meta">
          <span>{{ blog.date }}</span>
          <span>{{ blog.views }} 阅读</span>
        </div>
      </el-card>
      <div v-if="window.relatedBlogs.length === 0" class="no-data-tip">暂无相关博客</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WindowDetails', // 内部组件名称
  data() {
    return {
      window: { // 示例数据，实际应用中会通过路由参数加载
        id: 1,
        name: '川湘风味',
        specialty: '麻辣香锅',
        imageUrl: 'https://via.placeholder.com/600x400?text=川湘风味窗口',
        rating: 4.8,
        averagePrice: 18,
        description: '以麻辣香锅为主打，提供地道川味体验，麻辣鲜香，深受喜爱。',
        dishes: [
          {
            id: 1,
            name: '麻辣香锅',
            imageUrl: 'https://via.placeholder.com/300x200?text=麻辣香锅',
            rating: 4.8,
            price: 35,
            description: '正宗川式麻辣香锅，配料丰富，麻辣鲜香。',
            tags: ['招牌菜', '麻辣', '人气']
          },
          {
            id: 2,
            name: '水煮鱼',
            imageUrl: 'https://via.placeholder.com/300x200?text=水煮鱼',
            rating: 4.6,
            price: 45,
            description: '新鲜草鱼片，配以豆芽、青菜，麻辣鲜香。',
            tags: ['招牌菜', '麻辣', '鱼类']
          },
          {
            id: 3,
            name: '毛血旺',
            imageUrl: 'https://via.placeholder.com/300x200?text=毛血旺',
            rating: 4.7,
            price: 40,
            description: '经典川菜，味道浓郁，食材丰富。',
            tags: ['经典', '辣']
          }
        ],
        reviews: [
          {
            id: 1,
            username: '张三',
            rating: 5,
            comment: '麻辣香锅味道超赞，每次来必点！',
            date: '2023-03-15'
          },
          {
            id: 2,
            username: '李四',
            rating: 4,
            comment: '水煮鱼很新鲜，就是有点辣，不过瘾！',
            date: '2023-03-10'
          }
        ],
        relatedBlogs: [
          {
            id: 1,
            title: '探秘学一食堂川湘风味：麻辣诱惑无法抵挡',
            author: '美食探险家',
            summary: '详细介绍了川湘风味的招牌菜和就餐体验。',
            date: '2023-02-28',
            views: 1200
          },
          {
            id: 2,
            title: '在食堂也能吃到正宗水煮鱼！',
            author: '吃货小分队',
            summary: '分享了水煮鱼的口味和如何选择配菜的心得。',
            date: '2023-03-05',
            views: 850
          }
        ]
      },
      parentCanteenId: null // 用于返回食堂详情页
    }
  },
  created() {
    // 获取窗口ID和父级食堂ID
    const windowId = this.$route.query.windowId;
    this.parentCanteenId = this.$route.query.canteenId;

    if (windowId) {
      // 实际应用中，这里会根据windowId调用API获取窗口的详细数据
      // 暂时使用示例数据，并根据windowId模拟加载
      // 假设你有一个方法可以从某个数据源根据ID获取窗口数据
      this.loadWindowData(windowId);
    } else {
      this.$message.error('未找到窗口信息');
      this.$router.push({ name: 'canteenslist' }); // 如果没有windowId，返回食堂列表
    }
  },
  methods: {
    loadWindowData(windowId) {
      // 模拟根据windowId加载数据
      // 在实际项目中，你会在这里发起API请求
      // 为了演示，我们直接使用data中的示例window数据
      console.log('加载窗口ID为：', windowId, '的数据');
      // 如果你的后端API能够根据windowId获取数据，这里会是axios.get('/api/windows/' + windowId)
      // 假设我们有多个窗口数据，你需要根据windowId找到对应的窗口
      // 例如：
      // const allWindowsData = [
      //   { id: 1, name: '川湘风味', ... },
      //   { id: 2, name: '北方面食', ... }
      // ];
      // this.window = allWindowsData.find(w => w.id == windowId);
      // 目前为了快速演示，直接使用一个固定的window数据
      // 如果需要模拟不同窗口的数据，可以在data中定义一个窗口数组，然后在这里查找
    }
  }
}
</script>

<style scoped>
.window-details-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.back-button {
  margin-bottom: 20px;
}

/* 窗口简介卡片样式，参考食堂详情页中的窗口卡片 */
.window-intro-card {
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.window-image-intro {
  width: 100%;
  height: 300px; /* 调整图片高度 */
  object-fit: cover;
  display: block;
}

.window-content-intro {
  padding: 20px;
}

.window-name-intro {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 15px 0;
  color: #303133;
}

.window-rating-intro {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.window-price-intro {
  margin-left: 15px;
  color: #f56c6c;
  font-size: 20px;
  font-weight: 500;
}

.window-specialty-intro {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.window-description-intro {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

/* 各个部分的标题 */
.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-top: 40px;
  margin-bottom: 20px;
  border-left: 5px solid #409EFF;
  padding-left: 10px;
}

/* 菜品容器样式 */
.dishes-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

/* 菜品卡片样式 (沿用之前CanteenDetails.vue中的) */
.dish-card {
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.dish-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 18px 0 rgba(0,0,0,0.1);
}

.dish-image {
  height: 180px; /* 调整菜品图片高度 */
  overflow: hidden;
}

.dish-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dish-info {
  padding: 15px;
}

.dish-name {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.dish-rating {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.dish-price {
  color: #f56c6c;
  font-size: 16px;
  font-weight: 500;
}

.dish-description {
  color: #606266;
  font-size: 14px;
  line-height: 1.4;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.dish-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.dish-tags .el-tag {
  margin-right: 5px;
}

/* 用户评价样式 */
.reviews-container {
  padding: 10px 0;
}

.review-card {
  margin-bottom: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.username {
  font-weight: 600;
  color: #303133;
}

.review-content p {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  margin-bottom: 8px;
}

.review-date {
  font-size: 12px;
  color: #909399;
  text-align: right;
  display: block;
}

/* 相关博客样式 */
.related-blogs-container {
  padding: 10px 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.blog-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
  transition: all 0.3s;
  cursor: pointer;
}

.blog-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px 0 rgba(0,0,0,0.1);
}

.blog-header {
  margin-bottom: 10px;
}

.blog-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 5px 0;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.blog-author {
  font-size: 14px;
  color: #909399;
}

.blog-summary {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}

.blog-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.no-data-tip {
  text-align: center;
  color: #909399;
  padding: 20px;
  font-size: 16px;
}
</style>
