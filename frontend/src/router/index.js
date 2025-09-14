import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../stores/authStore.js';

// Import components
import HomePage from '../pages/HomePage.vue';
import LoginPage from '../pages/LoginPage.vue';
import RegisterPage from '../pages/RegisterPage.vue';
import AdminDashboard from '../pages/AdminDashboard.vue';
import SupportDashboard from '../pages/SupportDashboard.vue';
import CoachDashboard from '../pages/CoachDashboard.vue';
import PlayerDashboard from '../pages/PlayerDashboard.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage,
    meta: { public: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage,
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
    meta: { requiresGuest: true }
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { 
      requiresAuth: true,
      requiresRole: ['ADMIN']
    }
  },
  {
    path: '/support',
    name: 'SupportDashboard',
    component: SupportDashboard,
    meta: { 
      requiresAuth: true,
      requiresRole: ['SUPPORT']
    }
  },
  {
    path: '/coach',
    name: 'CoachDashboard',
    component: CoachDashboard,
    meta: { 
      requiresAuth: true,
      requiresRole: ['COACH']
    }
  },
  {
    path: '/player',
    name: 'PlayerDashboard',
    component: PlayerDashboard,
    meta: { 
      requiresAuth: true,
      requiresRole: ['PLAYER']
    }
  },
  // Legacy routes for backward compatibility
  {
    path: '/dashboard',
    redirect: (to) => {
      const authStore = useAuthStore();
      const role = authStore.role?.toLowerCase();
      return role ? `/${role}` : '/login';
    }
  },
  {
    path: '/approval-dashboard',
    redirect: '/admin'
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navigation guards
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.isAuthenticated;
  const userRole = authStore.role;

  // Check if route requires authentication
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
    return;
  }

  // Check if route requires guest (unauthenticated user)
  if (to.meta.requiresGuest && isAuthenticated) {
    // Redirect to appropriate dashboard based on role
    const role = userRole?.toLowerCase();
    next(role ? `/${role}` : '/');
    return;
  }

  // Check if route requires specific role
  if (to.meta.requiresRole && userRole) {
    const hasRequiredRole = to.meta.requiresRole.includes(userRole);
    if (!hasRequiredRole) {
      // Redirect to appropriate dashboard based on role
      const role = userRole?.toLowerCase();
      next(role ? `/${role}` : '/');
      return;
    }
  }

  next();
});

export default router;