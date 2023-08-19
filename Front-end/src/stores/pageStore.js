import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'

export const usePageStore = defineStore('page', () => {
    const page = useLocalStorage('page', 0);
    const pageClose = useLocalStorage('pageClose', 0);
  
    const setPage = (newPage) => {
      page.value = newPage;
    };

    const setClosePage = (newPage) => {
      pageClose.value = newPage;
    };
  
    return {
      page,
      pageClose,
      setPage,
      setClosePage
    };
  })
