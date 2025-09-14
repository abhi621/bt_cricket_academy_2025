<template>
  <div class="dashboard-page">
    <header class="dashboard-header">
      <h1>Cricket Academy Management</h1>
      <div class="user-info">
        <span>Welcome, {{ currentUser?.username }} ({{ currentUser?.role }})</span>
        <button @click="logout" class="logout-btn">Logout</button>
      </div>
    </header>
    
    <nav class="dashboard-nav">
      <router-link to="/dashboard" class="nav-link" :class="{ active: $route.path === '/dashboard' }">
        Dashboard
      </router-link>
      <router-link 
        v-if="currentUser?.role === 'ADMIN' || currentUser?.role === 'SUPPORT'" 
        to="/approval-dashboard" 
        class="nav-link" 
        :class="{ active: $route.path === '/approval-dashboard' }"
      >
        User Approvals
      </router-link>
    </nav>
    
    <main class="dashboard-content">
      <component :is="dashboardComponent" />
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../services/authService.js';
import AdminDashboard from './AdminDashboard.vue';
import SupportDashboard from './SupportDashboard.vue';
import CoachDashboard from './CoachDashboard.vue';
import PlayerDashboard from './PlayerDashboard.vue';

const router = useRouter();
const currentUser = ref(null);

const dashboardComponent = computed(() => {
  if (!currentUser.value) return null;
  
  switch (currentUser.value.role) {
    case 'ADMIN': return AdminDashboard;
    case 'SUPPORT': return SupportDashboard;
    case 'COACH': return CoachDashboard;
    case 'PLAYER': return PlayerDashboard;
    default: return PlayerDashboard;
  }
});

const logout = () => {
  authService.logout();
  router.push('/login');
};

onMounted(() => {
  currentUser.value = authService.getCurrentUser();
  if (!currentUser.value) {
    router.push('/login');
  }
});
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.dashboard-header {
  background: white;
  padding: 1rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dashboard-header h1 {
  color: #333;
  margin: 0;
  font-size: 1.5rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info span {
  color: #666;
}

.logout-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.875rem;
}

.logout-btn:hover {
  background-color: #c82333;
}

.dashboard-nav {
  background: white;
  padding: 0 2rem;
  border-bottom: 1px solid #eee;
  display: flex;
  gap: 1rem;
}

.nav-link {
  padding: 1rem 0;
  text-decoration: none;
  color: #666;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.nav-link:hover,
.nav-link.active {
  color: #007bff;
  border-bottom-color: #007bff;
}

.dashboard-content {
  padding: 2rem;
}
</style>