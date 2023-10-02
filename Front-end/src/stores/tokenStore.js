import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'

export const useTokenStore = defineStore('token', () => {
    const accessToken = useLocalStorage('accessToken', '')
    const refreshToken = useLocalStorage('refreshToken', '')

    const setAccessToken = (accToken) => {
        accessToken.value = accToken;
    };

    const setRefreshToken = (refToken) => {
        refreshToken.value = refToken;
    };

    return {
        accessToken,
        refreshToken,
        setAccessToken,
        setRefreshToken
    }
  })