<template>
  <!--  布局-->
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div>
        <a-space size="small">
          <a-button type="primary" @click="handleQuery()" size="large">
            刷新
          </a-button>

          <a-button type="danger" @click="add()" size="large">
            新增
          </a-button>
        </a-space>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"

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
      title="文档表单"
      v-model:visible="modalVisible"
      v-model:confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{span:6}">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择"
            allow-clear
            tree-default-expand-all
            :tree-data="treeSelectData"
            :replaceFields="{ label:'name', key:'id', value: 'id' }"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
      <a-form-item>
        <div id="content"></div>
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
import {useRoute} from "vue-router";

import E from 'wangeditor'


export default defineComponent({
  name: 'AdminDoc',
  setup() {


    const route = useRoute();

    //封装搜索条件
    // const param = ref({
    //   name: ''
    // });
    const param = ref();
    param.value = {};
    //文档数据
    const docs = ref();

    //分页
    // const pagination = ref({
    //   current: 1,
    //   pageSize: 10,
    //   total: 0
    // });
    //加载文档
    const loading = ref(false);
    //文档列
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父文档',
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
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];


    const treeSelectData = ref();
    treeSelectData.value = [];

    /**
     * 将某节点和其子孙节点设置为disabled
     */

    const setDisabled = (treeSelectData: any, id: any) => {
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          console.log("disabled", node);
          node.disabled = true;

          //  遍历子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisabled(children, children[j].id);
            }
          }
        } else {
          let children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisabled(children, id);
          }
        }
      }
    }


    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true;

      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      level1.value = [];

      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("原始数组：", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构：", level1);
        } else {
          message.error(data.message);
        }
      });
    };


    //显示弹窗
    const modalVisible = ref(false);
    //弹窗提交loading效果
    const modalLoading = ref(false);
    //每一本书
    const doc = ref({});


    const editor = new E('#content');

    /**
     * edit按钮方法
     */
    const edit = (record: any) => {

      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value);

      setDisabled(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'});
      setTimeout(function () {
        editor.create()
      }, 100);
    }

    /**
     * 新增
     */
    const add = () => {

      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value);

      treeSelectData.value.unshift({id: 0, name: '无'});
      setTimeout(function () {
        editor.create()
      }, 100);

    }


    const ids: Array<String> = [];
    /**
     * 获取需要被删除的当前id，及子孙id
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          console.log("delete", node);
          ids.push(id);
          //  遍历子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          let children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    }

    /**
     * 删除
     */

    const remove = (id: number) => {

      getDeleteIds(level1.value, id);


      Modal.confirm({
        title: '删除后不可恢复，确认删除？',
        icon: createVNode(ExclamationCircleOutlined),
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk() {
          axios.delete("doc/delete/" + ids.join(",")).then((response) => {
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
            handleQuery();


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
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        modalLoading.value = false;

        //如果成功了
        if (data.success) {
          modalVisible.value = false;
          Modal.success({
            title: '添加或修改成功'
          });
          //重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }

      });
    }


    /**
     * 组件挂载完成后执行的函数
     */
    onMounted(() => {
      handleQuery();
      // editor.create();
    });


    /**
     * 返回组件所需要的变量和方法
     */
    return {
      // docs,
      level1,
      columns,
      loading,

      modalVisible,
      modalLoading,
      doc,
      edit,
      handleModalOk,

      add,
      remove,
      param,
      handleQuery,
      treeSelectData


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
