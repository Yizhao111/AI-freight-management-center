<template>
  <div class="main">
    <div id="headsplitdiv"></div>
    <div class="form">
      <div id="toptitle">
        <h1 class="htitle" id="htitle">{{ quInfo.title }}</h1>
      </div>

      <div class="bt">
         <el-button type="success" icon="el-icon-pie-chart" @click="change(2)"
          >饼图</el-button
        >
        <el-button type="primary" icon="el-icon-document" @click="change(1)"
          >列表</el-button
        >
       
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
            <span class="qtypetip" v-else>【判断题】</span>
          </div>

          <div class="gauge" :id="'myChart' + index" v-show="type == 2"></div>

          <!-- {{ item }} -->
          <el-table
            :data="item.answerList"
            border
            style="width: 100%"
            v-show="type == 1"
          >
            <el-table-column
              prop="content"
              label="选项"
              width="180"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="checkNum"
              label="小计"
              width="180"
              align="center"
            >
            </el-table-column>
            <el-table-column prop="address" label="比例" align="center">
              <template slot-scope="scope">
                <el-progress
                  :percentage="
                    (scope.row.checkNum / scope.row.all).toFixed(2) * 100
                      ? (scope.row.checkNum / scope.row.all).toFixed(2) * 100
                      : 0
                  "
                ></el-progress>
              </template>
            </el-table-column>
          </el-table>

          <div
            class="errorMessage"
            style="left: 0px; display: block"
            v-if="show[index]"
          >
            <el-alert title="请选择选项" type="error" show-icon> </el-alert>
          </div>
        </div>
      </div>
      <!-- <div class="footer">
        <el-button type="success" style="width: 120px">提交</el-button>
      </div> -->
    </div>
    <div class="footerw"></div>
  </div>
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import { previewInfo, examAnswerStat } from "@/api/ques/exam";

export default {
  data() {
    return {
      show: [],
      examId: undefined,
      quInfo: {},
      checkForm: {},
      type: 2,
    };
  },
  created() {
    this.examId = this.$route.query.id;
    if (this.examId) {
      this.getInfo();
    }
  },

  mounted() {},
  methods: {
    change(type) {
    //   if (type == 2) {
    //     if (this.quInfo) {
    //         console.log(1);
    //       for (let index = 0; index < this.quInfo.selectList.length; index++) {
    //         const element = this.quInfo.selectList[index];
    //         this.$nextTick().then(() => {
    //           // 也支持promise
    //           // 这个函数中DOM必定渲染完成
    //           let myChart = echarts.init(
    //             document.getElementById("myChart" + index),
    //             "macarons"
    //           ); /* 动态绑定容器元素 */
    //           myChart.setOption(this.setData(element.answerList, element.stat));
    //         });
    //       }
    //     }
    //   }
      this.type = type;
        this.$forceUpdate()
    },
    async getInfo() {
      let res = await previewInfo(this.examId);
      this.quInfo = res.data;
      if (this.quInfo) {
        for (let index = 0; index < this.quInfo.selectList.length; index++) {
          const element = this.quInfo.selectList[index];
          this.$nextTick().then(() => {
            // 也支持promise
            // 这个函数中DOM必定渲染完成
            let myChart = echarts.init(
              document.getElementById("myChart" + index),
              "macarons"
            ); /* 动态绑定容器元素 */
            myChart.setOption(this.setData(element.answerList, element.stat));
          });
        }
      }
    },

    setData(list, stat) {
      var data = [];
      list.forEach((element) => {
        var a = {
          value: element.checkNum,
          name: element.content,
        };
        data.push(a);
      });
      return {
        // title: {
        //   subtext: "统计",
        //   left: "center",
        // },
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "vertical",
          left: "left",
        },
        series: [
          {
            name: "统计",
            type: "pie",
            radius: "50%",
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };
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
  /* background-image: url(//image.wjx.cn/images/theme/defaultpcbg.png); */
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
.gauge {
  height: 240px;
}
.bt {
  padding: 0 30px 0px;
}
</style>