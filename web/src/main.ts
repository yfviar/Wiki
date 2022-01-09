import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'


import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
// import * as Icons from '@ant-design/icons-vue'
import axios from "axios";

//配置请求域名baseURL
axios.defaults.baseURL = process.env.VUE_APP_SERVER

createApp(App).use(store).use(router).use(Antd).mount('#app');

console.log('环境：' + process.env.NODE_ENV);