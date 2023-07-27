import variables from '@/assets/styles/element-variables.scss'
import defaultSettings from '@/settings'

const { sideTheme, showSettings, tagsView, fixedHeader, sidebarLogo,topNav} = defaultSettings

const state = {
  theme: variables.theme,
  sideTheme: sideTheme,
  showSettings: showSettings,
  tagsView: tagsView,
  topNav: topNav,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo

  // theme: '',
  // sideTheme: 'sideTheme',
  // showSettings: false,
  // tagsView: true,
  // topNav: true,
  // fixedHeader: false,
  // sidebarLogo: true
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

