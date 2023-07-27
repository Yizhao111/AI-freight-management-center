<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default  {
  name:  'App'
}
</script>
<style>
.previewImage{
  width: 90%!important;
  height: 90vh;
  background-color: rgba(0,0,0,.6)!important;
}
.el-upload-list__item {
  transition: none !important;
}
</style>
