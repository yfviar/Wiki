<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <template #icon>
            <mail-outlined/>
          </template>
          <span>欢迎</span>
        </a-menu-item>

        <a-sub-menu :key="level.id" v-for=" level in level1">
          <template v-slot:title>
            <user-outlined/>
            <span>
                {{ level.name }}
              </span>
          </template>

          <a-menu-item :key="item.id" v-for="item in level.children">
            <mail-outlined/>
            <span>
              {{ item.name }}
            </span>
          </a-menu-item>

        </a-sub-menu>

      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用一木之禾知识库</h1>
      </div>

      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"
              :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span>
                <component :is="'FileTextOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
              <span>
                <component :is="'EyeOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
              <span>
                <component :is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>


    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import {EyeOutlined, LikeOutlined, MailOutlined, UserOutlined, FileTextOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'Home',
  components: {

    EyeOutlined,
    LikeOutlined,
    MailOutlined,
    UserOutlined,
    FileTextOutlined
  },
  setup() {

    //响应式数据:第一种ref()
    const ebooks = ref();
    //响应式数据：第二种
    const ebooks1 = reactive({books: []});

    /**
     * 一级分类树，children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级分类树，children属性就是二级分类
    level1.value = [];

    //加载分类
    const loading = ref(false);

    const handleQueryEbook = () => {
      //查询电子书
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2.value
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    }

    //分类数据
    const categorys = ref();

    const categoryId2 = ref();

    const isShowWelcome = ref(true);

    const handleClick = (value: any) => {
      // console.log("click", value)
      isShowWelcome.value = value.key == "welcome";
      categoryId2.value = value.key;

      if (categoryId2.value != 'welcome') {
        handleQueryEbook();
      }

    }


    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;

      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      level1.value = [];

      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;
          // console.log("原始数组：", categorys.value);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);
          // console.log("树形结构：", level1);


        } else {
          message.error(data.message);
        }
      });
    };


    onMounted(() => {
      handleQuery();
    });
    return {
      // actions,
      ebooks,
      level1,
      isShowWelcome,
      handleClick,
      ebooks2: toRef(ebooks1, "books")
    };
  },
});
</script>
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
