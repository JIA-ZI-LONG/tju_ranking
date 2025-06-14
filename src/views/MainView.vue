<template>
  <div>
    <MainMenu />
    <div class="main-content">
      <router-view/>
    </div>
  </div>
</template>

<script>
import MainMenu from '../components/MainMenu.vue'

export default {
  name: 'MainView',
  components: {
    MainMenu
  }
}
</script>

<style scoped>
.main-content {
  padding: 20px;
}
</style>
