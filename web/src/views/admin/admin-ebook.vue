<template>
  <!--  布局-->
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="about">
        <h1>电子书管理</h1>
      </div>
      <a-table
          :columns="columns"
          :row-key="record=> record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <!--        头像-->
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <!--        按钮-->
        <template v-slot:action="{text,record}">
          <a-space size="small">
            <a-button type="primary" @click="edit">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
          </a-space>
        </template>

      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
      title="电子书表单"
      :visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <p>test</p>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    //电子书数据
    const ebooks = ref();

    //分页
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    //加载电子书
    const loading = ref(false);
    //电子书列
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: {
          customRender: 'cover'
        }
      }, {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: {customRender: 'category'}
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     */
    const handleQuery = (p: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: p.page,
          size: p.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;
        //重置分页按钮
        pagination.value.current = p.page;
        pagination.value.total = data.content.total;
      });
    };
    /**
     * 处理分页方法
     */
    const handleTableChange = (pagination: any) => {
      console.log("自带的分页参数：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    //显示弹窗
    const modalVisible = ref(false);
    //弹窗提交loading效果
    const modalLoading = ref(false);
    /**
     * edit按钮方法
     */
    const edit = () => {
      console.log("edit");
      modalVisible.value = true;
    }
    /**
     * 编辑弹窗确认提交
     */
    const handleModalOk = () => {
      console.log("ok");
      modalLoading.value = true;

      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    }

    /**
     * 组件挂载完成后执行的函数
     */
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });


    /**
     * 返回组件所需要的变量和方法
     */
    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      modalVisible,
      modalLoading,
      edit,
      handleModalOk
    };

  }
});

</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
