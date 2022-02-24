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
      <router-link to="/admin/user">
        <a-menu-item key="/admin/user">
          用户管理
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

      <a class="login-menu" @click="showLoginModal()">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login()"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>

<script>
import {ref} from "vue";

export default {
  name: "the-header",
  setup() {
    const loginUser = ref({
      loginName: "test",
      password: "test"
    })

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    const login = () => {
      console.log("开始登陆");
    }

    return {
      loginModalVisible,
      loginModalLoading,
      loginUser,
      login,
      showLoginModal,
    }

  }
}
</script>

<style scoped>
.login-menu {
  float: right;
  color: white;
}
</style>