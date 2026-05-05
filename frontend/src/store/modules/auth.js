import { login, register, getProfile, updateProfile } from '@/api/auth'

const state = {
  token: localStorage.getItem('token') || null,
  user: JSON.parse(localStorage.getItem('user')) || null,
  loading: false
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    if (token) {
      localStorage.setItem('token', token)
    } else {
      localStorage.removeItem('token')
    }
  },
  SET_USER(state, user) {
    state.user = user
    if (user) {
      localStorage.setItem('user', JSON.stringify(user))
    } else {
      localStorage.removeItem('user')
    }
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  CLEAR_AUTH(state) {
    state.token = null
    state.user = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
}

const actions = {
  async login({ commit }, credentials) {
    commit('SET_LOADING', true)
    try {
      const response = await login(credentials)
      const { token, userProfile } = response.data.data
      commit('SET_TOKEN', token)
      commit('SET_USER', userProfile)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async register({ commit }, userData) {
    commit('SET_LOADING', true)
    try {
      const response = await register(userData)
      const { token, userProfile } = response.data.data
      commit('SET_TOKEN', token)
      commit('SET_USER', userProfile)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getProfile({ commit }) {
    try {
      const response = await getProfile()
      commit('SET_USER', response.data.data)
      return response
    } catch (error) {
      throw error
    }
  },

  async updateProfile({ commit }, profileData) {
    try {
      const response = await updateProfile(profileData)
      commit('SET_USER', response.data.data)
      return response
    } catch (error) {
      throw error
    }
  },

  logout({ commit }) {
    commit('CLEAR_AUTH')
  },

  checkAuth({ commit, state }) {
    if (!state.token) {
      commit('CLEAR_AUTH')
      return false
    }
    return true
  }
}

const getters = {
  isAuthenticated: state => !!state.token,
  currentUser: state => state.user,
  userRoles: state => state.user?.roles || [],
  isAdmin: state => state.user?.roles?.includes('ADMIN') || false,
  isLoading: state => state.loading
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}