<template>
  <div>
    <RoleSwitch v-if="role === 'ADMIN' || role === 'SUPPORT'" :role="role" @switchRole="switchRole" />
    <component :is="dashboardComponent" />
  </div>
</template>
<script setup>
import { ref, computed } from 'vue';
import RoleSwitch from './components/RoleSwitch.vue';
import AdminDashboard from './pages/AdminDashboard.vue';
import SupportDashboard from './pages/SupportDashboard.vue';
import CoachDashboard from './pages/CoachDashboard.vue';
import PlayerDashboard from './pages/PlayerDashboard.vue';

const role = ref('ADMIN');
const dashboardComponent = computed(() => {
  switch (role.value) {
    case 'ADMIN': return AdminDashboard;
    case 'SUPPORT': return SupportDashboard;
    case 'COACH': return CoachDashboard;
    case 'PLAYER': return PlayerDashboard;
    default: return PlayerDashboard;
  }
});
function switchRole(newRole) { role.value = newRole; }
</script>

