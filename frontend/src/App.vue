<template>
  <div id="app" class="bg">
    <header class="site-title">
      <h1>BT Cricket Academy</h1>
    </header>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
// App component now uses router-view to render different pages
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
}

body {
  font-family: 'Arial', sans-serif;
  line-height: 1.6;
  color: #222;
}

/* Background: uses /lords.jpg if present under frontend/public; falls back to gradient */
.bg {
  min-height: 100vh;
  background-image: linear-gradient(rgba(0,0,0,0.35), rgba(0,0,0,0.6)), url('/lords.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  display: flex;
  flex-direction: column;
}

.site-title {
  width: 100%;
  text-align: center;
  padding: 24px 16px;
}

.site-title h1 {
  color: #fff;
  font-size: 2rem;
  letter-spacing: 1px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.6);
}

.content {
  flex: 1;
  width: min(1100px, 94%);
  margin: 12px auto 32px;
  background: rgba(255,255,255,0.9);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.25);
  padding: 20px;
}

@media (min-width: 768px) {
  .site-title h1 { font-size: 2.4rem; }
  .content { padding: 28px; }
}
</style>
