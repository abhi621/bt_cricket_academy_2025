import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Gateway URL

class AuthService {
  constructor() {
    this.token = localStorage.getItem('token');
    this.setupInterceptors();
  }

  setupInterceptors() {
    // Add token to all requests
    axios.interceptors.request.use(
      (config) => {
        if (this.token) {
          config.headers.Authorization = `Bearer ${this.token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Handle responses
    axios.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          this.logout();
        }
        return Promise.reject(error);
      }
    );
  }

  async login(credentials) {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials);
      if (response.data.token) {
        this.token = response.data.token;
        localStorage.setItem('token', this.token);
        localStorage.setItem('user', JSON.stringify({
          username: response.data.username,
          role: response.data.role,
          approved: response.data.approved
        }));
      }
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Login failed' };
    }
  }

  async register(userData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/register`, userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Registration failed' };
    }
  }

  async approveUser(userId) {
    try {
      const response = await axios.patch(`${API_BASE_URL}/auth/approve/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Approval failed' };
    }
  }

  async getPendingApprovals() {
    try {
      const response = await axios.get(`${API_BASE_URL}/auth/pending-approvals`);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to fetch pending approvals' };
    }
  }

  logout() {
    this.token = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  isAuthenticated() {
    return !!this.token;
  }

  hasRole(role) {
    const user = this.getCurrentUser();
    return user && user.role === role;
  }
}

export default new AuthService();