<template>
  <div :class="cardClasses">
    <div v-if="title" class="card-header mb-4">
      <h3 :class="titleClasses">{{ title }}</h3>
    </div>
    <div class="card-body">
      <slot></slot>
    </div>
    <div v-if="hasFooter" class="card-footer mt-4 pt-4 border-t border-gray-200">
      <slot name="footer"></slot>
    </div>
  </div>
</template>

<script setup>
import { computed, useSlots } from 'vue';

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  variant: {
    type: String,
    default: 'default',
    validator: (value) => ['default', 'elevated', 'outlined'].includes(value)
  },
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  },
  titleSize: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg', 'xl'].includes(value)
  }
});

const slots = useSlots();

const hasFooter = computed(() => !!slots.footer);

const cardClasses = computed(() => {
  const baseClasses = 'rounded-lg bg-white';
  const variantClasses = {
    default: 'shadow-sm border border-gray-200',
    elevated: 'shadow-lg',
    outlined: 'border-2 border-cricket-green'
  };
  const sizeClasses = {
    sm: 'p-4',
    md: 'p-6',
    lg: 'p-8'
  };
  
  return [
    baseClasses,
    variantClasses[props.variant],
    sizeClasses[props.size]
  ].join(' ');
});

const titleClasses = computed(() => {
  const sizeClasses = {
    sm: 'text-lg',
    md: 'text-xl',
    lg: 'text-2xl',
    xl: 'text-3xl'
  };
  
  return [
    'font-semibold text-green-700',
    sizeClasses[props.titleSize]
  ].join(' ');
});
</script>