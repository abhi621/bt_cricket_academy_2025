<template>
  <div class="register-page flex items-center justify-center min-h-[80vh] py-12 px-4">
    <div class="register-card card w-full max-w-md shadow-2xl bg-white/95 backdrop-blur-sm">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-cricket-green">Create Account</h2>
        <p class="text-gray-600 mt-2">Join BT Cricket Academy</p>
      </div>

      <form @submit.prevent="handleRegister" class="space-y-6">
        <!-- Name -->
        <div>
          <label for="name" class="block text-sm font-medium text-gray-700 mb-1">
            Full Name *
          </label>
          <input
            id="name"
            v-model="form.name"
            type="text"
            required
            class="input-field"
            placeholder="Enter your full name"
          />
        </div>

        <!-- Email -->
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
            Email Address *
          </label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            required
            class="input-field"
            placeholder="Enter your email"
          />
        </div>

        <!-- Phone -->
        <div>
          <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
            Phone Number *
          </label>
          <input
            id="phone"
            v-model="form.phone"
            type="tel"
            required
            class="input-field"
            placeholder="Enter your phone number"
          />
        </div>

        <!-- Role Selection -->
        <div>
          <label for="role" class="block text-sm font-medium text-gray-700 mb-1">
            Register as *
          </label>
          <select
            id="role"
            v-model="form.role"
            required
            class="input-field"
          >
            <option value="">Select Role</option>
            <option value="COACH">Coach</option>
            <option value="PLAYER">Player</option>
          </select>
        </div>

        <!-- Password -->
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
            Password *
          </label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            required
            class="input-field"
            placeholder="Create a strong password"
          />
        </div>

        <!-- Confirm Password -->
        <div>
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1">
            Confirm Password *
          </label>
          <input
            id="confirmPassword"
            v-model="form.confirmPassword"
            type="password"
            required
            class="input-field"
            placeholder="Confirm your password"
          />
        </div>

        <!-- Error Message -->
        <div v-if="error" class="error-message bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg">
          {{ error }}
        </div>

        <!-- Success Message -->
        <div v-if="successMessage" class="success-message bg-green-50 border border-green-200 text-green-600 px-4 py-3 rounded-lg">
          {{ successMessage }}
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
            Creating Account...
          </span>
          <span v-else>Create Account</span>
        </button>
      </form>

      <!-- Login Link -->
      <div class="text-center mt-6 pt-6 border-t border-gray-200">
        <p class="text-gray-600">
          Already have an account?
          <router-link to="/login" class="text-cricket-green hover:text-cricket-green/80 font-medium">
            Sign in here
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

const form = ref({
  name: '',
  email: '',
  phone: '',
  role: '',
  password: '',
  confirmPassword: ''
});

const error = ref('');
const successMessage = ref('');
const isLoading = ref(false);

const handleRegister = async () => {
  error.value = '';
  successMessage.value = '';

  // Validate passwords match
  if (form.value.password !== form.value.confirmPassword) {
    error.value = 'Passwords do not match';
    return;
  }

  // Validate password strength
  if (form.value.password.length < 6) {
    error.value = 'Password must be at least 6 characters long';
    return;
  }

  isLoading.value = true;

  try {
    const result = await authStore.register({
      name: form.value.name,
      email: form.value.email,
      phone: form.value.phone,
      role: form.value.role,
      password: form.value.password
    });

    if (result.success) {
      successMessage.value = result.message || 'Registration successful! Please wait for approval before logging in.';
      // Reset form
      form.value = {
        name: '',
        email: '',
        phone: '',
        role: '',
        password: '',
        confirmPassword: ''
      };
      
      // Redirect to login after 3 seconds
      setTimeout(() => {
        router.push('/login');
      }, 3000);
    } else {
      error.value = result.error;
    }
  } catch (err) {
    error.value = 'Registration failed. Please try again.';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.register-card {
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