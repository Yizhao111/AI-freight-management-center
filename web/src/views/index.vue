<template>
  <div class="main">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="12">
        <div class="app-container" style="height: 120px; background-color: #FFF ">
          <div class="user-wrapper">
            <div class="user-header">
              <img :src="avatar" class="avatar" />
            </div>
            <div class="user-info">
              <div class="random-message">{{ welcomeMessage }}</div>
              <div class="user-dept">
                <span>{{ user.roles[0] ? user.roles[0] : '暂无角色' }}</span>
              </div>
              <div class="user-login-info">
                {{ '最后登录时间' }}：
                <span id="last-login-time">{{ user.lastLoginTime ? user.lastLoginTime : '第一次登录系统' }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="4">
        <div class="app-container" style="height: 120px;padding: 0">
          <el-card :body-style="{ padding: '0px' }" shadow="never" style="height: 120px">
            <div class="count-header">
              <svg-icon icon-class="count1" class="indexIcon" />
              <span class="des">{{ '订单金额' }}</span>
              <countTo class="countTo todayIp" :start-val="0" :end-val="todayIp" :duration="3000" />
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :xs="24" :sm="4">
        <div class="app-container" style="height: 120px;padding: 0">
          <el-card :body-style="{ padding: '0px' }" shadow="never" style="height: 120px">
            <div class="count-header">
              <svg-icon icon-class="count2" class="indexIcon" />
              <span class="des">{{ '订单数' }}</span>
              <countTo class="countTo todayVisit" :start-val="0" :end-val="todayVisit" :duration="3000" />
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :xs="24" :sm="4">
        <div class="app-container" style="height: 120px;padding: 0">
          <el-card :body-style="{ padding: '0px' }" shadow="never" style="height: 120px">
            <div class="count-header">
              <svg-icon icon-class="count3" class="indexIcon" />
              <span class="des">{{ '商品数' }}</span>
              <countTo class="countTo totalVisit" :start-val="0" :end-val="totalVisit" :duration="3000" />
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24">
        <div class="app-container" style="background-color: #FFF">
          <div id="visit-count-chart" style="width: 100%;height: 370px" />
        </div>
      </el-col>
      <!-- <el-col :xs="24" :sm="12">
        <div class="app-container">
          <el-table
            :data="server"
            border
            class="server-table"
            style="width: 100%"
          >
            <el-table-column
              prop="name"
              label="服务名"
            />
            <el-table-column
              prop="port"
              label="端口"
            >
              <template slot-scope="scope">
                <el-tag
                  :type="scope.row.id % 2 === 0 ? 'primary' : 'success'"
                  disable-transitions
                >
                  {{ scope.row.port }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="description"
              label="描述"
            />
          </el-table>
        </div>
      </el-col>-->
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import echarts from "echarts";
import { parseTime } from "@/utils/penint";
import countTo from "vue-count-to";
import resize from "@/components/Charts/mixins/resize";
import { getIndexData, getIndexData1 } from "@/api/common";

export default {
  name: "Dashboard",
  components: { countTo },
  filters: {
    portFilter(v) {
      const map = {
        0: "danger",
        1: "success"
      };
      return map[status];
    }
  },
  mixins: [resize],
  data() {
    return {
      // count1: require('@/assets/icons/svg/count1.svg'),
      welcomeMessage: "",
      todayIp: 0,
      todayVisit: 0,
      totalVisit: 0,
      chart: null
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "device"]),
    user() {
      return this.$store.state.user;
    }
  },
  mounted() {
    this.welcomeMessage = this.welcome();
    this.initIndexData();
    this._getIndexData1()
  },
  methods: {
    _getIndexData1(){
      getIndexData1().then(res => {
        const data = res.data
        this.todayIp = data.orderPrice;
        this.totalVisit = data.orderCount;
        this.todayVisit = data.goodsCount;
      })
    },
    resolveIcon(icon) {
      return require("@/assets/icons/svg/" + `${icon}`)
    },
    welcome() {
      const date = new Date();
      const hour = date.getHours();
      const time =
        hour < 6
          ? "早上好"
          : hour <= 11
            ? "早上好"
            : hour <= 13
              ? "下午好"
              : hour <= 18
                ? "下午好"
                : "晚上好";
      const welcomeArr = [
        "喝杯咖啡休息下吧☕",
        "要不要和朋友打局LOL",
        "今天又写了几个Bug🐞呢",
        "今天在群里吹水了吗",
        "今天吃了什么好吃的呢",
        "今天您微笑了吗😊",
        "今天帮别人解决问题了吗",
        "准备吃些什么呢",
        "周末要不要去看电影？"
      ];
      // const index = Math.floor(Math.random() * welcomeArr.length);
      // return `${time}, ${this.user.name}, ${welcomeArr[index]}`;
      return `${time}, ${this.user.name}`;
    },
    initIndexData: function () {
      getIndexData().then(r => {
        const data = r.data;
       
        const tenVisitCount = [];
        const dateArr = [];
        for (let i = 10; i >= 0; i--) {
          const time = parseTime(
            new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * i),
            "{m}-{d}"
          );
          let contain = false;
          for (const o of data) {
            if (o.days === time) {
              contain = true;
              tenVisitCount.push(o.count);
            }
          }
          if (!contain) {
            tenVisitCount.push(0);
          }
          dateArr.push(time);
        }
        const tenUserVisitCount = [];
        for (let i = 10; i >= 0; i--) {
          const time = parseTime(
            new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * i),
            "{m}-{d}"
          );
          let contain = false;
          for (const o of data) {
            if (o.days === time) {
              contain = true;
              tenUserVisitCount.push(o.count);
            }
          }
          if (!contain) {
            tenUserVisitCount.push(0);
          }
        }
        this.chart = echarts.init(document.getElementById("visit-count-chart"));
        const option = {
          backgroundColor: "#FFF",
          title: {
            text: "近十天客户新增记录" + "\n",
            textStyle: {
              fontSize: 14
            }
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow"
            }
          },
          legend: {
            data: ["总数"],
            top: "18"
          },
          grid: {
            left: "3%",
            right: "5%",
            bottom: "3%",
            containLabel: true,
            show: false
          },
          toolbox: {
            feature: {
              dataView: { show: false, readOnly: false }
            }
          },
          xAxis: {
            type: "category",
            boundaryGap: true,
            splitLine: {
              show: false
            },
            data: dateArr,
            axisLine: {
              lineStyle: {
                color: "#333"
              }
            }
          },
          yAxis: [
            {
              type: "value",
              splitLine: {
                lineStyle: {
                  type: "dashed",
                  color: "#DDD"
                }
              },
              axisLine: {
                show: false,
                lineStyle: {
                  color: "#333"
                }
              },
              nameTextStyle: {
                color: "#999"
              },
              splitArea: {
                show: false
              }
            }
          ],
          series: [
            {
              name: "总数",
              smooth: true,
              type: "line",
              color: "rgb(0, 143, 251)",
              areaStyle: {
                color: {
                  type: "linear",
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: "rgba(0, 143, 251, 0.8)"
                    },
                    {
                      offset: 1,
                      color: "#fff"
                    }
                  ],
                  globalCoord: false
                }
              },
              lineStyle: {
                normal: {
                  width: 3
                }
              },
              data: tenUserVisitCount
            },
          ]
        }
        this.chart.setOption(option);
      });
    }
  }
}
</script>
<style lang="scss">
.indexIcon {
  font-size: 60px;
  vertical-align: -0.4em !important;
}

.main {
  .el-card {
    border: none;
    border-radius: 2px;
  }

  .el-table.server-table {
    th {
      height: 2.7rem;
      padding: 7px 0;
      border-right: none;
    }

    td {
      padding: 7px 0;
      border-right: none;
    }
  }

  .count-header {
    padding-left: 1rem;
    height: 120px;
    line-height: 120px;
    text-align: left;
    vertical-align: center;

    img {
      width: 3.8rem;
      margin-top: 1.8rem;
      vertical-align: top;
    }

    span {
      font-size: 15px;
      color: #606266;
      font-weight: 600;

      &.des {
        margin-left: 9px;
      }
    }
  }

  .todayIp {
    color: #27c6da !important;
  }

  .todayVisit {
    color: #fc573b !important;
  }

  .totalVisit {
    color: #be63f9 !important;
  }
}
</style>
<style lang="scss" scoped>
.main {
  padding: 10px;

  .app-container {
    margin: 0 0 10px 0;
  }

  .user-container {
    padding: 15px;
  }

  .user-wrapper {
    width: 100%;
    display: inline-block;

    .user-header {
      display: inline-block;
      vertical-align: middle;

      img {
        width: 5rem;
        margin: 0.5rem 1rem;
        border-radius: 50%;
      }
    }

    .user-info {
      display: inline-block;
      vertical-align: middle;

      .random-message {
        font-size: 1rem;
        margin-bottom: 0.5rem;
        color: #333;
      }

      .user-dept,
      .user-login-info {
        color: rgba(0, 0, 0, 0.45);
        margin-bottom: 0.5rem;
        font-size: 0.9rem;
        line-height: 1.1rem;
      }
    }
  }

  .user-visits {
    text-align: center;
    padding-right: 2rem;
    margin-top: 1rem;
    vertical-align: middle;

    .num {
      font-weight: 600;
    }
  }

  .project-wrapper {
    padding: 0;

    .project-header {
      padding: 18px;
      margin-bottom: 16px;
    }

    table {
      width: 100%;
      border-collapse: collapse;

      td {
        width: 50%;
        border-top: 1px solid #f1f1f1;
        border-bottom: 1px solid #f1f1f1;
        padding: 0.7rem;

        .project-avatar-wrapper {
          display: inline-block;
          float: left;
          margin-right: 0.7rem;

          .project-avatar {
            color: #42b983;
            background-color: #d6f8b8;
          }
        }

        &:nth-child(odd) {
          border-right: 1px solid #f1f1f1;
        }
      }
    }

    .project-detail {
      display: inline-block;
      float: left;
      text-align: left;
      width: 75%;

      .project-name {
        font-size: 0.9rem;
        margin-top: -2px;
        font-weight: 600;
      }

      .project-desc {
        color: rgba(0, 0, 0, 0.45);

        p {
          margin: 5px 0 0 0;
          font-size: 0.85rem;
          line-height: 1.4rem;
          white-space: normal;
        }
      }
    }
  }

  @media screen and (max-width: 1366px) {
    .user-info {
      max-width: 25rem;
    }
  }

  @media screen and (max-width: 1300px) {
    .user-info {
      max-width: 19rem;
    }
  }

  @media screen and (max-width: 1120px) {
    .user-info {
      max-width: 17rem;
    }
  }
}
</style>
