<template>
  <div class="base-table-container">
    <!-- Table Actions -->
    <div v-if="hasActions" class="table-actions mb-4 flex flex-col sm:flex-row gap-4 justify-between">
      <div class="search-container">
        <slot name="search">
          <div v-if="searchable" class="relative">
            <input
              v-model="searchQuery"
              type="text"
              :placeholder="searchPlaceholder"
              class="input-field pl-10"
            />
            <svg class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
            </svg>
          </div>
        </slot>
      </div>
      
      <div class="action-buttons">
        <slot name="actions"></slot>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="loading-container text-center py-8">
      <div class="inline-flex items-center">
        <svg class="animate-spin h-8 w-8 text-cricket-green mr-3" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <span class="text-gray-600">Loading...</span>
      </div>
    </div>

    <!-- Table -->
    <div v-else class="table-wrapper overflow-x-auto rounded-lg border border-gray-200">
      <table class="w-full table-auto">
        <thead class="bg-gray-50">
          <tr>
            <th 
              v-for="column in columns" 
              :key="column.key"
              :class="getHeaderClasses(column)"
              @click="handleSort(column)"
            >
              <div class="flex items-center gap-2">
                <span>{{ column.label }}</span>
                <div v-if="column.sortable" class="sort-icons">
                  <svg 
                    v-if="sortBy === column.key && sortOrder === 'asc'" 
                    class="w-4 h-4 text-cricket-green" 
                    fill="currentColor" 
                    viewBox="0 0 20 20"
                  >
                    <path d="M3 3a1 1 0 000 2h11a1 1 0 100-2H3zM3 7a1 1 0 000 2h5a1 1 0 000-2H3zM3 11a1 1 0 100 2h4a1 1 0 100-2H3zM13 16a1 1 0 102 0v-5.586l1.293 1.293a1 1 0 001.414-1.414l-3-3a1 1 0 00-1.414 0l-3 3a1 1 0 101.414 1.414L13 10.414V16z"></path>
                  </svg>
                  <svg 
                    v-else-if="sortBy === column.key && sortOrder === 'desc'" 
                    class="w-4 h-4 text-cricket-green" 
                    fill="currentColor" 
                    viewBox="0 0 20 20"
                  >
                    <path d="M3 3a1 1 0 000 2h11a1 1 0 100-2H3zM3 7a1 1 0 000 2h7a1 1 0 100-2H3zM3 11a1 1 0 100 2h4a1 1 0 100-2H3zM15 8a1 1 0 10-2 0v5.586l-1.293-1.293a1 1 0 00-1.414 1.414l3 3a1 1 0 001.414 0l3-3a1 1 0 00-1.414-1.414L15 13.586V8z"></path>
                  </svg>
                  <svg 
                    v-else-if="column.sortable" 
                    class="w-4 h-4 text-gray-400" 
                    fill="currentColor" 
                    viewBox="0 0 20 20"
                  >
                    <path d="M5 12a1 1 0 102 0V6.414l1.293 1.293a1 1 0 001.414-1.414l-3-3a1 1 0 00-1.414 0l-3 3a1 1 0 001.414 1.414L5 6.414V12zM15 8a1 1 0 10-2 0v5.586l-1.293-1.293a1 1 0 00-1.414 1.414l3 3a1 1 0 001.414 0l3-3a1 1 0 00-1.414-1.414L15 13.586V8z"></path>
                  </svg>
                </div>
              </div>
            </th>
            <th v-if="hasRowActions" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-if="!filteredData.length" class="no-data">
            <td :colspan="columns.length + (hasRowActions ? 1 : 0)" class="px-6 py-8 text-center text-gray-500">
              {{ emptyMessage }}
            </td>
          </tr>
          <tr 
            v-for="(item, index) in paginatedData" 
            :key="getRowKey(item, index)"
            class="hover:bg-gray-50 transition-colors"
          >
            <td 
              v-for="column in columns" 
              :key="column.key"
              :class="getCellClasses(column)"
            >
              <slot 
                :name="`cell-${column.key}`" 
                :item="item" 
                :value="getValue(item, column.key)"
                :index="index"
              >
                <span>{{ formatValue(getValue(item, column.key), column) }}</span>
              </slot>
            </td>
            <td v-if="hasRowActions" class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <slot name="row-actions" :item="item" :index="index"></slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div v-if="paginated && !loading" class="pagination-container mt-4 flex flex-col sm:flex-row items-center justify-between gap-4">
      <div class="pagination-info text-sm text-gray-700">
        Showing {{ startIndex + 1 }} to {{ endIndex }} of {{ filteredData.length }} entries
      </div>
      
      <div class="pagination-controls flex items-center gap-2">
        <button
          @click="currentPage = 1"
          :disabled="currentPage === 1"
          class="pagination-btn"
        >
          First
        </button>
        <button
          @click="currentPage--"
          :disabled="currentPage === 1"
          class="pagination-btn"
        >
          Previous
        </button>
        
        <span class="pagination-page-info text-sm text-gray-600">
          Page {{ currentPage }} of {{ totalPages }}
        </span>
        
        <button
          @click="currentPage++"
          :disabled="currentPage === totalPages"
          class="pagination-btn"
        >
          Next
        </button>
        <button
          @click="currentPage = totalPages"
          :disabled="currentPage === totalPages"
          class="pagination-btn"
        >
          Last
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, useSlots, watch } from 'vue';

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  searchable: {
    type: Boolean,
    default: false
  },
  searchPlaceholder: {
    type: String,
    default: 'Search...'
  },
  paginated: {
    type: Boolean,
    default: true
  },
  pageSize: {
    type: Number,
    default: 10
  },
  emptyMessage: {
    type: String,
    default: 'No data available'
  },
  rowKey: {
    type: String,
    default: 'id'
  }
});

const emit = defineEmits(['sort', 'search']);

const slots = useSlots();

// Search
const searchQuery = ref('');

// Sorting
const sortBy = ref('');
const sortOrder = ref('asc');

// Pagination
const currentPage = ref(1);

// Computed properties
const hasActions = computed(() => !!slots.search || !!slots.actions || props.searchable);
const hasRowActions = computed(() => !!slots['row-actions']);

const filteredData = computed(() => {
  let result = [...props.data];
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(item => {
      return props.columns.some(column => {
        const value = getValue(item, column.key);
        return String(value).toLowerCase().includes(query);
      });
    });
    emit('search', searchQuery.value);
  }
  
  if (sortBy.value) {
    result.sort((a, b) => {
      const aVal = getValue(a, sortBy.value);
      const bVal = getValue(b, sortBy.value);
      
      if (sortOrder.value === 'asc') {
        return aVal > bVal ? 1 : -1;
      } else {
        return aVal < bVal ? 1 : -1;
      }
    });
  }
  
  return result;
});

const totalPages = computed(() => {
  if (!props.paginated) return 1;
  return Math.ceil(filteredData.value.length / props.pageSize);
});

const startIndex = computed(() => {
  if (!props.paginated) return 0;
  return (currentPage.value - 1) * props.pageSize;
});

const endIndex = computed(() => {
  if (!props.paginated) return filteredData.value.length;
  return Math.min(startIndex.value + props.pageSize, filteredData.value.length);
});

const paginatedData = computed(() => {
  if (!props.paginated) return filteredData.value;
  return filteredData.value.slice(startIndex.value, endIndex.value);
});

// Methods
const getValue = (item, key) => {
  return key.split('.').reduce((obj, k) => obj?.[k], item);
};

const getRowKey = (item, index) => {
  return getValue(item, props.rowKey) || index;
};

const formatValue = (value, column) => {
  if (column.formatter && typeof column.formatter === 'function') {
    return column.formatter(value);
  }
  return value;
};

const getHeaderClasses = (column) => {
  const baseClasses = 'px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider';
  const sortableClasses = column.sortable ? 'cursor-pointer hover:bg-gray-100' : '';
  return [baseClasses, sortableClasses].join(' ');
};

const getCellClasses = (column) => {
  return `px-6 py-4 whitespace-nowrap text-sm ${column.align === 'center' ? 'text-center' : column.align === 'right' ? 'text-right' : 'text-left'}`;
};

const handleSort = (column) => {
  if (!column.sortable) return;
  
  if (sortBy.value === column.key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortBy.value = column.key;
    sortOrder.value = 'asc';
  }
  
  emit('sort', { column: column.key, order: sortOrder.value });
};

// Reset pagination when search changes
watch(searchQuery, () => {
  currentPage.value = 1;
});
</script>

<style scoped>
.pagination-btn {
  @apply px-3 py-1 text-sm border border-gray-300 rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed;
}

.pagination-btn:disabled {
  @apply hover:bg-white;
}
</style>