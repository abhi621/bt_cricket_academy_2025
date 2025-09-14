import { createRouter, createWebHistory } from 'vue-router';
import authService from '../services/authService.js';

import LoginPage from '../pages/LoginPage.vue';
import SelfRegistrationPage from '../pages/SelfRegistrationPage.vue';
import DashboardPage from '../pages/DashboardPage.vue';
import ApprovalDashboard from '../pages/ApprovalDashboard.vue';

const routes = [
  {
    path: '/',
    redirect: '/login'
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
    component: SelfRegistrationPage,
    meta: { requiresGuest: true }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: DashboardPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/approval-dashboard',
    name: 'ApprovalDashboard',
    component: ApprovalDashboard,
    meta: { 
      requiresAuth: true,
      requiresRole: ['ADMIN', 'SUPPORT']
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navigation guards
router.beforeEach((to, from, next) => {
  const isAuthenticated = authService.isAuthenticated();
  const currentUser = authService.getCurrentUser();

  // Check if route requires authentication
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
    return;
  }

  // Check if route requires guest (unauthenticated user)
  if (to.meta.requiresGuest && isAuthenticated) {
    next('/dashboard');
    return;
  }

  // Check if route requires specific role
  if (to.meta.requiresRole && currentUser) {
    const hasRequiredRole = to.meta.requiresRole.includes(currentUser.role);
    if (!hasRequiredRole) {
      next('/dashboard'); // Redirect to dashboard if user doesn't have required role
      return;
    }
  }

  next();
});

export default router;