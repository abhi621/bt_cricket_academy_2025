<template>
  <div class="register-page">
    <div class="register-container">
      <h2>Register as Coach/Player</h2>
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-group">
          <label for="username">Username:</label>
          <input
            type="text"
            id="username"
            v-model="registerForm.username"
            required
            class="form-input"
            placeholder="Enter your username"
          />
        </div>
        
        <div class="form-group">
          <label for="email">Email:</label>
          <input
            type="email"
            id="email"
            v-model="registerForm.email"
            required
            class="form-input"
            placeholder="Enter your email"
          />
        </div>
        
        <div class="form-group">
          <label for="password">Password:</label>
          <input
            type="password"
            id="password"
            v-model="registerForm.password"
            required
            class="form-input"
            placeholder="Enter your password"
          />
        </div>
        
        <div class="form-group">
          <label for="role">Role:</label>
          <select
            id="role"
            v-model="registerForm.role"
            required
            class="form-input"
          >
            <option value="">Select your role</option>
            <option value="COACH">Coach</option>
            <option value="PLAYER">Player</option>
          </select>
        </div>
        
        <button type="submit" :disabled="isLoading" class="register-btn">
          {{ isLoading ? 'Registering...' : 'Register' }}
        </button>
      </form>
      
      <div v-if="successMessage" class="success-message">
        {{ successMessage }}
      </div>
      
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      
      <div class="login-link">
        <p>Already have an account? 
          <router-link to="/login">Login here</router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../services/authService.js';

const router = useRouter();

const registerForm = ref({
  username: '',
  email: '',
  password: '',
  role: ''
});

const isLoading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

const handleRegister = async () => {
  isLoading.value = true;
  errorMessage.value = '';
  successMessage.value = '';
  
  try {
    const response = await authService.register(registerForm.value);
    
    if (response.message) {
      if (response.message.includes('successful')) {
        successMessage.value = response.message;
        // Clear form on success
        registerForm.value = {
          username: '',
          email: '',
          password: '',
          role: ''
        };
      } else {
        errorMessage.value = response.message;
      }
    }
  } catch (error) {
    errorMessage.value = error.message || 'Registration failed. Please try again.';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
}

.register-container {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 450px;
}

.register-form {
  margin-top: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-input:focus {
  outline: none;
  border-color: #28a745;
  box-shadow: 0 0 0 2px rgba(40, 167, 69, 0.25);
}

.register-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1rem;
}

.register-btn:hover:not(:disabled) {
  background-color: #218838;
}

.register-btn:disabled {
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

.login-link {
  margin-top: 1.5rem;
  text-align: center;
}

.login-link a {
  color: #007bff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>