import { defineStore } from 'pinia'
import { useLocalStorage, useSessionStorage } from '@vueuse/core'

export const useCategoryStore = defineStore('category', () => {
    const categoryFilter = useLocalStorage('category', 0)

    const setCategory = (category) => {
        categoryFilter.value = category;
    };

    return {
        categoryFilter,
        setCategory
    }
  })