<template>
  <div style="margin-bottom: 1rem;">
    <label>Switch Role: </label>
    <select :value="role" @change="onSwitch($event)">
      <option value="ADMIN">Admin</option>
      <option value="SUPPORT">Support</option>
      <option value="COACH">Coach</option>
    </select>
  </div>
</template>
<script setup>
const props = defineProps(['role']);
const emit = defineEmits(['switchRole']);
function onSwitch(e) {
  emit('switchRole', e.target.value);
}
</script>

