<template>
  <div class="canteen-details-container">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button icon="el-icon-arrow-left" @click="$router.push({ name: 'canteenslist' })">返回食堂列表</el-button>
    </div>

    <!-- 食堂简介卡片 -->
    <el-card class="canteen-intro-card">
      <div class="intro-header">
        <div class="intro-left">
          <h1 class="canteen-name">{{ canteen.name }}</h1>
          <div class="canteen-tags">
            <el-tag size="medium" type="info">{{ canteen.campus }}</el-tag>
            <el-tag size="medium" type="success" v-if="canteen.isOpen">营业中</el-tag>
            <el-tag size="medium" type="danger" v-else>已关闭</el-tag>
          </div>
        </div>
        <div class="intro-right">
          <div class="rating-price">
            <el-rate
              v-model="canteen.rating"
              disabled
              :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              show-score>
            </el-rate>
            <span class="average-price">人均 ¥{{ canteen.averagePrice }}</span>
          </div>
          <div class="business-hours">
            <i class="el-icon-time"></i>
            <span>{{ canteen.businessHours }}</span>
          </div>
          <div class="location">
            <i class="el-icon-location"></i>
            <span>{{ canteen.location }}</span>
          </div>
        </div>
      </div>
      <div class="intro-description">
        <p>{{ canteen.description }}</p>
      </div>
    </el-card>

    <!-- 窗口和菜品展示 -->
    <div class="windows-section">
      <el-tabs v-model="activeWindow" type="card">
        <el-tab-pane
          v-for="window in canteen.windows"
          :key="window.name"
          :label="window.name"
          :name="window.name">
          <div class="window-info-container">
            <el-card class="window-card" :body-style="{ padding: '0px' }" @click.native="goToWindowDetails(window)">
              <img :src="window.imageUrl" :alt="window.name" class="window-image">
              <div class="window-content">
                <h3 class="window-name-card">{{ window.name }}</h3>
                <div class="window-rating">
                  <el-rate
                    v-model="window.rating"
                    disabled
                    :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                    show-score>
                  </el-rate>
                  <span class="window-price">人均 ¥{{ window.averagePrice }}</span>
                </div>
                <p class="window-specialty-description">{{ window.specialty }} - {{ window.description }}</p>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CanteenDetails',
  data() {
    return {
      activeWindow: '',
      canteen: {
        id: 1,
        name: '学一食堂',
        campus: '卫津路校区',
        rating: 4.5,
        averagePrice: 15,
        isOpen: true,
        businessHours: '06:30-22:00',
        location: '卫津路校区中心位置',
        description: '天津大学最受欢迎的食堂之一，提供多样化的菜品选择，环境整洁，价格实惠。',
        windows: [
          {
            name: '川湘风味',
            specialty: '麻辣香锅',
            imageUrl: 'https://via.placeholder.com/300x200?text=川湘风味',
            rating: 4.8,
            averagePrice: 18,
            description: '以麻辣香锅为主打，提供地道川味体验。'
          },
          {
            name: '北方面食',
            specialty: '手工拉面',
            imageUrl: 'https://via.placeholder.com/300x200?text=北方面食',
            rating: 4.7,
            averagePrice: 15,
            description: '提供传统北方手工面食，口感劲道。'
          },
          {
            name: '粤式茶点',
            specialty: '虾饺皇',
            imageUrl: 'https://via.placeholder.com/300x200?text=粤式茶点',
            rating: 4.9,
            averagePrice: 20,
            description: '精致粤式茶点，品味岭南风情。'
          }
        ]
      }
    }
  },
  created() {
    // 获取食堂ID
    const canteenId = this.$route.query.id
    if (canteenId) {
      // 这里应该调用API获取食堂数据
      // 暂时使用示例数据
      this.loadCanteenData(canteenId)
    } else {
      this.$message.error('未找到食堂信息')
      this.$router.push({ name: 'canteenslist' })
    }
  },
  methods: {
    loadCanteenData(canteenId) {
      // 这里应该调用API获取食堂数据
      // 暂时使用示例数据
      const canteenData = {
        id: canteenId,
        name: '学一食堂',
        campus: '卫津路校区',
        rating: 4.5,
        averagePrice: 15,
        isOpen: true,
        businessHours: '06:30-22:00',
        location: '卫津路校区中心位置',
        description: '天津大学最受欢迎的食堂之一，提供多样化的菜品选择，环境整洁，价格实惠。',
        windows: [
          {
            name: '川湘风味',
            specialty: '麻辣香锅',
            imageUrl: 'https://via.placeholder.com/300x200?text=川湘风味',
            rating: 4.8,
            averagePrice: 18,
            description: '以麻辣香锅为主打，提供地道川味体验。'
          },
          {
            name: '北方面食',
            specialty: '手工拉面',
            imageUrl: 'https://via.placeholder.com/300x200?text=北方面食',
            rating: 4.7,
            averagePrice: 15,
            description: '提供传统北方手工面食，口感劲道。'
          },
          {
            name: '粤式茶点',
            specialty: '虾饺皇',
            imageUrl: 'https://via.placeholder.com/300x200?text=粤式茶点',
            rating: 4.9,
            averagePrice: 20,
            description: '精致粤式茶点，品味岭南风情。'
          }
        ]
      }
      this.canteen = canteenData
      // 设置默认选中的窗口
      if (this.canteen.windows.length > 0) {
        this.activeWindow = this.canteen.windows[0].name
      }
    },
    goToWindowDetails(window) {
      this.$router.push({
        name: 'windowdetails',
        query: {
          windowId: window.id,
          canteenId: this.canteen.id // 传递父级食堂ID
        }
      });
    }
  }
}
</script>

<style scoped>
.canteen-details-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.back-button {
  margin-bottom: 20px;
}

.canteen-intro-card {
  margin-bottom: 30px;
}

.intro-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.intro-left {
  flex: 1;
}

.canteen-name {
  margin: 0 0 15px 0;
  font-size: 28px;
  font-weight: 600;
}

.canteen-tags {
  margin-bottom: 15px;
}

.canteen-tags .el-tag {
  margin-right: 10px;
}

.intro-right {
  text-align: right;
}

.rating-price {
  margin-bottom: 10px;
}

.average-price {
  margin-left: 15px;
  color: #f56c6c;
  font-size: 18px;
  font-weight: 500;
}

.business-hours,
.location {
  margin-top: 8px;
  color: #606266;
}

.business-hours i,
.location i {
  margin-right: 5px;
}

.intro-description {
  color: #606266;
  line-height: 1.6;
}

.windows-section {
  margin-top: 30px;
}

.window-info-container {
  padding: 20px 0;
  display: flex; /* 使用flexbox布局 */
  justify-content: center; /* 居中显示 */
}

.window-card {
  width: 300px; /* 固定卡片宽度 */
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  transition: all 0.3s;
  cursor: pointer;
}

.window-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 18px 0 rgba(0,0,0,0.15);
}

.window-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.window-content {
  padding: 15px;
}

.window-name-card {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 10px 0;
  color: #303133;
}

.window-rating {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.window-price {
  margin-left: 10px;
  color: #f56c6c;
  font-size: 16px;
  font-weight: 500;
}

.window-specialty-description {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  margin-top: 10px;
  min-height: 42px; /* 确保有足够高度，防止内容过短时卡片变形 */
}
</style>
