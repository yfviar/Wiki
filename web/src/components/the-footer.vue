<template>
  <a-layout-footer style="text-align: center">
    一木之禾————你好哇，{{ user.name }}
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

</style>