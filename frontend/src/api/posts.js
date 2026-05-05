import axios from 'axios'

const API_URL = '/api/posts'

const posts = {
  getPosts: async (params = {}) => {
    try {
      const response = await axios.get(`${API_URL}`, { params })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getPost: async (postId) => {
    try {
      const response = await axios.get(`${API_URL}/${postId}`)
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  createPost: async (postData) => {
    try {
      const response = await axios.post(`${API_URL}`, postData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  updatePost: async (postId, postData) => {
    try {
      const response = await axios.put(`${API_URL}/${postId}`, postData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  deletePost: async (postId) => {
    try {
      await axios.delete(`${API_URL}/${postId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return true
    } catch (error) {
      throw error.response.data
    }
  },

  getMyPosts: async (params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/my-posts`, { params }, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getPostComments: async (postId, params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/comments/post/${postId}`, { params })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  createComment: async (postId, commentData) => {
    try {
      const response = await axios.post(`${API_URL}/comments`, commentData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  updateComment: async (commentId, commentData) => {
    try {
      const response = await axios.put(`${API_URL}/comments/${commentId}`, commentData, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  deleteComment: async (commentId) => {
    try {
      await axios.delete(`${API_URL}/comments/${commentId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return true
    } catch (error) {
      throw error.response.data
    }
  },

  toggleLike: async (postId) => {
    try {
      const response = await axios.post(`${API_URL}/${postId}/like`, {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  purchasePost: async (postId) => {
    try {
      const response = await axios.post(`${API_URL}/${postId}/purchase`, {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMyPurchases: async () => {
    try {
      const response = await axios.get(`${API_URL}/my-purchases`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getPinnedPosts: async (params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/pinned`, { params })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getPopularPosts: async (params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/popular`, { params })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  getMostPurchasedPosts: async (params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/most-purchased`, { params })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  },

  searchPosts: async (keyword, params = {}) => {
    try {
      const response = await axios.get(`${API_URL}/search`, {
        params: { ...params, keyword }
      })
      return response.data
    } catch (error) {
      throw error.response.data
    }
  }
}

export default posts