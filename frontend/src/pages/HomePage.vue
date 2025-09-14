<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero-section relative py-20 px-4 text-center">
      <!-- Dark gradient overlay for better text visibility -->
      <div class="absolute inset-0 bg-gradient-to-b from-black/60 to-black/40 rounded-lg"></div>
      
      <div class="relative z-10 max-w-4xl mx-auto">
        <h1 class="text-4xl md:text-6xl font-bold text-white mb-6 drop-shadow-2xl">
          Welcome to BT Cricket Academy
        </h1>
        <p class="text-xl md:text-2xl text-white/90 mb-8 drop-shadow-lg">
          Premier Training Facility for Cricket Excellence
        </p>
        <p class="text-lg text-white/80 mb-10 max-w-2xl mx-auto drop-shadow-lg">
          Transform your cricket skills with professional coaching, state-of-the-art facilities, 
          and comprehensive training programs designed for all skill levels.
        </p>
        
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
          <router-link 
            to="/login" 
            class="btn-primary text-lg px-8 py-3 shadow-lg hover:shadow-xl transform hover:scale-105 transition-all"
          >
            Login
          </router-link>
          <router-link 
            to="/register" 
            class="btn-secondary text-lg px-8 py-3 shadow-lg hover:shadow-xl transform hover:scale-105 transition-all bg-white/90 hover:bg-white"
          >
            Register Now
          </router-link>
        </div>
      </div>
    </section>

    <!-- About Section -->
    <section class="about-section py-16 px-4">
      <div class="max-w-6xl mx-auto">
        <div class="card">
          <h2 class="text-3xl font-bold text-cricket-green mb-6 text-center">About Our Academy</h2>
          <div class="text-gray-700 text-lg leading-relaxed">
            <p>{{ academyInfo.about }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Fee Structure Section -->
    <section class="fee-structure-section py-16 px-4 bg-gray-50/80">
      <div class="max-w-6xl mx-auto">
        <h2 class="text-3xl font-bold text-cricket-green mb-10 text-center">Training Programs & Fees</h2>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div 
            v-for="fee in academyInfo.feeStructure" 
            :key="fee.category"
            class="fee-card card hover:shadow-xl transition-shadow border-l-4 border-cricket-green"
          >
            <h3 class="text-xl font-semibold text-cricket-green mb-2">{{ fee.category }}</h3>
            <div class="text-3xl font-bold text-cricket-gold mb-2">₹{{ fee.amount }}</div>
            <div class="text-sm text-gray-600 mb-3">{{ fee.duration }}</div>
            <p class="text-gray-700 text-sm">{{ fee.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Section -->
    <section class="contact-section py-16 px-4">
      <div class="max-w-4xl mx-auto">
        <div class="card">
          <h2 class="text-3xl font-bold text-cricket-green mb-8 text-center">Contact Information</h2>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div class="contact-item">
              <h3 class="text-lg font-semibold text-cricket-green mb-2">Phone</h3>
              <p class="text-gray-700">{{ academyInfo.contact.phone }}</p>
            </div>
            
            <div class="contact-item">
              <h3 class="text-lg font-semibold text-cricket-green mb-2">Email</h3>
              <p class="text-gray-700">{{ academyInfo.contact.email }}</p>
            </div>
            
            <div class="contact-item">
              <h3 class="text-lg font-semibold text-cricket-green mb-2">Address</h3>
              <p class="text-gray-700">{{ academyInfo.contact.address }}</p>
            </div>
            
            <div class="contact-item">
              <h3 class="text-lg font-semibold text-cricket-green mb-2">Timings</h3>
              <p class="text-gray-700">{{ academyInfo.contact.timings }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const academyInfo = ref({
  about: '',
  feeStructure: [],
  contact: {}
});

const loadAcademyInfo = async () => {
  try {
    const response = await fetch('/academy-info.json');
    const data = await response.json();
    academyInfo.value = data;
  } catch (error) {
    console.error('Error loading academy info:', error);
    // Set default data if file is not available
    academyInfo.value = {
      about: "BT Cricket Academy is a premier training facility dedicated to nurturing cricket talent.",
      feeStructure: [
        { category: "Beginner", amount: "2000", duration: "Monthly", description: "Basic coaching" },
        { category: "Intermediate", amount: "3000", duration: "Monthly", description: "Advanced techniques" }
      ],
      contact: {
        phone: "+91-7518171558",
        email: "info@btcricketacademy.com",
        address: "BT Cricket Academy, New Delhi",
        timings: "Monday - Saturday: 6:00 AM - 8:00 PM"
      }
    };
  }
};

onMounted(() => {
  loadAcademyInfo();
});
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, rgba(45, 80, 22, 0.9), rgba(139, 69, 19, 0.7));
  border-radius: 12px;
  margin-bottom: 2rem;
}

.fee-card:hover {
  transform: translateY(-2px);
}

.contact-item h3 {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>