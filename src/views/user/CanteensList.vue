<template>
  <div class="canteens-container">
    <!-- 顶部搜索和筛选区域 -->
    <div class="search-section">
      <el-input
        v-model="searchQuery"
        placeholder="搜索食堂名称"
        prefix-icon="el-icon-search"
        clearable
        @clear="handleSearch"
        @input="handleSearch"
        class="search-input">
      </el-input>
      
      <el-select v-model="selectedCampus" placeholder="选择校区" @change="handleSearch" class="filter-select">
        <el-option
          v-for="item in campusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="sortBy" placeholder="排序方式" @change="handleSearch" class="filter-select">
        <el-option label="评分最高" value="rating"></el-option>
        <el-option label="价格最低" value="price"></el-option>
        <el-option label="距离最近" value="distance"></el-option>
      </el-select>
    </div>

    <!-- 食堂列表 -->
    <el-row :gutter="20" class="canteen-list">
      <el-col :span="8" v-for="canteen in filteredCanteens" :key="canteen.id">
        <!--非原生html组件，不能用v-on直接绑定click事件，加native-->
        <el-card :body-style="{ padding: '0px' }" class="canteen-card" @click.native="goToCanteenDetail(canteen)">
          <img :src="canteen.imageUrl" class="canteen-image">
          <div class="canteen-content">
            <h3 class="canteen-name">{{ canteen.name }}</h3>
            <div class="canteen-info">
              <el-rate
                v-model="canteen.rating"
                disabled
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                show-score>
              </el-rate>
              <span class="canteen-price">人均 ¥{{ canteen.averagePrice }}</span>
            </div>
            <div class="canteen-tags">
              <el-tag size="small" type="info">{{ canteen.campus }}</el-tag>
              <el-tag size="small" type="success" v-if="canteen.isOpen">营业中</el-tag>
              <el-tag size="small" type="danger" v-else>已关闭</el-tag>
            </div>
            <div class="canteen-description">{{ canteen.description }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'CanteensList',
  data() {
    return {
      searchQuery: '',
      selectedCampus: '',
      sortBy: 'rating',
      campusOptions: [
        { value: '卫津路校区', label: '卫津路校区' },
        { value: '北洋园校区', label: '北洋园校区' }
      ],
      canteens: [
        {
          id: 1,
          name: '学一食堂',
          campus: '卫津路校区',
          imageUrl: 'https://via.placeholder.com/400x300?text=Canteen+1',
          rating: 4.5,
          averagePrice: 15,
          isOpen: true,
          businessHours: '06:30-22:00',
          location: '卫津路校区中心位置',
          description: '天津大学最受欢迎的食堂之一，提供多样化的菜品选择，环境整洁，价格实惠。',
          windows: [
            { name: '川湘风味', specialty: '麻辣香锅', price: '15-20元' },
            { name: '北方面食', specialty: '手工拉面', price: '10-15元' },
            { name: '粤式茶点', specialty: '虾饺皇', price: '20-30元' }
          ]
        },
        {
          id: 2,
          name: '学二食堂',
          campus: '卫津路校区',
          imageUrl: 'https://via.placeholder.com/400x300?text=Canteen+2',
          rating: 4.2,
          averagePrice: 12,
          isOpen: true,
          businessHours: '06:30-21:30',
          location: '卫津路校区东区',
          description: '以北方菜系为主，特色面食和饺子深受学生喜爱。',
          windows: [
            { name: '北方面食', specialty: '手工水饺', price: '10-15元' },
            { name: '东北菜', specialty: '锅包肉', price: '15-20元' }
          ]
        },
        {
          id: 3,
          name: '学三食堂',
          campus: '北洋园校区',
          imageUrl: 'https://via.placeholder.com/400x300?text=Canteen+3',
          rating: 4.3,
          averagePrice: 18,
          isOpen: true,
          businessHours: '07:00-22:00',
          location: '北洋园校区中心',
          description: '环境优雅，提供多样化的菜品选择，包括西餐和特色小吃。',
          windows: [
            { name: '西式简餐', specialty: '意大利面', price: '20-25元' },
            { name: '特色小吃', specialty: '煎饼果子', price: '8-12元' }
          ]
        }
      ]
    }
  },
  computed: {
    filteredCanteens() {
      let result = this.canteens

      // 搜索过滤
      if (this.searchQuery) {
        result = result.filter(canteen => 
          canteen.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        )
      }

      // 校区过滤
      if (this.selectedCampus) {
        result = result.filter(canteen => 
          canteen.campus === this.selectedCampus
        )
      }

      // 排序
      result = [...result].sort((a, b) => {
        switch (this.sortBy) {
          case 'rating':
            return b.rating - a.rating
          case 'price':
            return a.averagePrice - b.averagePrice
          case 'distance':
            return 0 // 这里可以添加距离排序逻辑
          default:
            return 0
        }
      })

      return result
    }
  },
  methods: {
    handleSearch() {
      // 搜索逻辑已通过计算属性实现
    },
    goToCanteenDetail(canteen) {
      this.$router.push({
        path: '/main/user/canteen-details',
        query: { id: canteen.id }
      })
    }
  }
}
</script>

<style scoped>
.canteens-container {
  padding: 20px;
}

.search-section {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.search-input {
  width: 300px;
}

.filter-select {
  width: 150px;
}

.canteen-list {
  margin-top: 20px;
}

.canteen-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.canteen-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.canteen-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.canteen-content {
  padding: 15px;
}

.canteen-name {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 500;
}

.canteen-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.canteen-price {
  color: #f56c6c;
  font-size: 16px;
  font-weight: 500;
}

.canteen-tags {
  margin-bottom: 10px;
}

.canteen-tags .el-tag {
  margin-right: 8px;
}

.canteen-description {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}
</style>
