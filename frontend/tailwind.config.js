/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        cricket: {
          green: '#2D5016',
          gold: '#FFD700',
          brown: '#8B4513'
        }
      },
      backgroundImage: {
        'stadium': "url('/assets/stadium-bg.jpg')"
      }
    },
  },
  plugins: [],
}