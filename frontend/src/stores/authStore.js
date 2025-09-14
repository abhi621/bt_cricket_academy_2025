import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import authService from '../services/authService.js';

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref(null);
  const token = ref(null);
  const role = ref(null);
  const isLoading = ref(false);
  const error = ref(null);

  // Load from localStorage on store initialization
  const storedToken = localStorage.getItem('auth_token');
  const storedUser = localStorage.getItem('auth_user');
  
  if (storedToken && storedUser) {
    token.value = storedToken;
    user.value = JSON.parse(storedUser);
    role.value = user.value?.role;
  }

  // Computed
  const isAuthenticated = computed(() => !!token.value && !!user.value);
  const isAdmin = computed(() => role.value === 'ADMIN');
  const isSupport = computed(() => role.value === 'SUPPORT');
  const isCoach = computed(() => role.value === 'COACH');
  const isPlayer = computed(() => role.value === 'PLAYER');
  const isPending = computed(() => user.value?.status === 'PENDING');

  // Actions
  async function login(credentials) {
    try {
      isLoading.value = true;
      error.value = null;
      
      const response = await authService.login(credentials);
      
      if (response.token && response.user) {
        token.value = response.token;
        user.value = response.user;
        role.value = response.user.role;
        
        // Persist to localStorage
        localStorage.setItem('auth_token', response.token);
        localStorage.setItem('auth_user', JSON.stringify(response.user));
        
        return { success: true };
      } else {
        throw new Error('Invalid login response');
      }
    } catch (err) {
      error.value = err.response?.data?.message || err.message || 'Login failed';
      return { success: false, error: error.value };
    } finally {
      isLoading.value = false;
    }
  }

  async function register(userData) {
    try {
      isLoading.value = true;
      error.value = null;
      
      const response = await authService.register(userData);
      
      if (response.success) {
        return { success: true, message: 'Registration successful! Please wait for approval.' };
      } else {
        throw new Error(response.message || 'Registration failed');
      }
    } catch (err) {
      error.value = err.response?.data?.message || err.message || 'Registration failed';
      return { success: false, error: error.value };
    } finally {
      isLoading.value = false;
    }
  }

  async function fetchCurrentUser() {
    try {
      if (!token.value) return;
      
      isLoading.value = true;
      const response = await authService.getCurrentUser();
      
      if (response.user) {
        user.value = response.user;
        role.value = response.user.role;
        localStorage.setItem('auth_user', JSON.stringify(response.user));
      }
    } catch (err) {
      console.error('Error fetching current user:', err);
      // If token is invalid, clear auth state
      if (err.response?.status === 401) {
        logout();
      }
    } finally {
      isLoading.value = false;
    }
  }

  function logout() {
    user.value = null;
    token.value = null;
    role.value = null;
    error.value = null;
    
    // Clear localStorage
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
  }

  function clearError() {
    error.value = null;
  }

  return {
    // State
    user,
    token,
    role,
    isLoading,
    error,
    
    // Computed
    isAuthenticated,
    isAdmin,
    isSupport,
    isCoach,
    isPlayer,
    isPending,
    
    // Actions
    login,
    register,
    fetchCurrentUser,
    logout,
    clearError
  };
});