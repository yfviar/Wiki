<template>
  <!--  布局-->
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div>
        <a-space size="small">
          <a-input-search

              v-model:value="param.name"
              placeholder="请输入书籍名称"
              size="large"
              style="width: 300px"
              enter-button
              @search="handleQuery({page:1,size:pagination.pageSize})"
          />

          <a-button type="danger" @click="add()" size="large">
            新增
          </a-button>
        </a-space>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <!--        头像-->
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <!--        分类-->
        <template v-slot:category="{text,record}">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <!--        按钮-->
        <template v-slot:action="{ text,record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="primary">
                文档管理
              </a-button>
            </router-link>

            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-button type="danger" @click="remove(record.id)">
              删除
            </a-button>
          </a-space>
        </template>

      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
      title="电子书表单"
      v-model:visible="modalVisible"
      v-model:confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{span:6}">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, createVNode} from "vue";
import axios from "axios";
import {Modal, message} from 'ant-design-vue';
import {ExclamationCircleOutlined} from '@ant-design/icons-vue';
import {Tool} from "@/util/tool";



export default defineComponent({
  name: 'AdminEbook',
  setup() {

    //封装搜索条件
    // 方法一
    // const param = ref({
    //   name: ''
    // });
    // 方法二
    const param = ref();
    param.value = {};
    //电子书数据
    const ebooks = ref();

    //分页
    const pagination = ref({
      current: 1,
      pageSize: 10,
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
          name: param.value.name,
          page: p.page,
          size: p.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
          //重置分页按钮
          pagination.value.current = p.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };
    /**
     * 处理分页方法
     */
    const handleTableChange = (pagination: any) => {
      // console.log("自带的分页参数：" + pagination);
      handleQuery({
        name: param.value.name,
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    //显示弹窗
    const modalVisible = ref(false);
    //弹窗提交loading效果
    const modalLoading = ref(false);
    //每一本书
    const ebook = ref();

    //分类id数组，[100,101]对应：前端开发/Vue
    const categoryIds = ref();


    /**
     * edit按钮方法
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [String(ebook.value.category1Id), String(ebook.value.category2Id)];
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    }
    /**
     * 删除
     */

    const remove = (id: number) => {

      Modal.confirm({
        title: '删除后不可恢复，确认删除？',
        icon: createVNode(ExclamationCircleOutlined),
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk() {
          axios.delete("ebook/delete/" + id).then((response) => {
            const data = response.data;
            if (data.success) {
              Modal.success({
                title: '删除成功'
              });
            } else {
              Modal.error({
                title: '删除失败'
              });
            }

            //重新加载列表
            handleQuery({
              page: 1,
              size: pagination.value.pageSize
            });


          });
        },
        onCancel() {
          console.log('取消');
        },
      });

    }


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


    /**
     * 编辑弹窗确认提交
     */
    const handleModalOk = () => {

      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];


      axios.post("/ebook/save", ebook.value).then((response) => {
        const data = response.data;
        modalLoading.value = false;

        //如果成功了
        if (data.success) {
          modalVisible.value = false;
          Modal.success({
            title: '添加或修改成功'
          });
          //重新加载列表
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }

      });
    }

    let categorys: any;

    /**
     * 查询分类
     */
    const queryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          // console.log("树形结构：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    }

    const getCategoryName = (cid: any) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (Number(item.id) === cid) {
          result = item.name;
        }
      });

      return result;
    }


    /**
     * 组件挂载完成后执行的函数
     */
    onMounted(() => {
      //查询所有分类
      queryCategory();

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
      ebook,
      edit,
      handleModalOk,

      add,
      remove,
      param,
      handleQuery,
      categoryIds,
      level1,
      getCategoryName

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
