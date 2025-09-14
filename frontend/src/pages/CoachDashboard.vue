<template>
  <div class="coach-dashboard max-w-7xl mx-auto">
    <div class="mb-8">
      <div class="flex justify-between items-center">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Coach Dashboard</h1>
          <p class="text-gray-600">Manage your batches, attendance, and provide feedback</p>
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
      <BaseCard v-for="stat in coachStats" :key="stat.title" variant="elevated">
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
      <!-- Today's Schedule -->
      <BaseCard title="Today's Schedule" variant="elevated">
        <div class="space-y-4">
          <div v-for="session in todaySchedule" :key="session.id" class="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <div>
              <h4 class="font-semibold text-gray-900">{{ session.batch }}</h4>
              <p class="text-sm text-gray-600">{{ session.time }}</p>
              <p class="text-xs text-gray-500">{{ session.players }} players</p>
            </div>
            <div class="text-right">
              <span :class="getSessionStatusClass(session.status)">
                {{ session.status }}
              </span>
              <div class="mt-2">
                <button 
                  v-if="session.status === 'UPCOMING'"
                  @click="startSession(session)"
                  class="text-green-600 hover:text-green-800 text-sm"
                >
                  Start Session
                </button>
                <button 
                  v-else-if="session.status === 'ONGOING'"
                  @click="markAttendance(session)"
                  class="text-blue-600 hover:text-blue-800 text-sm"
                >
                  Mark Attendance
                </button>
              </div>
            </div>
          </div>
        </div>
      </BaseCard>

      <!-- My Batches -->
      <BaseCard title="My Batches" variant="elevated">
        <div class="space-y-3">
          <div v-for="batch in myBatches" :key="batch.id" class="flex items-center justify-between p-3 border border-gray-200 rounded-lg">
            <div>
              <h4 class="font-medium text-gray-900">{{ batch.name }}</h4>
              <p class="text-sm text-gray-600">{{ batch.time }} • {{ batch.days }}</p>
              <p class="text-xs text-gray-500">{{ batch.playerCount }} players</p>
            </div>
            <div class="flex space-x-2">
              <button @click="viewBatch(batch)" class="text-blue-600 hover:text-blue-800 text-sm">
                View
              </button>
              <button @click="addFeedback(batch)" class="text-green-600 hover:text-green-800 text-sm">
                Feedback
              </button>
            </div>
          </div>
        </div>
      </BaseCard>
    </div>

    <!-- Attendance & Feedback Section -->
    <div class="mt-8">
      <BaseCard title="Recent Attendance & Feedback" variant="elevated">
        <div class="space-y-6">
          <!-- Quick Attendance -->
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Quick Attendance (Today)</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div v-for="player in attendancePlayers" :key="player.id" class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div>
                  <span class="font-medium text-gray-900">{{ player.name }}</span>
                  <p class="text-xs text-gray-500">{{ player.batch }}</p>
                </div>
                <div class="flex items-center space-x-2">
                  <input
                    :id="`attendance-${player.id}`"
                    v-model="player.present"
                    type="checkbox"
                    class="w-4 h-4 text-green-600 bg-gray-100 border-gray-300 rounded focus:ring-green-500 focus:ring-2"
                  />
                  <label :for="`attendance-${player.id}`" class="text-sm text-gray-700">
                    {{ player.present ? 'Present' : 'Absent' }}
                  </label>
                </div>
              </div>
            </div>
            <div class="mt-4 flex justify-end">
              <button @click="saveAttendance" class="btn-primary">
                Save Attendance
              </button>
            </div>
          </div>

          <!-- Player Feedback -->
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Player Feedback</h3>
            <div class="space-y-3">
              <div v-for="feedback in recentFeedback" :key="feedback.id" class="p-4 border border-gray-200 rounded-lg">
                <div class="flex justify-between items-start mb-2">
                  <span class="font-medium text-gray-900">{{ feedback.playerName }}</span>
                  <span class="text-xs text-gray-500">{{ feedback.date }}</span>
                </div>
                <p class="text-sm text-gray-700">{{ feedback.comment }}</p>
                <div class="mt-2">
                  <span class="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
                    {{ feedback.category }}
                  </span>
                </div>
              </div>
            </div>
            <div class="mt-4">
              <button @click="showFeedbackModal = true" class="btn-secondary">
                Add New Feedback
              </button>
            </div>
          </div>
        </div>
      </BaseCard>
    </div>

    <!-- Feedback Modal -->
    <BaseModal :is-open="showFeedbackModal" @close="showFeedbackModal = false" title="Add Player Feedback">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Select Player</label>
          <select v-model="feedbackForm.playerId" class="input-field">
            <option value="">Choose a player</option>
            <option v-for="player in allPlayers" :key="player.id" :value="player.id">
              {{ player.name }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Category</label>
          <select v-model="feedbackForm.category" class="input-field">
            <option value="">Select category</option>
            <option value="Technical">Technical Skills</option>
            <option value="Physical">Physical Fitness</option>
            <option value="Mental">Mental Aspect</option>
            <option value="General">General Performance</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Feedback</label>
          <textarea 
            v-model="feedbackForm.comment" 
            rows="4" 
            class="input-field"
            placeholder="Enter your feedback..."
          ></textarea>
        </div>
      </div>

      <template #footer>
        <div class="flex justify-end space-x-3">
          <button @click="showFeedbackModal = false" class="btn-secondary">Cancel</button>
          <button @click="submitFeedback" class="btn-primary">Add Feedback</button>
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

const showFeedbackModal = ref(false);

// Mock data
const coachStats = ref([
  {
    title: 'My Batches',
    value: '3',
    icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
    bgColor: 'bg-blue-500'
  },
  {
    title: 'Total Players',
    value: '45',
    icon: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z',
    bgColor: 'bg-green-500'
  },
  {
    title: 'Today\'s Sessions',
    value: '2',
    icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-yellow-500'
  },
  {
    title: 'Attendance Rate',
    value: '94%',
    icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    bgColor: 'bg-purple-500'
  }
]);

const todaySchedule = ref([
  { id: 1, batch: 'Morning Batch A', time: '6:00 AM - 8:00 AM', players: 15, status: 'COMPLETED' },
  { id: 2, batch: 'Evening Batch B', time: '4:00 PM - 6:00 PM', players: 18, status: 'UPCOMING' }
]);

const myBatches = ref([
  { id: 1, name: 'Morning Batch A', time: '6:00 AM - 8:00 AM', days: 'Mon, Wed, Fri', playerCount: 15 },
  { id: 2, name: 'Evening Batch B', time: '4:00 PM - 6:00 PM', days: 'Tue, Thu, Sat', playerCount: 18 },
  { id: 3, name: 'Weekend Special', time: '8:00 AM - 10:00 AM', days: 'Sunday', playerCount: 12 }
]);

const attendancePlayers = ref([
  { id: 1, name: 'John Smith', batch: 'Evening Batch B', present: true },
  { id: 2, name: 'Sarah Connor', batch: 'Evening Batch B', present: false },
  { id: 3, name: 'Mike Johnson', batch: 'Evening Batch B', present: true },
  { id: 4, name: 'Emily Davis', batch: 'Evening Batch B', present: true }
]);

const recentFeedback = ref([
  { id: 1, playerName: 'John Smith', comment: 'Great improvement in batting technique. Focus more on footwork.', category: 'Technical', date: '2025-01-10' },
  { id: 2, playerName: 'Sarah Connor', comment: 'Excellent fielding skills. Continue working on throwing accuracy.', category: 'Technical', date: '2025-01-09' }
]);

const allPlayers = ref([
  { id: 1, name: 'John Smith' },
  { id: 2, name: 'Sarah Connor' },
  { id: 3, name: 'Mike Johnson' },
  { id: 4, name: 'Emily Davis' }
]);

const feedbackForm = ref({
  playerId: '',
  category: '',
  comment: ''
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

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};

const startSession = (session) => {
  console.log('Starting session:', session);
  session.status = 'ONGOING';
};

const markAttendance = (session) => {
  console.log('Marking attendance for session:', session);
  // Navigate to attendance marking interface
};

const viewBatch = (batch) => {
  console.log('Viewing batch:', batch);
  // Navigate to batch details
};

const addFeedback = (batch) => {
  console.log('Adding feedback for batch:', batch);
  showFeedbackModal.value = true;
};

const saveAttendance = () => {
  console.log('Saving attendance:', attendancePlayers.value);
  alert('Attendance saved successfully!');
};

const submitFeedback = () => {
  console.log('Submitting feedback:', feedbackForm.value);
  // Add to recent feedback
  recentFeedback.value.unshift({
    id: Date.now(),
    playerName: allPlayers.value.find(p => p.id == feedbackForm.value.playerId)?.name || '',
    comment: feedbackForm.value.comment,
    category: feedbackForm.value.category,
    date: new Date().toISOString().split('T')[0]
  });
  
  // Reset form
  feedbackForm.value = { playerId: '', category: '', comment: '' };
  showFeedbackModal.value = false;
  alert('Feedback added successfully!');
};

onMounted(() => {
  console.log('Coach dashboard mounted');
});
</script>

