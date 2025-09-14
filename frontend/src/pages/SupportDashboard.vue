<template>
  <div class="support-dashboard max-w-7xl mx-auto">
    <div class="mb-8">
      <div class="flex justify-between items-center">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Support Dashboard</h1>
          <p class="text-gray-600">Manage player registrations, fees, and attendance</p>
        </div>
        <button @click="handleLogout" class="btn-secondary">
          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
          </svg>
          Logout
        </button>
      </div>
    </div>

    <!-- Quick Stats -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <BaseCard v-for="stat in supportStats" :key="stat.title" variant="elevated">
        <div class="text-center">
          <div :class="`w-12 h-12 mx-auto mb-3 rounded-lg ${stat.bgColor} flex items-center justify-center`">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="stat.icon"></path>
            </svg>
          </div>
          <dt class="text-sm font-medium text-gray-500">{{ stat.title }}</dt>
          <dd class="text-2xl font-bold text-gray-900">{{ stat.value }}</dd>
        </div>
      </BaseCard>
    </div>

    <!-- Main Content Tabs -->
    <div class="bg-white rounded-lg shadow-lg overflow-hidden">
      <div class="border-b border-gray-200">
        <nav class="flex space-x-8 px-6" aria-label="Tabs">
          <button
            v-for="tab in supportTabs"
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="getTabClasses(tab.id)"
          >
            {{ tab.name }}
          </button>
        </nav>
      </div>

      <div class="p-6">
        <!-- Player Registration Tab -->
        <div v-if="activeTab === 'registrations'" class="space-y-6">
          <div class="flex justify-between items-center">
            <h2 class="text-xl font-semibold text-gray-900">Pending Registrations</h2>
            <span class="bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded-full">
              {{ pendingRegistrations.length }} pending
            </span>
          </div>

          <BaseTable
            :data="pendingRegistrations"
            :columns="registrationColumns"
            :loading="false"
            searchable
            search-placeholder="Search registrations..."
          >
            <template #cell-status="{ value }">
              <span :class="getStatusBadgeClass(value)">{{ value }}</span>
            </template>

            <template #row-actions="{ item }">
              <div class="flex space-x-2">
                <button @click="approveRegistration(item)" class="text-green-600 hover:text-green-800 text-sm">
                  Approve
                </button>
                <button @click="rejectRegistration(item)" class="text-red-600 hover:text-red-800 text-sm">
                  Reject
                </button>
              </div>
            </template>
          </BaseTable>
        </div>

        <!-- Fee Collection Tab -->
        <div v-if="activeTab === 'fees'" class="space-y-6">
          <div class="flex justify-between items-center">
            <h2 class="text-xl font-semibold text-gray-900">Fee Collection</h2>
            <button class="btn-primary">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
              </svg>
              Record Payment
            </button>
          </div>

          <BaseTable
            :data="feeRecords"
            :columns="feeColumns"
            :loading="false"
            searchable
            search-placeholder="Search fee records..."
          >
            <template #cell-status="{ value }">
              <span :class="getPaymentStatusClass(value)">{{ value }}</span>
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
                <button @click="sendReminder(item)" class="text-blue-600 hover:text-blue-800 text-sm">
                  Send Reminder
                </button>
              </div>
            </template>
          </BaseTable>
        </div>

        <!-- Attendance Tab -->
        <div v-if="activeTab === 'attendance'" class="space-y-6">
          <div class="flex justify-between items-center">
            <h2 class="text-xl font-semibold text-gray-900">Today's Attendance</h2>
            <div class="text-sm text-gray-500">
              {{ new Date().toLocaleDateString() }}
            </div>
          </div>

          <BaseTable
            :data="attendanceRecords"
            :columns="attendanceColumns"
            :loading="false"
            searchable
            search-placeholder="Search players..."
          >
            <template #cell-present="{ item, value }">
              <div class="flex items-center">
                <input
                  :id="`attendance-${item.id}`"
                  v-model="item.present"
                  type="checkbox"
                  @change="updateAttendance(item)"
                  class="w-4 h-4 text-green-600 bg-gray-100 border-gray-300 rounded focus:ring-green-500 focus:ring-2"
                />
                <label :for="`attendance-${item.id}`" class="ml-2 text-sm text-gray-900">
                  {{ value ? 'Present' : 'Absent' }}
                </label>
              </div>
            </template>
          </BaseTable>

          <div class="flex justify-end">
            <button @click="saveAttendance" class="btn-primary">
              Save Attendance
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/authStore.js';
import BaseCard from '../components/BaseCard.vue';
import BaseTable from '../components/BaseTable.vue';

const router = useRouter();
const authStore = useAuthStore();

const activeTab = ref('registrations');

const supportStats = ref([
  {
    title: 'Pending Registrations',
    value: '8',
    icon: 'M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z',
    bgColor: 'bg-orange-500'
  },
  {
    title: 'Due Payments',
    value: '₹24,500',
    icon: 'M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z',
    bgColor: 'bg-red-500'
  },
  {
    title: 'Today Present',
    value: '142/189',
    icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-green-500'
  },
  {
    title: 'Active Batches',
    value: '12',
    icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
    bgColor: 'bg-blue-500'
  }
]);

const supportTabs = [
  { id: 'registrations', name: 'Registrations' },
  { id: 'fees', name: 'Fee Collection' },
  { id: 'attendance', name: 'Attendance' }
];

// Mock data
const pendingRegistrations = ref([
  { id: 1, name: 'Alex Johnson', email: 'alex@email.com', role: 'PLAYER', phone: '+91-9876543210', registeredAt: '2025-01-10', status: 'PENDING' },
  { id: 2, name: 'Maria Garcia', email: 'maria@email.com', role: 'COACH', phone: '+91-8765432109', registeredAt: '2025-01-09', status: 'PENDING' }
]);

const feeRecords = ref([
  { id: 1, playerName: 'John Smith', amount: 3000, dueDate: '2025-01-15', status: 'PENDING', month: 'January 2025' },
  { id: 2, playerName: 'Sarah Connor', amount: 2500, dueDate: '2025-01-10', status: 'OVERDUE', month: 'January 2025' },
  { id: 3, playerName: 'Mike Johnson', amount: 3000, dueDate: '2025-01-20', status: 'PAID', month: 'January 2025' }
]);

const attendanceRecords = ref([
  { id: 1, playerName: 'John Smith', batch: 'Morning Batch A', present: true },
  { id: 2, playerName: 'Sarah Connor', batch: 'Evening Batch B', present: false },
  { id: 3, playerName: 'Mike Johnson', batch: 'Morning Batch A', present: true }
]);

// Table columns
const registrationColumns = [
  { key: 'name', label: 'Name', sortable: true },
  { key: 'email', label: 'Email', sortable: true },
  { key: 'role', label: 'Role', sortable: true },
  { key: 'phone', label: 'Phone', sortable: true },
  { key: 'registeredAt', label: 'Registered', sortable: true },
  { key: 'status', label: 'Status', sortable: true }
];

const feeColumns = [
  { key: 'playerName', label: 'Player', sortable: true },
  { key: 'amount', label: 'Amount', sortable: true, formatter: (value) => `₹${value}` },
  { key: 'month', label: 'Month', sortable: true },
  { key: 'dueDate', label: 'Due Date', sortable: true },
  { key: 'status', label: 'Status', sortable: true }
];

const attendanceColumns = [
  { key: 'playerName', label: 'Player Name', sortable: true },
  { key: 'batch', label: 'Batch', sortable: true },
  { key: 'present', label: 'Attendance', sortable: false }
];

// Methods
const getTabClasses = (tabId) => {
  const baseClasses = 'py-4 px-1 border-b-2 font-medium text-sm';
  const activeClasses = activeTab.value === tabId 
    ? 'border-green-500 text-green-600'
    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300';
  return `${baseClasses} ${activeClasses}`;
};

const getStatusBadgeClass = (status) => {
  const baseClasses = 'px-2 py-1 text-xs font-medium rounded-full';
  switch (status) {
    case 'PENDING':
      return `${baseClasses} bg-yellow-100 text-yellow-800`;
    case 'APPROVED':
      return `${baseClasses} bg-green-100 text-green-800`;
    case 'REJECTED':
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

const approveRegistration = (registration) => {
  console.log('Approving registration:', registration);
  registration.status = 'APPROVED';
};

const rejectRegistration = (registration) => {
  console.log('Rejecting registration:', registration);
  registration.status = 'REJECTED';
};

const markAsPaid = (feeRecord) => {
  console.log('Marking as paid:', feeRecord);
  feeRecord.status = 'PAID';
};

const sendReminder = (feeRecord) => {
  console.log('Sending reminder:', feeRecord);
  // Implement reminder logic
};

const updateAttendance = (record) => {
  console.log('Updating attendance:', record);
  // Attendance is automatically updated via v-model
};

const saveAttendance = () => {
  console.log('Saving attendance for all records');
  // Implement save attendance logic
  alert('Attendance saved successfully!');
};

onMounted(() => {
  console.log('Support dashboard mounted');
});
</script>

