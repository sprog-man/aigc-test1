import axios from 'axios'

const API_URL = '/api/auth'

const auth = {
  login: async (credentials) => {
    try {
      const response = await axios.post(`${API_URL}/login`, credentials)
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  register: async (userData) => {
    try {
      const response = await axios.post(`${API_URL}/register`, userData)
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getProfile: async () => {
    try {
      const response = await axios.get(`${API_URL}/me`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  updateProfile: async (profileData) => {
    try {
      const response = await axios.put(`${API_URL}/profile`, profileData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  changePassword: async (passwordData) => {
    try {
      const response = await axios.put(`${API_URL}/change-password`, passwordData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  checkUsernameEmail: async (data) => {
    try {
      const response = await axios.post(`${API_URL}/check-username-email`, data)
      return response.data
    } catch (error) {
      throw error.response.data
    }
  }
}

export default auth