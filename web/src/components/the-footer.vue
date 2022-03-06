<template>
  <a-layout-footer class="footer">
    一木之禾<span v-show="user.name">————你好哇，</span>{{ user.name }}
  </a-layout-footer>
</template>

<script lang="ts">
import store from "@/store";
import {computed, defineComponent} from "vue";

export default defineComponent({
  name: "the-footer",
  setup() {
    const user = computed(() => store.state.user);

    return {
      user,
    }
  }
});
</script>

<style scoped>
.footer {
  text-align: center;
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>