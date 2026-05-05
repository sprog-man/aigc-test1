import axios from 'axios'

const API_URL = '/api/activities'

const activities = {
  dailySignIn: async () => {
    try {
      const response = await axios.post(`${API_URL}/daily-sign-in`, {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMyActivities: async (params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/my-activities`, { params }, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMyActivitiesByType: async (type, params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/my-activities/${type}`, { params }, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMyRecentActivities: async (limit = 10) => {
    try {
      const response = await axios.get(`${API_URL}/my-recent-activities`, {
        params: { limit },
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getLearningCoinRanking: async (topN = 50) => {
    try {
      const response = await axios.get(`${API_URL}/learning-coin-ranking`, {
        params: { topN }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMyLearningRanking: async () => {
    try {
      const response = await axios.get(`${API_URL}/my-learning-rank`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getSignDayRanking: async (topN = 50) => {
    try {
      const response = await axios.get(`${API_URL}/sign-day-ranking`, {
        params: { topN }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMySignRanking: async () => {
    try {
      const response = await axios.get(`${API_URL}/my-sign-rank`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMyBalance: async () => {
    try {
      const response = await axios.get(`${API_URL}/my-balance`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMySignStatus: async () => {
    try {
      const response = await axios.get(`${API_URL}/my-sign-status`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  }
}

export default activities