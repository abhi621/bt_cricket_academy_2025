<template>
  <div class="approval-dashboard">
    <h2>User Approval Dashboard</h2>
    <p>Manage pending user registrations</p>
    
    <div v-if="isLoading" class="loading">
      Loading pending approvals...
    </div>
    
    <div v-else-if="pendingUsers.length === 0" class="no-pending">
      <p>No pending approvals at this time.</p>
    </div>
    
    <div v-else class="users-table">
      <table>
        <thead>
          <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Registration Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in pendingUsers" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span class="role-badge" :class="user.role.toLowerCase()">
                {{ user.role }}
              </span>
            </td>
            <td>{{ formatDate(user.id) }}</td>
            <td>
              <button 
                @click="approveUser(user)"
                :disabled="approvingUsers.has(user.id)"
                class="approve-btn"
              >
                {{ approvingUsers.has(user.id) ? 'Approving...' : 'Approve' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>
    
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import authService from '../services/authService.js';

const pendingUsers = ref([]);
const isLoading = ref(false);
const approvingUsers = ref(new Set());
const successMessage = ref('');
const errorMessage = ref('');

const loadPendingApprovals = async () => {
  isLoading.value = true;
  errorMessage.value = '';
  
  try {
    const users = await authService.getPendingApprovals();
    pendingUsers.value = users;
  } catch (error) {
    errorMessage.value = error.message || 'Failed to load pending approvals';
  } finally {
    isLoading.value = false;
  }
};

const approveUser = async (user) => {
  approvingUsers.value.add(user.id);
  errorMessage.value = '';
  successMessage.value = '';
  
  try {
    await authService.approveUser(user.id);
    successMessage.value = `User ${user.username} has been approved successfully!`;
    
    // Remove the approved user from the pending list
    pendingUsers.value = pendingUsers.value.filter(u => u.id !== user.id);
    
    // Clear success message after 3 seconds
    setTimeout(() => {
      successMessage.value = '';
    }, 3000);
  } catch (error) {
    errorMessage.value = error.message || 'Failed to approve user';
  } finally {
    approvingUsers.value.delete(user.id);
  }
};

const formatDate = (id) => {
  // Simple date formatting based on ID (in real app, you'd use actual registration date)
  return new Date().toLocaleDateString();
};

onMounted(() => {
  loadPendingApprovals();
});
</script>

<style scoped>
.approval-dashboard {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.approval-dashboard h2 {
  color: #333;
  margin-bottom: 0.5rem;
}

.approval-dashboard p {
  color: #666;
  margin-bottom: 2rem;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.no-pending {
  text-align: center;
  padding: 2rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  color: #666;
}

.users-table {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.role-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.875rem;
  font-weight: 500;
  text-transform: uppercase;
}

.role-badge.coach {
  background-color: #e3f2fd;
  color: #1976d2;
}

.role-badge.player {
  background-color: #f3e5f5;
  color: #7b1fa2;
}

.approve-btn {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.875rem;
}

.approve-btn:hover:not(:disabled) {
  background-color: #218838;
}

.approve-btn:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}

.success-message {
  margin-top: 1rem;
  padding: 0.75rem;
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
  border-radius: 4px;
}

.error-message {
  margin-top: 1rem;
  padding: 0.75rem;
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
}
</style>