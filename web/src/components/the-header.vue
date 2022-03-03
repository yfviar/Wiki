<template>
  <a-layout-header class="header">
    <div class="logo"></div>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >

      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" v-if="user.id">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook" v-if="user.id">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" v-if="user.id">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>

      <div class="login">
        <a class="login-menu" v-show="user.id" @click="logout()">
          <span>退出登录</span>
        </a>
        <a class="login-menu" v-show="user.id">
          <span>您好：{{ user.name }}</span>
        </a>
        <a class="login-menu" v-show="!user.id" @click="showLoginModal()">
          <span>登录</span>
        </a>
      </div>
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

<script lang="ts">
import {computed, createVNode, ref} from "vue";
import {message, Modal} from "ant-design-vue";
import axios from "axios";
import store from "@/store";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import router from "@/router";


declare let hexMd5: any;
declare let KEY: any;


export default {
  name: "the-header",
  setup() {
    const loginUser = ref()
    loginUser.value = {}

    const user = computed(() => store.state.user);


    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginUser.value = {};
      loginModalVisible.value = true;
    };

    const login = () => {
      console.log("开始登陆");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          store.commit("setUser", data.content);
          message.success("登录成功！");

        } else {
          message.error(data.message);
          loginUser.value.password = null;
        }
      });
    }


    const logout = () => {

      Modal.confirm({
        title: '删除后不可恢复，确认删除？',
        icon: createVNode(ExclamationCircleOutlined),
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk() {
          axios.get("/user/logout/" + +user.value.token).then((response) => {
            const data = response.data;
            if (data.success) {

              store.commit("setUser", {});
              Modal.success({
                title: '已退出登录'
              });
              router.push("/");
            } else {
              Modal.error({
                title: data.message
              });
            }
          });
        },
        onCancel() {
          console.log('取消');
        },
      });
    }

    return {
      loginModalVisible,
      loginModalLoading,
      loginUser,
      login,
      showLoginModal,
      user,
      logout
    }

  }
}
</script>

<style scoped>
.logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
  color: white;
  font-size: 18px;
}

.login {
  position: absolute;
  right: 50px;
  color: white;
  padding-left: 10px;
}

.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>