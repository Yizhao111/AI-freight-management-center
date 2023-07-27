import { login, logout, getInfo, success,register } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import db from '@/utils/localstorage'



const user = {
  state: {
    token: getToken(),
    routeToken: db.get('ROUTE_TOKEN', null),
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    lastLoginTime: ''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_LASTLOGINTIME: (state, lastLoginTime) => {
      state.lastLoginTime = lastLoginTime
    },
    SET_ROUTET_TOEKN(state, routeToken) {
      db.save('ROUTE_TOKEN', routeToken)
      state.routeToken = routeToken
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const key = userInfo.key
      const grant_type = "password"
      return new Promise((resolve, reject) => {
        login(username, password, code, key, grant_type).then(res => {
          success()
          setToken(res.access_token)
          commit('SET_TOKEN', res.access_token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    Register({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const nickName = userInfo.nickName
      const roleId = userInfo.roleId
      const email = userInfo.email
      const mobile = userInfo.mobile
      return new Promise((resolve, reject) => {
        var data = {
          username,
          password,
          nickName,
          roleId,
          email,
          mobile
        }
        register(data).then(res => {
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    SocialLogin({ commit }, data) {
      return new Promise((resolve, reject) => {
        success()
        setToken(data.data.access_token)
        commit('SET_TOKEN', data.data.access_token)
        resolve()
      }).catch(error => {
        reject(error)

      })
    },
    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          const user = res.data.user
          const avatar = user.avatar == "" ? require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API +"/system/oss"+ user.avatar;
          if (res.data.roles && res.data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.data.roles)
            commit('SET_PERMISSIONS', res.data.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', user.username)
          commit('SET_AVATAR', avatar)
          commit('SET_LASTLOGINTIME', user.lastLoginTime)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
