import {
  dailySignIn,
  getMyActivities,
  getMyActivitiesByType,
  getMyRecentActivities,
  getLearningCoinRanking,
  getMyLearningRanking,
  getSignDayRanking,
  getMySignRanking,
  getMyBalance,
  getMySignStatus
} from '@/api/activities'

const state = {
  activities: [],
  recentActivities: [],
  learningRanking: [],
  signRanking: [],
  balance: 0,
  signStatus: null,
  loading: false
}

const mutations = {
  SET_ACTIVITIES(state, activities) {
    state.activities = activities
  },
  SET_RECENT_ACTIVITIES(state, activities) {
    state.recentActivities = activities
  },
  SET_LEARNING_RANKING(state, ranking) {
    state.learningRanking = ranking
  },
  SET_SIGN_RANKING(state, ranking) {
    state.signRanking = ranking
  },
  SET_BALANCE(state, balance) {
    state.balance = balance
  },
  SET_SIGN_STATUS(state, status) {
    state.signStatus = status
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  ADD_ACTIVITY(state, activity) {
    state.activities.unshift(activity)
    state.recentActivities.unshift(activity)
    if (state.recentActivities.length > 10) {
      state.recentActivities = state.recentActivities.slice(0, 10)
    }
  }
}

const actions = {
  async dailySignIn({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await dailySignIn()
      commit('ADD_ACTIVITY', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyActivities({ commit }, params = {}) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyActivities(params)
      commit('SET_ACTIVITIES', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyActivitiesByType({ commit }, { type, params = {} }) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyActivitiesByType(type, params)
      commit('SET_ACTIVITIES', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyRecentActivities({ commit }, limit = 10) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyRecentActivities(limit)
      commit('SET_RECENT_ACTIVITIES', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getLearningCoinRanking({ commit }, topN = 50) {
    commit('SET_LOADING', true)
    try {
      const response = await getLearningCoinRanking(topN)
      commit('SET_LEARNING_RANKING', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyLearningRanking({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyLearningRanking()
      commit('SET_LEARNING_RANKING', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getSignDayRanking({ commit }, topN = 50) {
    commit('SET_LOADING', true)
    try {
      const response = await getSignDayRanking(topN)
      commit('SET_SIGN_RANKING', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMySignRanking({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getMySignRanking()
      commit('SET_SIGN_RANKING', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyBalance({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyBalance()
      commit('SET_BALANCE', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMySignStatus({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getMySignStatus()
      commit('SET_SIGN_STATUS', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

const getters = {
  activities: state => state.activities,
  recentActivities: state => state.recentActivities,
  learningRanking: state => state.learningRanking,
  signRanking: state => state.signRanking,
  balance: state => state.balance,
  signStatus: state => state.signStatus,
  loading: state => state.loading,
  canSignIn: state => state.signStatus?.canSignIn || false,
  hasSignedToday: state => state.signStatus?.hasSignedToday || false
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}