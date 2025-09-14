<template>
  <div id="app" class="min-h-screen bg-cover bg-center bg-fixed bg-stadium">
    <!-- Global overlay for readability -->
    <div class="min-h-screen bg-black/40">
      <!-- Semi-transparent white overlay behind content -->
      <div class="min-h-screen bg-gradient-to-br from-white/90 to-white/80">
        <header class="w-full text-center py-6 bg-gradient-to-r from-cricket-green/90 to-cricket-green/70">
          <h1 class="text-white text-3xl md:text-4xl font-bold tracking-wide drop-shadow-lg">
            BT Cricket Academy
          </h1>
        </header>
        
        <main class="flex-1 container mx-auto px-4 py-8">
          <router-view />
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAuthStore } from './stores/authStore.js';

const authStore = useAuthStore();

onMounted(() => {
  // Fetch current user if token exists
  if (authStore.token) {
    authStore.fetchCurrentUser();
  }
});
</script>

<style>
/* Any additional global styles can go here */
</style>
