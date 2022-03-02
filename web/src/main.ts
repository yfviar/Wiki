import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
// import * as Icons from '@ant-design/icons-vue'
import axios from "axios";
import {Tool} from "@/util/tool";


/**
 * axios拦截器
 */

axios.interceptors.request.use(config => {
        console.log('请求参数：', config);

        const token = store.state.user.token;
        if (Tool.isNotEmpty(token)) {
            console.log("headers", config.headers);
            let headers = JSON.stringify(config.headers);
            headers = headers.substr(0, headers.length - 1) + ',"token":"' + token + '"}';
            config.headers = JSON.parse(headers)

            // console.log("headers", config.headers);
            console.log("请求headers增加token:", token);

        }
        return config;
    }
    ,
    error => {
        return Promise.reject(error);
    }
)
;

axios.interceptors.response.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

//配置请求域名baseURL
axios.defaults.baseURL = process.env.VUE_APP_SERVER

createApp(App).use(store).use(router).use(Antd).mount('#app');

console.log('环境：' + process.env.NODE_ENV);