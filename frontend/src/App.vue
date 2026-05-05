<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.page-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.main-content {
  margin-top: 60px;
  min-height: calc(100vh - 60px);
}

.footer {
  background: #f5f5f5;
  padding: 20px 0;
  text-align: center;
  margin-top: 40px;
  color: #666;
}
</style>