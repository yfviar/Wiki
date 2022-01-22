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
              placeholder="请输入分类名称"
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
          :data-source="categorys"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <!--        按钮-->
        <template v-slot:action="{ text,record }">
          <a-space size="small">
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
      title="分类表单"
      v-model:visible="modalVisible"
      v-model:confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{span:6}">
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
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
  name: 'AdminCategory',
  setup() {

    //封装搜索条件
    // const param = ref({
    //   name: ''
    // });
    const param = ref();
    param.value = {};
    //分类数据
    const categorys = ref();

    //分页
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    //加载分类
    const loading = ref(false);
    //分类列
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
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
      axios.get("/category/list", {
        params: {
          name: param.value.name,
          page: p.page,
          size: p.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content.list;
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
      console.log("自带的分页参数：" + pagination);
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
    const category = ref({});


    /**
     * edit按钮方法
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
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
          axios.delete("category/delete/" + id).then((response) => {
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
     * 编辑弹窗确认提交
     */
    const handleModalOk = () => {

      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
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
      categorys,
      pagination,
      columns,
      loading,
      handleTableChange,

      modalVisible,
      modalLoading,
      category,
      edit,
      handleModalOk,

      add,
      remove,
      param,
      handleQuery

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
