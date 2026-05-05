import { getProfile, updateProfile, getUserBalance } from '@/api/user'

const state = {
  profile: null,
  balance: 0,
  loading: false
}

const mutations = {
  SET_PROFILE(state, profile) {
    state.profile = profile
  },
  SET_BALANCE(state, balance) {
    state.balance = balance
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  }
}

const actions = {
  async getProfile({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getProfile()
      commit('SET_PROFILE', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async updateProfile({ commit }, profileData) {
    commit('SET_LOADING', true)
    try {
      const response = await updateProfile(profileData)
      commit('SET_PROFILE', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getUserBalance({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getUserBalance()
      commit('SET_BALANCE', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

const getters = {
  profile: state => state.profile,
  balance: state => state.balance,
  loading: state => state.loading
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}