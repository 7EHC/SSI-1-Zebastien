import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'

export const useModeStore = defineStore('mode', () => {
    const AcCloseBut = useLocalStorage('activeClose', false)

    const setMode = (mode) => {
        AcCloseBut.value = mode;
    };

    return {
        AcCloseBut,
        setMode
    }
  })