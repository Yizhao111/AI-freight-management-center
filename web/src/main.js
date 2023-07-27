import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/penint.scss' // css
import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'
import axios from 'axios'
Vue.prototype.$axios = axios  
import Tinymce from '@/components/tinymce/index.vue'
Vue.component('tinymce', Tinymce)

import quill from '@/components/Editor/index.vue'
Vue.component('quill', quill)

import './assets/icons' // icon
import './permission' // permission control

import {
  parseTime,
  resetForm,
  addDateRange,
  addDate,
  selectDictLabel,
  selectDictLabels,
  download,
  handleTree
} from "@/utils/penint";
import Pagination from "@/components/Pagination";
// 自定义表格工具扩展
import RightToolbar from "@/components/RightToolbar"
import modal from '@/utils/modal'

// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.addDate = addDate
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.$modal = modal
Vue.use(modal)

import VueClipBoard from 'vue-clipboard2'
Vue.use(VueClipBoard)

Vue.prototype.msgSuccess = function (msg) {
  this.$message({
    showClose: true,
    message: msg,
    type: "success"
  });
}

Vue.prototype.msgError = function (msg) {
  this.$message({
    showClose: true,
    message: msg,
    type: "error"
  });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.use(permission)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
