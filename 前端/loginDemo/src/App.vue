<script setup>
</script>

<template>
  <router-view>
 </router-view>
</template>

<style scoped>
html, body {
  height: 100%;   /* 占满浏览器视口高度 */
  margin: 0;      /* 清除默认边距 */
  overflow: hidden; /* 禁止全局滚动，交给内部容器处理 */
}
</style>
