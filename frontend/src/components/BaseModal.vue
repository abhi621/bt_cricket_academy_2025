<template>
  <!-- Modal Backdrop -->
  <div 
    v-if="isOpen" 
    class="modal-backdrop fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4"
    @click="handleBackdropClick"
  >
    <!-- Modal Container -->
    <div 
      :class="modalClasses"
      @click.stop
    >
      <!-- Modal Header -->
      <div v-if="title || showCloseButton" class="modal-header flex items-center justify-between p-6 border-b border-gray-200">
        <h3 v-if="title" class="text-xl font-semibold text-cricket-green">{{ title }}</h3>
        <button 
          v-if="showCloseButton"
          @click="closeModal"
          class="text-gray-400 hover:text-gray-600 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>

      <!-- Modal Body -->
      <div :class="bodyClasses">
        <slot></slot>
      </div>

      <!-- Modal Footer -->
      <div v-if="hasFooter" class="modal-footer p-6 border-t border-gray-200 bg-gray-50">
        <slot name="footer"></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, useSlots, watch } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg', 'xl'].includes(value)
  },
  showCloseButton: {
    type: Boolean,
    default: true
  },
  closeOnBackdrop: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['close']);

const slots = useSlots();

const hasFooter = computed(() => !!slots.footer);

const modalClasses = computed(() => {
  const baseClasses = 'modal-content bg-white rounded-lg shadow-xl max-h-[90vh] overflow-y-auto';
  const sizeClasses = {
    sm: 'max-w-md w-full',
    md: 'max-w-lg w-full',
    lg: 'max-w-2xl w-full',
    xl: 'max-w-4xl w-full'
  };
  
  return [baseClasses, sizeClasses[props.size]].join(' ');
});

const bodyClasses = computed(() => {
  const baseClasses = 'modal-body';
  const paddingClasses = props.title || props.showCloseButton ? 'p-6' : 'p-6';
  
  return [baseClasses, paddingClasses].join(' ');
});

const closeModal = () => {
  emit('close');
};

const handleBackdropClick = () => {
  if (props.closeOnBackdrop) {
    closeModal();
  }
};

// Handle ESC key to close modal
watch(() => props.isOpen, (newValue) => {
  if (newValue) {
    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        closeModal();
      }
    };
    document.addEventListener('keydown', handleEscape);
    
    // Cleanup
    return () => {
      document.removeEventListener('keydown', handleEscape);
    };
  }
});
</script>

<style scoped>
.modal-backdrop {
  animation: fadeIn 0.3s ease-out;
}

.modal-content {
  animation: slideIn 0.3s ease-out;
  transform-origin: center;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(-10px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}
</style>