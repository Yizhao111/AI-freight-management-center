<template>
  <div class="main">
    <el-row :gutter="20">
      <el-col :span="4">
       <Left  v-if="show" :quInfo="quInfo"/>
        </el-col>
      <el-col :span="20">
        <Main :examId="examId" :quInfo="quInfo" v-if="show"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { previewInfo } from "@/api/ques/exam";

import Main from "./main";
import Left from "./left";
export default {
  components: {
    Main,
    Left
  },
  data() {
    return {
      examId: undefined,
      show: false,
      quInfo: {}
    };
  },
  created(){
     this.examId = this.$route.query.id;
     if (this.examId) {
        this.getInfo()
        this.show = true
     }
  },
  methods:{
    async getInfo() {
      let res = await previewInfo(this.examId);
      this.quInfo = res.data;
      this.quInfo.selectList.forEach((element) => {
        element.answerList.forEach((e) => {
          e.checked = undefined;
        });
      });
    },
  }
};
</script>

<style scoped>
.main{
    width: 90%;
    margin: 20px auto;
}
</style>