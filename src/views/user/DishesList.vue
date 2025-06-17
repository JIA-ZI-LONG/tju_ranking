<template>
  <div class="dishes-list-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>菜品列表</h1>
    </div>

    <!-- 搜索和筛选区域 -->
    <el-card class="search-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="searchQuery"
            placeholder="搜索菜品名称或描述"
            prefix-icon="el-icon-search"
            @keyup.enter.native="handleSearch"
            clearable>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="selectedCategory" placeholder="选择分类" clearable style="width: 100%">
            <el-option
              v-for="category in categoryOptions"
              :key="category.value"
              :label="category.label"
              :value="category.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="sortBy" placeholder="排序方式" style="width: 100%">
            <el-option label="评分从高到低" value="rating"></el-option>
            <el-option label="价格从低到高" value="price"></el-option>
            <el-option label="名称排序" value="name"></el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch" icon="el-icon-search">搜索</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 菜品列表 -->
    <div v-loading="loading" style="min-height: 400px;">
      <div v-if="filteredDishes.length === 0 && !loading" class="empty-state">
        <el-empty description="暂无菜品数据"></el-empty>
      </div>
      <el-row :gutter="20" class="dishes-list" v-else>
        <el-col :span="6" v-for="dish in filteredDishes" :key="dish.id">
          <el-card :body-style="{ padding: '0px' }" class="dish-card" @click.native="goToDishDetail(dish)">
            <img :src="dish.imageUrl" :alt="dish.name" class="dish-image">
            <div class="dish-content">
              <h3 class="dish-name">{{ dish.name }}</h3>
              <div class="dish-info">
                <el-rate
                  v-model="dish.rating"
                  disabled
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                  show-score>
                </el-rate>
                <span class="dish-price">¥{{ dish.price }}</span>
              </div>
              <div class="dish-tags">
                <el-tag size="small" type="info">{{ dish.category }}</el-tag>
                <el-tag size="small" type="warning" v-if="dish.isSpicy">辣</el-tag>
              </div>
              <div class="dish-description">{{ dish.description }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 分页 -->
    <div class="pagination-container" v-if="total > 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[12, 24, 48, 96]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import DishServices from '@/service/DishServices'

export default {
  name: 'DishesList',
  data() {
    return {
      searchQuery: '',
      selectedCategory: '',
      sortBy: 'rating',
      categoryOptions: [
        { value: '', label: '全部分类' },
        { value: '主食', label: '主食' },
        { value: '汤品', label: '汤品' },
        { value: '小菜', label: '小菜' },
        { value: '甜品', label: '甜品' },
        { value: '饮品', label: '饮品' }
      ],
      dishes: [],
      loading: false,
      currentPage: 1,
      pageSize: 12,
      total: 0
    }
  },
  computed: {
    dishesModel() {
      return (Array.isArray(this.dishes) ? this.dishes : [])
        .map(dish => ({
          id: dish.id,
          name: dish.name,
          imageUrl: dish.imageUrl || `https://via.placeholder.com/300x200?text=${encodeURIComponent(dish.name)}`,
          rating: dish.score || 0,
          price: dish.price || 0,
          category: dish.category || '未分类',
          isSpicy: dish.isSpicy || false,
          description: dish.introduction || '暂无介绍'
        }))
    },
    filteredDishes() {
      let filtered = [...this.dishesModel]

      // 分类筛选
      if (this.selectedCategory) {
        filtered = filtered.filter(dish => dish.category === this.selectedCategory)
      }

      // 排序
      return filtered.sort((a, b) => {
        switch (this.sortBy) {
          case 'rating':
            return b.rating - a.rating
          case 'price':
            return a.price - b.price
          case 'name':
            return a.name.localeCompare(b.name)
          default:
            return 0
        }
      })
    },
    searchParams() {
      return {
        current: this.currentPage,
        size: this.pageSize,
        name: this.searchQuery || undefined,
        category: this.selectedCategory || undefined
      }
    }
  },
  mounted() {
    const searchParam = this.$route.query.search
    if (searchParam) {
      this.searchQuery = searchParam
    }
    this.loadDishes()
  },
  methods: {
    loadDishes() {
      this.loading = true
      DishServices.SearchDishes(this.searchParams)
        .then(response => {
          if (!response.success || !response.data) {
            throw new Error(response.errorMsg || '获取菜品列表失败')
          }
          const data = response.data
          this.dishes = Array.isArray(data) ? data : (data.records || [])
          this.total = data.total || this.dishes.length
        })
        .catch(error => {
          console.error('加载菜品列表失败：', error)
          this.$message.error('加载菜品列表失败：' + (error.message || '网络错误'))
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleSearch() {
      this.currentPage = 1
      this.loadDishes()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.loadDishes()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadDishes()
    },
    goToDishDetail(dish) {
      if (!dish?.id) {
        this.$message.error('菜品信息不完整')
        return
      }
      this.$router.push({
        path: '/main/user/dish-details',
        query: { id: dish.id }
      })
    }
  },
  watch: {
    selectedCategory() {
      this.handleSearch()
    },
    sortBy() {
      // 排序通过计算属性实现，无需重新加载数据
    }
  }
}
</script>

<style scoped>
.dishes-list-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.search-card {
  margin-bottom: 20px;
}

.dishes-list {
  margin-top: 20px;
}

.dish-card {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  cursor: pointer;
}

.dish-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 18px 0 rgba(0,0,0,0.15);
}

.dish-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.dish-content {
  padding: 15px;
}

.dish-name {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dish-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.dish-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: 600;
}

.dish-tags {
  margin-bottom: 10px;
}

.dish-tags .el-tag {
  margin-right: 8px;
}

.dish-description {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  overflow: hidden;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #909399;
  font-size: 16px;
}

.pagination-container {
  margin-top: 30px;
  text-align: center;
}
</style>
