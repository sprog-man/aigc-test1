import {
  getPosts,
  getPost,
  createPost,
  updatePost,
  deletePost,
  getMyPosts,
  getPostComments,
  createComment,
  updateComment,
  deleteComment,
  toggleLike,
  purchasePost,
  getMyPurchases,
  getPinnedPosts,
  getPopularPosts,
  getMostPurchasedPosts,
  searchPosts
} from '@/api/posts'

const state = {
  posts: [],
  currentPost: null,
  myPosts: [],
  myPurchases: [],
  comments: [],
  totalPosts: 0,
  loading: false
}

const mutations = {
  SET_POSTS(state, { posts, total }) {
    state.posts = posts
    state.totalPosts = total
  },
  SET_CURRENT_POST(state, post) {
    state.currentPost = post
  },
  SET_MY_POSTS(state, posts) {
    state.myPosts = posts
  },
  SET_MY_PURCHASES(state, posts) {
    state.myPurchases = posts
  },
  SET_COMMENTS(state, comments) {
    state.comments = comments
  },
  ADD_POST(state, post) {
    state.posts.unshift(post)
  },
  UPDATE_POST(state, updatedPost) {
    const index = state.posts.findIndex(p => p.id === updatedPost.id)
    if (index !== -1) {
      state.posts.splice(index, 1, updatedPost)
    }
    if (state.currentPost && state.currentPost.id === updatedPost.id) {
      state.currentPost = updatedPost
    }
  },
  DELETE_POST(state, postId) {
    state.posts = state.posts.filter(p => p.id !== postId)
    state.myPosts = state.myPosts.filter(p => p.id !== postId)
    if (state.currentPost && state.currentPost.id === postId) {
      state.currentPost = null
    }
  },
  ADD_COMMENT(state, comment) {
    state.comments.unshift(comment)
  },
  UPDATE_COMMENT(state, updatedComment) {
    const index = state.comments.findIndex(c => c.id === updatedComment.id)
    if (index !== -1) {
      state.comments.splice(index, 1, updatedComment)
    }
  },
  DELETE_COMMENT(state, commentId) {
    state.comments = state.comments.filter(c => c.id !== commentId)
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  }
}

const actions = {
  async getPosts({ commit }, params = {}) {
    commit('SET_LOADING', true)
    try {
      const response = await getPosts(params)
      commit('SET_POSTS', {
        posts: response.data.data.data,
        total: response.data.data.total
      })
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getPost({ commit }, postId) {
    commit('SET_LOADING', true)
    try {
      const response = await getPost(postId)
      commit('SET_CURRENT_POST', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async createPost({ commit }, postData) {
    commit('SET_LOADING', true)
    try {
      const response = await createPost(postData)
      commit('ADD_POST', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async updatePost({ commit }, { postId, postData }) {
    commit('SET_LOADING', true)
    try {
      const response = await updatePost(postId, postData)
      commit('UPDATE_POST', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async deletePost({ commit }, postId) {
    commit('SET_LOADING', true)
    try {
      await deletePost(postId)
      commit('DELETE_POST', postId)
      return true
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyPosts({ commit }, params = {}) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyPosts(params)
      commit('SET_MY_POSTS', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMyPurchases({ commit }) {
    commit('SET_LOADING', true)
    try {
      const response = await getMyPurchases()
      commit('SET_MY_PURCHASES', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getPostComments({ commit }, { postId, params = {} }) {
    commit('SET_LOADING', true)
    try {
      const response = await getPostComments(postId, params)
      commit('SET_COMMENTS', response.data.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async createComment({ commit }, { postId, commentData }) {
    commit('SET_LOADING', true)
    try {
      const response = await createComment(postId, commentData)
      commit('ADD_COMMENT', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async updateComment({ commit }, { commentId, commentData }) {
    commit('SET_LOADING', true)
    try {
      const response = await updateComment(commentId, commentData)
      commit('UPDATE_COMMENT', response.data.data)
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async deleteComment({ commit }, commentId) {
    commit('SET_LOADING', true)
    try {
      await deleteComment(commentId)
      commit('DELETE_COMMENT', commentId)
      return true
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async toggleLike({ commit }, { postId }) {
    commit('SET_LOADING', true)
    try {
      const response = await toggleLike(postId)
      if (state.currentPost && state.currentPost.id === postId) {
        commit('SET_CURRENT_POST', response.data.data)
      }
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async purchasePost({ commit }, { postId }) {
    commit('SET_LOADING', true)
    try {
      const response = await purchasePost(postId)
      if (state.currentPost && state.currentPost.id === postId) {
        commit('SET_CURRENT_POST', response.data.data)
      }
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getPinnedPosts({ commit }, params = {}) {
    commit('SET_LOADING', true)
    try {
      const response = await getPinnedPosts(params)
      commit('SET_POSTS', {
        posts: response.data.data.data,
        total: response.data.data.total
      })
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getPopularPosts({ commit }, params = {}) {
    commit('SET_LOADING', true)
    try {
      const response = await getPopularPosts(params)
      commit('SET_POSTS', {
        posts: response.data.data.data,
        total: response.data.data.total
      })
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async getMostPurchasedPosts({ commit }, params = {}) {
    commit('SET_LOADING', true)
    try {
      const response = await getMostPurchasedPosts(params)
      commit('SET_POSTS', {
        posts: response.data.data.data,
        total: response.data.data.total
      })
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  },

  async searchPosts({ commit }, { keyword, params = {} }) {
    commit('SET_LOADING', true)
    try {
      const response = await searchPosts(keyword, params)
      commit('SET_POSTS', {
        posts: response.data.data.data,
        total: response.data.data.total
      })
      return response
    } catch (error) {
      throw error
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

const getters = {
  posts: state => state.posts,
  currentPost: state => state.currentPost,
  myPosts: state => state.myPosts,
  myPurchases: state => state.myPurchases,
  comments: state => state.comments,
  totalPosts: state => state.totalPosts,
  loading: state => state.loading
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}