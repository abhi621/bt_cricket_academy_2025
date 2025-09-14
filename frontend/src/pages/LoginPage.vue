<template>
  <div class="login-page flex items-center justify-center min-h-[80vh] py-12 px-4">
    <div class="login-card card w-full max-w-md shadow-2xl bg-white/95 backdrop-blur-sm">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-cricket-green">Welcome Back</h2>
        <p class="text-gray-600 mt-2">Sign in to your account</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-6">
        <!-- Username/Email -->
        <div>
          <label for="username" class="block text-sm font-medium text-gray-700 mb-1">
            Username or Email
          </label>
          <input
            id="username"
            v-model="loginForm.username"
            type="text"
            required
            class="input-field"
            placeholder="Enter your username or email"
          />
        </div>

        <!-- Password -->
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
            Password
          </label>
          <input
            id="password"
            v-model="loginForm.password"
            type="password"
            required
            class="input-field"
            placeholder="Enter your password"
          />
        </div>

        <!-- Error Message -->
        <div v-if="errorMessage" class="error-message bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg">
          {{ errorMessage }}
        </div>

        <!-- Pending Approval Message -->
        <div v-if="isPendingApproval" class="warning-message bg-yellow-50 border border-yellow-200 text-yellow-700 px-4 py-3 rounded-lg">
          <div class="flex items-center">
            <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
            </svg>
            Your account is pending approval. Please contact the administrator.
          </div>
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          :disabled="isLoading"
          class="btn-primary w-full text-lg py-3 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <span v-if="isLoading" class="flex items-center justify-center">
            <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            Signing in...
          </span>
          <span v-else>Sign In</span>
        </button>
      </form>

      <!-- Register Link -->
      <div class="text-center mt-6 pt-6 border-t border-gray-200">
        <p class="text-gray-600">
          Don't have an account?
          <router-link to="/register" class="text-cricket-green hover:text-cricket-green/80 font-medium">
            Register as Coach/Player
          </router-link>
        </p>
        <p class="text-sm text-gray-500 mt-2">
          <router-link to="/" class="hover:text-cricket-green">
            ← Back to Home
          </router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/authStore.js';

const router = useRouter();
const authStore = useAuthStore();

const loginForm = ref({
  username: '',
  password: ''
});

const isLoading = ref(false);
const errorMessage = ref('');
const isPendingApproval = ref(false);

const handleLogin = async () => {
  isLoading.value = true;
  errorMessage.value = '';
  isPendingApproval.value = false;
  
  try {
    const result = await authStore.login({
      username: loginForm.value.username,
      password: loginForm.value.password
    });
    
    if (result.success) {
      // Check if user is pending approval
      if (authStore.isPending) {
        isPendingApproval.value = true;
        authStore.logout(); // Clear auth state for pending users
        return;
      }
      
      // Redirect based on role
      const role = authStore.role?.toLowerCase();
      if (role && ['admin', 'support', 'coach', 'player'].includes(role)) {
        router.push(`/${role}`);
      } else {
        router.push('/');
      }
    } else {
      errorMessage.value = result.error || 'Login failed. Please try again.';
    }
  } catch (error) {
    errorMessage.value = 'Login failed. Please check your credentials and try again.';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.9));
  backdrop-filter: blur(10px);
}

.input-field:focus {
  transform: translateY(-1px);
  box-shadow: 0 10px 25px rgba(45, 80, 22, 0.1);
}

.error-message {
  animation: shake 0.5s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}
</style>