import axios from 'axios'

const API_URL = '/api/users'

const user = {
  getProfile: async (userId) => {
    try {
      const response = await axios.get(`${API_URL}/${userId}`)
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  updateProfile: async (userId, profileData) => {
    try {
      const response = await axios.put(`${API_URL}/${userId}`, profileData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getUserBalance: async (userId) => {
    try {
      const response = await axios.get(`${API_URL}/${userId}/learning-coins`)
      return response.data
    } catch (error) {
      throw error.response.data
    }
  }
}

export default user