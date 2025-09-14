<template>
  <div class="player-dashboard max-w-7xl mx-auto">
    <div class="mb-8">
      <div class="flex justify-between items-center">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Player Dashboard</h1>
          <p class="text-gray-600">Track your progress, schedule, and payments</p>
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
      <BaseCard v-for="stat in playerStats" :key="stat.title" variant="elevated">
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

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <!-- My Schedule -->
      <BaseCard title="My Training Schedule" variant="elevated">
        <div class="space-y-4">
          <div v-for="session in mySchedule" :key="session.id" class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <div>
              <h4 class="font-semibold text-gray-900">{{ session.batch }}</h4>
              <p class="text-sm text-gray-600">{{ session.day }} • {{ session.time }}</p>
              <p class="text-xs text-gray-500">Coach: {{ session.coach }}</p>
            </div>
            <div class="text-right">
              <span :class="getSessionStatusClass(session.status)">
                {{ session.status }}
              </span>
            </div>
          </div>
        </div>
      </BaseCard>

      <!-- Fee Status -->
      <BaseCard title="Fee Status" variant="elevated">
        <div class="space-y-4">
          <div v-for="fee in feeStatus" :key="fee.id" class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
            <div>
              <h4 class="font-medium text-gray-900">{{ fee.month }}</h4>
              <p class="text-sm text-gray-600">Amount: ₹{{ fee.amount }}</p>
              <p class="text-xs text-gray-500">Due: {{ fee.dueDate }}</p>
            </div>
            <div class="text-right">
              <span :class="getPaymentStatusClass(fee.status)" class="block mb-2">
                {{ fee.status }}
              </span>
              <button 
                v-if="fee.status === 'PENDING'"
                @click="payFee(fee)"
                class="text-green-600 hover:text-green-800 text-sm"
              >
                Pay Now
              </button>
            </div>
          </div>
        </div>
      </BaseCard>
    </div>

    <!-- Attendance History -->
    <div class="mt-8">
      <BaseCard title="Attendance History" variant="elevated">
        <div class="overflow-x-auto">
          <table class="w-full table-auto">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Batch</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Coach</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="record in attendanceHistory" :key="record.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ record.date }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ record.batch }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getAttendanceStatusClass(record.status)">
                    {{ record.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ record.coach }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </BaseCard>
    </div>

    <!-- Coach Feedback -->
    <div class="mt-8">
      <BaseCard title="Coach Feedback" variant="elevated">
        <div class="space-y-4">
          <div v-for="feedback in coachFeedback" :key="feedback.id" class="p-4 border border-gray-200 rounded-lg">
            <div class="flex justify-between items-start mb-2">
              <div>
                <span class="font-medium text-gray-900">Coach {{ feedback.coachName }}</span>
                <span class="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full ml-2">
                  {{ feedback.category }}
                </span>
              </div>
              <span class="text-xs text-gray-500">{{ feedback.date }}</span>
            </div>
            <p class="text-sm text-gray-700">{{ feedback.comment }}</p>
          </div>
        </div>
      </BaseCard>
    </div>

    <!-- Payment Modal -->
    <BaseModal :is-open="showPaymentModal" @close="showPaymentModal = false" title="Fee Payment">
      <div v-if="selectedFee" class="space-y-4">
        <div class="bg-gray-50 p-4 rounded-lg">
          <h3 class="font-semibold text-gray-900">Payment Details</h3>
          <div class="mt-2 space-y-1">
            <p class="text-sm text-gray-600">Month: {{ selectedFee.month }}</p>
            <p class="text-sm text-gray-600">Amount: ₹{{ selectedFee.amount }}</p>
            <p class="text-sm text-gray-600">Due Date: {{ selectedFee.dueDate }}</p>
          </div>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Payment Method</label>
          <select v-model="paymentForm.method" class="input-field">
            <option value="">Select payment method</option>
            <option value="ONLINE">Online Payment</option>
            <option value="CASH">Cash Payment</option>
            <option value="CHEQUE">Cheque</option>
          </select>
        </div>

        <div v-if="paymentForm.method === 'ONLINE'">
          <p class="text-sm text-gray-600 p-3 bg-blue-50 rounded-lg">
            You will be redirected to the payment gateway to complete your transaction.
          </p>
        </div>

        <div v-if="paymentForm.method === 'CHEQUE'">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Cheque Number</label>
            <input v-model="paymentForm.chequeNumber" type="text" class="input-field" placeholder="Enter cheque number" />
          </div>
        </div>
      </div>

      <template #footer>
        <div class="flex justify-end space-x-3">
          <button @click="showPaymentModal = false" class="btn-secondary">Cancel</button>
          <button @click="processPayment" class="btn-primary" :disabled="!paymentForm.method">
            {{ paymentForm.method === 'ONLINE' ? 'Proceed to Payment' : 'Submit Payment' }}
          </button>
        </div>
      </template>
    </BaseModal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/authStore.js';
import BaseCard from '../components/BaseCard.vue';
import BaseModal from '../components/BaseModal.vue';

const router = useRouter();
const authStore = useAuthStore();

const showPaymentModal = ref(false);
const selectedFee = ref(null);

// Mock data
const playerStats = ref([
  {
    title: 'Attendance Rate',
    value: '92%',
    icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-green-500'
  },
  {
    title: 'Sessions This Month',
    value: '16',
    icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-blue-500'
  },
  {
    title: 'Fee Status',
    value: 'Paid',
    icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-purple-500'
  },
  {
    title: 'Performance',
    value: 'Good',
    icon: 'M13 7h8m0 0v8m0-8l-8 8-4-4-6 6',
    bgColor: 'bg-yellow-500'
  }
]);

const mySchedule = ref([
  { id: 1, batch: 'Morning Batch A', day: 'Monday', time: '6:00 AM - 8:00 AM', coach: 'Coach Smith', status: 'UPCOMING' },
  { id: 2, batch: 'Morning Batch A', day: 'Wednesday', time: '6:00 AM - 8:00 AM', coach: 'Coach Smith', status: 'UPCOMING' },
  { id: 3, batch: 'Morning Batch A', day: 'Friday', time: '6:00 AM - 8:00 AM', coach: 'Coach Smith', status: 'UPCOMING' }
]);

const feeStatus = ref([
  { id: 1, month: 'January 2025', amount: 3000, dueDate: '2025-01-15', status: 'PENDING' },
  { id: 2, month: 'December 2024', amount: 3000, dueDate: '2024-12-15', status: 'PAID' },
  { id: 3, month: 'November 2024', amount: 3000, dueDate: '2024-11-15', status: 'PAID' }
]);

const attendanceHistory = ref([
  { id: 1, date: '2025-01-10', batch: 'Morning Batch A', status: 'PRESENT', coach: 'Coach Smith' },
  { id: 2, date: '2025-01-08', batch: 'Morning Batch A', status: 'PRESENT', coach: 'Coach Smith' },
  { id: 3, date: '2025-01-06', batch: 'Morning Batch A', status: 'ABSENT', coach: 'Coach Smith' },
  { id: 4, date: '2025-01-03', batch: 'Morning Batch A', status: 'PRESENT', coach: 'Coach Smith' }
]);

const coachFeedback = ref([
  { id: 1, coachName: 'Smith', comment: 'Great improvement in batting technique. Keep practicing the defensive shots.', category: 'Technical', date: '2025-01-10' },
  { id: 2, coachName: 'Smith', comment: 'Excellent fielding skills today. Work on throwing accuracy for next session.', category: 'Fielding', date: '2025-01-08' },
  { id: 3, coachName: 'Smith', comment: 'Good physical fitness. Continue with the fitness routine.', category: 'Physical', date: '2025-01-06' }
]);

const paymentForm = ref({
  method: '',
  chequeNumber: ''
});

// Methods
const getSessionStatusClass = (status) => {
  const baseClasses = 'px-2 py-1 text-xs font-medium rounded-full';
  switch (status) {
    case 'UPCOMING':
      return `${baseClasses} bg-blue-100 text-blue-800`;
    case 'ONGOING':
      return `${baseClasses} bg-green-100 text-green-800`;
    case 'COMPLETED':
      return `${baseClasses} bg-gray-100 text-gray-800`;
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

const getAttendanceStatusClass = (status) => {
  const baseClasses = 'px-2 py-1 text-xs font-medium rounded-full';
  switch (status) {
    case 'PRESENT':
      return `${baseClasses} bg-green-100 text-green-800`;
    case 'ABSENT':
      return `${baseClasses} bg-red-100 text-red-800`;
    case 'LATE':
      return `${baseClasses} bg-yellow-100 text-yellow-800`;
    default:
      return `${baseClasses} bg-gray-100 text-gray-800`;
  }
};

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};

const payFee = (fee) => {
  selectedFee.value = fee;
  showPaymentModal.value = true;
};

const processPayment = () => {
  console.log('Processing payment:', {
    fee: selectedFee.value,
    method: paymentForm.value.method,
    chequeNumber: paymentForm.value.chequeNumber
  });

  if (paymentForm.value.method === 'ONLINE') {
    // Simulate redirect to payment gateway
    alert('Redirecting to payment gateway...');
  } else {
    // Mark as paid for cash/cheque payments
    selectedFee.value.status = 'PAID';
    alert('Payment submitted successfully!');
  }

  // Reset form and close modal
  paymentForm.value = { method: '', chequeNumber: '' };
  showPaymentModal.value = false;
  selectedFee.value = null;
};

onMounted(() => {
  console.log('Player dashboard mounted');
});
</script>

