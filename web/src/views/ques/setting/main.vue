<template>
  <div class=""  v-if="quInfo" :style="imageList[quInfo.theme].imageUrl">
    <div id="headsplitdiv"></div>
    <div class="form">
      <div id="toptitle">
        <h1 class="htitle" id="htitle">{{ quInfo.title }}</h1>
      </div>
      <div
        id="divContent"
        class="divContent"
        v-for="(item, index) in quInfo.selectList"
        :key="index"
      >
        <div
          class="field ui-field-contain"
          id="div1"
          req="1"
          topic="1"
          data-role="fieldcontain"
          type="3"
          :class="{ show: show[index] }"
        >
          <div class="field-label">
            {{ index + 1 }}. {{ item.content }}
            <span class="qtypetip" v-if="item.quType == 1">【单选题】</span>
            <span class="qtypetip" v-else-if="item.quType == 2"
              >【多选题】</span
            >
            <span class="qtypetip" v-else>【判断题】</span
            ><span class="req">*</span>
          </div>
          <div v-for="(itemx, indexx) in item.answerList" :key="indexx">
            <div class="ui-controlgroup column1" v-if="item.quType == 2">
              <!-- <el-checkbox :label="itemx.content" class="radioDiv" v-model="itemx.checked" @change="checkBoxCheck($event,itemx)"></el-checkbox><-->
              <el-checkbox
                :label="itemx.content"
                class="radioDiv"
                v-model="itemx.checked"
                @change="changeBox($event, itemx, index, indexx)"
              ></el-checkbox>
            </div>
            <div class="ui-controlgroup column1" v-else>
              <el-radio-group
                v-model="checkForm[index]"
                class="radioDiv"
                @change="checkRadio($event, itemx, index, indexx)"
              >
                <el-radio :label="itemx.id">{{ itemx.content }}</el-radio>
              </el-radio-group>
            </div>
          </div>

          <div
            class="errorMessage"
            style="left: 0px; display: block"
            v-if="show[index]"
          >
            <el-alert title="请选择选项" type="error" show-icon> </el-alert>
          </div>
        </div>
      </div>
      <div class="footer">
        <el-button type="success" style="width: 120px" @click="submitForm"
          >提交</el-button
        >
      </div>
    </div>
    <div class="footerw"></div>
  </div>
</template>

<script>
export default {
  name: "main",
  props: ["examId", "quInfo"],
  data() {
    return {
      show: [],
      isUrl: false,
      checkForm: {},
      imageList: [
        {
          id: 0,
          imageUrl: {
            backgroundColor: "#eaf2f7",
            backgroundImage: "url(//image.wjx.cn/images/theme/defaultpcbg.png)",
            backgroundSize: "800px 80px",
            backgroundRepeat: "no-repeat",
            backgroundPosition: "top center",
          },
        },
        {
          id: 1,
          url: "https://s1.ax1x.com/2022/11/13/zkPuw9.png",
          imageUrl: {
            backgroundImage: "url('https://s1.ax1x.com/2022/11/13/zkPuw9.png')",
            // margin: 0,
            // backgroundSize:"100% 100%",
            backgroundAttachment:"fixed"
          },
        },
        {
          id: 2,
          url: "https://s1.ax1x.com/2022/11/13/zkPAzT.png",
          imageUrl: {
            backgroundImage: "url('https://s1.ax1x.com/2022/11/13/zkPAzT.png')",
            // margin: 0,
            // backgroundSize:"100% 100%",
            backgroundAttachment:"fixed"
          },
        },
        {
          id: 3,
          url: "https://s1.ax1x.com/2022/11/13/zkPVQU.png",
          imageUrl: {
            backgroundImage: "url('https://s1.ax1x.com/2022/11/13/zkPVQU.png')",
            // margin: 0,
            // backgroundSize:"100% 100%",
            backgroundAttachment:"fixed"
          },
        },
        {
          id: 4,
          url: "https://s1.ax1x.com/2022/11/13/zkPneJ.png",
          imageUrl: {
            backgroundImage: "url('https://s1.ax1x.com/2022/11/13/zkPneJ.png')",
            margin: 0,
            backgroundSize:"100% 100%",
            backgroundAttachment:"fixed"
          },
        },
        {
          id: 5,
          url: "https://s1.ax1x.com/2022/11/13/zkPZyF.png",
          imageUrl: {
            backgroundImage: "url('https://s1.ax1x.com/2022/11/13/zkPZyF.png')",
            // margin: 0,
            // backgroundSize:"100% 100%",
            backgroundAttachment:"fixed"
          },
        },
        {
          id: 6,
          url: "https://s1.ax1x.com/2022/11/13/zkPeL4.png",
          imageUrl: {
            backgroundImage: "url('https://s1.ax1x.com/2022/11/13/zkPeL4.png')",
            // margin: 0,
            // backgroundSize:"100% 100%",
            backgroundAttachment:"fixed"
          },
        },
      ],
    };
  },
  created() {},
  mounted() {
    this.$EventBus.$on("theme", (e) => {
      //需要执行的代码
      this.quInfo.theme = e;
    });
  },
  methods: {
    submitForm() {
      this.show = [];
      let quList = this.quInfo.selectList;
      for (let index = 0; index < quList.length; index++) {
        const e = quList[index];
        var flag = false;
        if (e.quType == 2) {
          var flag2 = false;
          for (let index = 0; index < e.answerList.length; index++) {
            const element = e.answerList[index];
            if (element.checked == undefined || element.checked == false) {
              flag2 = true;
            } else {
              flag2 = false;
            }
            if (flag2 == false) {
              break;
            }
          }
          if (flag2) {
            flag = true;
          }
        } else {
          e.answerList.forEach((element) => {
            if (element.checked == undefined) {
              flag = true;
            }
          });
        }

        if (flag) {
          this.show.push(true);
        } else {
          this.show.push(false);
        }
      }
      this.toCommit();
    },
    toCommit() {
      var flag = false;
      this.show.forEach((e) => {
        if (e) {
          flag = true;
        }
      });
      if (flag) {
        this.msgError("请完成您的问卷再操作");
        return;
      }
      this.$confirm("此问卷为预览状态，不能提交", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {})
        .catch(() => {});
    },
    // async getInfo() {
    //   let res = await previewInfo(this.examId);
    //   this.quInfo = res.data;
    //   this.quInfo.selectList.forEach((element) => {
    //     element.answerList.forEach((e) => {
    //       e.checked = undefined;
    //     });
    //   });
    // },
    checkRadio(e, item, index, indexx) {
      this.quInfo.selectList[index].answerList.forEach((ele) => {
        if (ele.id === e) {
          ele.checked = true;
        } else {
          ele.checked = false;
        }
      });
      this.show[index] = false;
      this.$forceUpdate();
    },
    changeBox(e, item, index, indexx) {
      if (e) {
        this.show[index] = false;
      }
      this.$forceUpdate();
    },
  },
};
</script>

<style scoped>
body {
  background-color: #eaf2f7 !important;
}
#headsplitdiv {
  height: 80px !important;
}
#toptitle {
  padding: 50px 130px 20px;
  width: 100%;
  color: #0095ff;
}
#toptitle .htitle {
  font-size: 24px;
  line-height: 40px;
  font-weight: 600;
  margin: 0;
  padding: 0;
  text-align: center;
  -webkit-font-smoothing: antialiased;
}
#divContent {
  background-color: #fff;
  padding: 0 30px 0px;
}
.main {
  background-color: #eaf2f7;
  background-image: url(//image.wjx.cn/images/theme/defaultpcbg.png);
  background-size: 800px 80px;
  background-repeat: no-repeat;
  background-position: top center;
  /* height: 100%; */
}
.form {
  max-width: 800px;
  /* height: 200px; */
  background: #fff;
  margin: 0 auto !important;
}

.field {
  margin: 0 0 10px 0;
  clear: both;
  padding: 20px;
}
.field-label {
  font-size: 17px !important;
  line-height: 26px;
  font-weight: 500;
  -webkit-font-smoothing: antialiased;
  position: relative;
  padding: 0 0 10px 0 !important;
  display: block;
  word-wrap: break-word;
  color: #262626;
}

.ui-controlgroup {
  border: none;
  margin: 0;
  padding: 0;
}

.errorMessage {
  margin-top: 10px;
  transition: all 0.5s ease 0s;
}
.radioDiv {
  width: auto;
  text-align: left;
  display: table;
  margin-left: 10px;
  cursor: pointer;
  font-size: 16px;
  color: #595959;
  text-align: left;
  padding: 4px 0px 4px 0px;
}
.qtypetip {
  font-weight: normal;
  color: gray;
}
span.req {
  position: absolute;
  top: 0;
  left: -12px;
  color: #ff4040 !important;
  margin: 2px 0 0 2px;
}
.show {
  border: 1px dashed rgb(255, 64, 64);
}
.footer {
  text-align: center;
  margin: 20px;
  padding-bottom: 40px;
  background-color: #fff;
}
.footerw {
  height: 20px;
}
</style>