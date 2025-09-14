<template>
  <div class="admin-dashboard">
    <!-- Mobile Menu Toggle -->
    <div class="lg:hidden mb-4">
      <button 
        @click="sidebarOpen = !sidebarOpen"
        class="btn-primary"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
        </svg>
        Menu
      </button>
    </div>

    <div class="flex gap-6">
      <!-- Sidebar -->
      <div :class="sidebarClasses">
        <div class="p-6 border-b border-gray-200">
          <h2 class="text-xl font-bold text-green-700">Admin Dashboard</h2>
        </div>
        
        <nav class="p-4">
          <ul class="space-y-2">
            <li>
              <button 
                @click="activeTab = 'overview'"
                :class="getTabClasses('overview')"
              >
                <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                </svg>
                Overview
              </button>
            </li>
            <li>
              <button 
                @click="activeTab = 'users'"
                :class="getTabClasses('users')"
              >
                <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
                </svg>
                User Management
              </button>
            </li>
            <li>
              <button 
                @click="activeTab = 'fees'"
                :class="getTabClasses('fees')"
              >
                <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                </svg>
                Fee Management
              </button>
            </li>
            <li>
              <button 
                @click="handleLogout"
                class="w-full flex items-center px-4 py-2 text-left text-red-600 hover:bg-red-50 rounded-lg transition-colors"
              >
                <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                </svg>
                Logout
              </button>
            </li>
          </ul>
        </nav>
      </div>

      <!-- Main Content -->
      <div class="flex-1 min-w-0">
        <!-- Overview Tab -->
        <div v-if="activeTab === 'overview'" class="overview-section">
          <div class="mb-6">
            <h1 class="text-2xl font-bold text-gray-900">Dashboard Overview</h1>
            <p class="text-gray-600">Welcome to the admin dashboard</p>
          </div>

          <!-- Stats Cards -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <BaseCard v-for="stat in stats" :key="stat.title" variant="elevated">
              <div class="flex items-center">
                <div class="flex-shrink-0">
                  <div :class="`w-8 h-8 rounded-lg ${stat.bgColor} flex items-center justify-center`">
                    <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="stat.icon"></path>
                    </svg>
                  </div>
                </div>
                <div class="ml-4">
                  <dt class="text-sm font-medium text-gray-500 truncate">{{ stat.title }}</dt>
                  <dd class="text-lg font-semibold text-gray-900">{{ stat.value }}</dd>
                </div>
              </div>
            </BaseCard>
          </div>

          <!-- Recent Activity -->
          <BaseCard title="Recent Activity" variant="elevated">
            <div class="space-y-4">
              <div v-for="activity in recentActivity" :key="activity.id" class="flex items-center space-x-3">
                <div class="flex-shrink-0">
                  <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                    <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z"></path>
                    </svg>
                  </div>
                </div>
                <div class="flex-1 min-w-0">
                  <p class="text-sm text-gray-900">{{ activity.message }}</p>
                  <p class="text-xs text-gray-500">{{ activity.time }}</p>
                </div>
              </div>
            </div>
          </BaseCard>
        </div>

        <!-- User Management Tab -->
        <div v-else-if="activeTab === 'users'" class="users-section">
          <div class="mb-6">
            <h1 class="text-2xl font-bold text-gray-900">User Management</h1>
            <p class="text-gray-600">Manage admins, support staff, coaches, and players</p>
          </div>

          <BaseCard variant="elevated">
            <BaseTable
              :data="users"
              :columns="userColumns"
              :loading="loadingUsers"
              searchable
              search-placeholder="Search users..."
              @search="handleUserSearch"
            >
              <template #actions>
                <button @click="showCreateUserModal = true" class="btn-primary">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                  </svg>
                  Add User
                </button>
              </template>

              <template #cell-status="{ value }">
                <span :class="getStatusBadgeClass(value)">
                  {{ value }}
                </span>
              </template>

              <template #row-actions="{ item }">
                <div class="flex space-x-2">
                  <button 
                    @click="editUser(item)"
                    class="text-blue-600 hover:text-blue-800 text-sm"
                  >
                    Edit
                  </button>
                  <button 
                    v-if="item.status === 'PENDING'"
                    @click="approveUser(item)"
                    class="text-green-600 hover:text-green-800 text-sm"
                  >
                    Approve
                  </button>
                </div>
              </template>
            </BaseTable>
          </BaseCard>
        </div>

        <!-- Fee Management Tab -->
        <div v-else-if="activeTab === 'fees'" class="fees-section">
          <div class="mb-6">
            <h1 class="text-2xl font-bold text-gray-900">Fee Management</h1>
            <p class="text-gray-600">Manage fees and payment tracking</p>
          </div>

          <!-- Fee Summary Cards -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <BaseCard v-for="summary in feeSummary" :key="summary.title" variant="elevated">
              <div class="text-center">
                <dt class="text-sm font-medium text-gray-500">{{ summary.title }}</dt>
                <dd class="text-2xl font-bold text-green-600">₹{{ summary.amount.toLocaleString() }}</dd>
              </div>
            </BaseCard>
          </div>

          <BaseCard variant="elevated">
            <BaseTable
              :data="fees"
              :columns="feeColumns"
              :loading="loadingFees"
              searchable
              search-placeholder="Search fees..."
            >
              <template #actions>
                <button @click="showCreateFeeModal = true" class="btn-primary">
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                  </svg>
                  Add Fee Record
                </button>
              </template>

              <template #cell-status="{ value }">
                <span :class="getPaymentStatusClass(value)">
                  {{ value }}
                </span>
              </template>

              <template #row-actions="{ item }">
                <div class="flex space-x-2">
                  <button 
                    v-if="item.status === 'PENDING'"
                    @click="markAsPaid(item)"
                    class="text-green-600 hover:text-green-800 text-sm"
                  >
                    Mark Paid
                  </button>
                  <button 
                    @click="editFee(item)"
                    class="text-blue-600 hover:text-blue-800 text-sm"
                  >
                    Edit
                  </button>
                </div>
              </template>
            </BaseTable>
          </BaseCard>
        </div>
      </div>
    </div>

    <!-- User Modal -->
    <BaseModal :is-open="showCreateUserModal" @close="showCreateUserModal = false" title="Create User">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Name</label>
          <input v-model="newUser.name" type="text" class="input-field" placeholder="Enter user name" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input v-model="newUser.email" type="email" class="input-field" placeholder="Enter email" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Role</label>
          <select v-model="newUser.role" class="input-field">
            <option value="">Select Role</option>
            <option value="ADMIN">Admin</option>
            <option value="SUPPORT">Support</option>
            <option value="COACH">Coach</option>
            <option value="PLAYER">Player</option>
          </select>
        </div>
      </div>

      <template #footer>
        <div class="flex justify-end space-x-3">
          <button @click="showCreateUserModal = false" class="btn-secondary">Cancel</button>
          <button @click="createUser" class="btn-primary">Create User</button>
        </div>
      </template>
    </BaseModal>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/authStore.js';
import BaseCard from '../components/BaseCard.vue';
import BaseTable from '../components/BaseTable.vue';
import BaseModal from '../components/BaseModal.vue';

const router = useRouter();
const authStore = useAuthStore();

// Reactive state
const activeTab = ref('overview');
const sidebarOpen = ref(false);
const loadingUsers = ref(false);
const loadingFees = ref(false);
const showCreateUserModal = ref(false);
const showCreateFeeModal = ref(false);

// Form data
const newUser = ref({
  name: '',
  email: '',
  role: ''
});

// Mock data (to be replaced with API calls)
const stats = ref([
  {
    title: 'Total Users',
    value: '234',
    icon: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z',
    bgColor: 'bg-blue-500'
  },
  {
    title: 'Active Players',
    value: '189',
    icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
    bgColor: 'bg-green-500'
  },
  {
    title: 'Monthly Revenue',
    value: '₹4,56,000',
    icon: 'M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z',
    bgColor: 'bg-yellow-500'
  },
  {
    title: 'Pending Approvals',
    value: '12',
    icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-red-500'
  }
]);

const recentActivity = ref([
  { id: 1, message: 'New player registration: John Doe', time: '2 minutes ago' },
  { id: 2, message: 'Fee payment received from Player #123', time: '15 minutes ago' },
  { id: 3, message: 'Coach Sarah approved for batch assignment', time: '1 hour ago' },
  { id: 4, message: 'Monthly fee reminder sent to all players', time: '2 hours ago' }
]);

const users = ref([
  { id: 1, name: 'John Admin', email: 'john@admin.com', role: 'ADMIN', status: 'ACTIVE' },
  { id: 2, name: 'Jane Support', email: 'jane@support.com', role: 'SUPPORT', status: 'ACTIVE' },
  { id: 3, name: 'Coach Smith', email: 'smith@coach.com', role: 'COACH', status: 'PENDING' },
  { id: 4, name: 'Player Doe', email: 'doe@player.com', role: 'PLAYER', status: 'ACTIVE' }
]);

const fees = ref([
  { id: 1, playerName: 'John Player', amount: 3000, dueDate: '2025-01-15', status: 'PAID' },
  { id: 2, playerName: 'Jane Player', amount: 2000, dueDate: '2025-01-20', status: 'PENDING' },
  { id: 3, playerName: 'Bob Player', amount: 4000, dueDate: '2025-01-10', status: 'OVERDUE' }
]);

const feeSummary = ref([
  { title: 'Total Revenue', amount: 456000 },
  { title: 'Pending Payments', amount: 89000 },
  { title: 'This Month', amount: 234000 }
]);

// Table columns
const userColumns = [
  { key: 'name', label: 'Name', sortable: true },
  { key: 'email', label: 'Email', sortable: true },
  { key: 'role', label: 'Role', sortable: true },
  { key: 'status', label: 'Status', sortable: true }
];

const feeColumns = [
  { key: 'playerName', label: 'Player', sortable: true },
  { key: 'amount', label: 'Amount', sortable: true, formatter: (value) => `₹${value}` },
  { key: 'dueDate', label: 'Due Date', sortable: true },
  { key: 'status', label: 'Status', sortable: true }
];

// Computed
const sidebarClasses = computed(() => {
  const baseClasses = 'bg-white rounded-lg shadow-lg';
  const responsiveClasses = sidebarOpen.value 
    ? 'fixed inset-y-0 left-0 z-50 w-64 lg:static lg:inset-0'
    : 'hidden lg:block w-64';
  return `${baseClasses} ${responsiveClasses}`;
});

// Methods
const getTabClasses = (tab) => {
  const baseClasses = 'w-full flex items-center px-4 py-2 text-left rounded-lg transition-colors';
  const activeClasses = activeTab.value === tab 
    ? 'bg-green-100 text-green-700 font-medium'
    : 'text-gray-600 hover:bg-gray-100';
  return `${baseClasses} ${activeClasses}`;
};

const getStatusBadgeClass = (status) => {
  const baseClasses = 'px-2 py-1 text-xs font-medium rounded-full';
  switch (status) {
    case 'ACTIVE':
      return `${baseClasses} bg-green-100 text-green-800`;
    case 'PENDING':
      return `${baseClasses} bg-yellow-100 text-yellow-800`;
    case 'INACTIVE':
      return `${baseClasses} bg-red-100 text-red-800`;
    default:
      return `${baseClasses} bg-gray-100 text-gray-800`;
  }
};

const getPaymentStatusClass = (status) => {
  const baseClasses = 'px-2 py-1 text-xs font-medium rounded-full';
  switch (status) {
    case 'PAID':
      return `${baseClasses} bg-green-100 text-green-800`;
    case 'PENDING':
      return `${baseClasses} bg-yellow-100 text-yellow-800`;
    case 'OVERDUE':
      return `${baseClasses} bg-red-100 text-red-800`;
    default:
      return `${baseClasses} bg-gray-100 text-gray-800`;
  }
};

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};

const handleUserSearch = (query) => {
  console.log('Searching users:', query);
  // Implement user search logic
};

const editUser = (user) => {
  console.log('Editing user:', user);
  // Implement edit user logic
};

const approveUser = (user) => {
  console.log('Approving user:', user);
  // Implement approve user logic
  user.status = 'ACTIVE';
};

const createUser = () => {
  console.log('Creating user:', newUser.value);
  // Implement create user logic
  showCreateUserModal.value = false;
  newUser.value = { name: '', email: '', role: '' };
};

const editFee = (fee) => {
  console.log('Editing fee:', fee);
  // Implement edit fee logic
};

const markAsPaid = (fee) => {
  console.log('Marking fee as paid:', fee);
  // Implement mark as paid logic
  fee.status = 'PAID';
};

onMounted(() => {
  // Load initial data
  console.log('Admin dashboard mounted');
});
</script>

<style scoped>
/* Mobile sidebar overlay */
@media (max-width: 1023px) {
  .sidebar-overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 40;
  }
}
</style>

