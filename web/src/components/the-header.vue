<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <router-link to="/">
        <a-menu-item key="/">
          首页
        </a-menu-item>
      </router-link>

      <router-link to="/admin/ebook">
        <a-menu-item key="/admin/ebook">
          电子书管理
        </a-menu-item>
      </router-link>
      <router-link to="/admin/category">
        <a-menu-item key="/admin/category">
          分类管理
        </a-menu-item>
      </router-link>



      <router-link to="/about">
        <a-menu-item key="/about">
          关于我们
        </a-menu-item>
      </router-link>
    </a-menu>
  </a-layout-header>
</template>

<script>
export default {
  name: "the-header"
}
</script>

<style scoped>

</style>