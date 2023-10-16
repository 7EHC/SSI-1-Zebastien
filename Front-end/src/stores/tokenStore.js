import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'

export const useTokenStore = defineStore('token', () => {
    const accessToken = useLocalStorage('accessToken', '')
    const refreshToken = useLocalStorage('refreshToken', '')
    const userLogin = useLocalStorage('user', '')
    const userRole = useLocalStorage('role', '')

    const setAccessToken = (accToken) => {
        accessToken.value = accToken;
    };

    const setRefreshToken = (refToken) => {
        refreshToken.value = refToken;
    };

    const setUserLogin = (user) => {
        userLogin.value = user
    }

    const setRole = (role) => {
        userRole.value = role
    }

    return {
        accessToken,
        refreshToken,
        userLogin,
        userRole,
        setAccessToken,
        setRefreshToken,
        setUserLogin,
        setRole
    }
  })