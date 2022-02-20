<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-menu-item key="1">
          <template #icon>
            <mail-outlined/>
            <!--            <user-outlined/>-->
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
      <a-list item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
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
import {StarOutlined, LikeOutlined, MessageOutlined, MailOutlined, UserOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'Home',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    MailOutlined,
    UserOutlined
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

    //分类数据
    const categorys = ref();


    onMounted(() => {
      handleQuery();
    });
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
          console.log("原始数组：", categorys.value);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);
          console.log("树形结构：", level1);

          //查询电子书
          axios.get("/ebook/all", {}).then((response) => {
            const data = response.data;
            ebooks.value = data.content.list;
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const actions: Record<string, string>[] = [
      {type: 'StarOutlined', text: '156'},
      {type: 'LikeOutlined', text: '156'},
      {type: 'MessageOutlined', text: '2'},
    ];
    return {
      actions,
      ebooks,
      level1,
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
