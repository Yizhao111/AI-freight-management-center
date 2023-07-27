<template>
  <div>
    <div class="block" v-for="(item, index) in list" :key="index">
      <div class="clicked" v-if="quInfo.theme == index" @click="checkout(index)">
        <span class="curspan">使用中</span>
        <el-image :src="item.url" />
      </div>
      <div  v-else @click="checkout(index)">
        <el-image :src="item.url"></el-image>
      </div>
    </div>
<div style="text-align:center;margin-top:20px">
    <el-button type="success" @click="setting">确认使用该主题</el-button>
</div>
    
  </div>
</template>

<script>
import {updateTheme} from '@/api/ques/exam'
export default {
  props: ["quInfo"],

  data() {
    return {
      list: [
        { id: 0, url: "https://s1.ax1x.com/2022/11/13/zFx2wt.png" },
        { id: 1, url: "https://s1.ax1x.com/2022/11/13/zFxgeI.png" },
        { id: 2, url: "https://s1.ax1x.com/2022/11/13/zFxBWD.png" },
        { id: 3, url: "https://s1.ax1x.com/2022/11/13/zFxsQH.png" },
        { id: 4, url: "https://s1.ax1x.com/2022/11/13/zFxrSe.png" },
        { id: 5, url: "https://s1.ax1x.com/2022/11/13/zFxyyd.png" },
        { id: 6, url: "https://s1.ax1x.com/2022/11/13/zFx6OA.png" },
      ],
    };
  },
  mounted(){

  },
  methods: {
    checkout(index){
        this.quInfo.theme = index
        this.$EventBus.$emit('theme', index)

    },

    setting(){
        var data = {
            id: this.quInfo.id,
            theme: this.quInfo.theme
        }
        updateTheme(data).then(res => {
            if (res.status == 200) {
                this.msgSuccess("设置主题成功")
            }
        })
    }
  },
};
</script>

<style scoped>
.img {
  height: 20px;
  width: 20px;
}
.clicked {
  box-shadow: 0 0 0px 2px #0095ff;
  margin-bottom: 10px;
}
.curspan {
  display: block;
  position: inherit;
  /* left: 0; */
  top: 0;
  border-radius: 0 0px 2px 0px;
  background: rgba(0, 0, 0, 0.3);
  font-size: 12px;
  font-weight: 400;
  color: #ffffff;
  padding: 1px 5px;
  line-height: 18px;
}
</style>