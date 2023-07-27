<template>
  <div class="invoice-body orange">
  <div class="invoice-body-up ovh">
    <div class="invoice-body-up-left fl">
      <dl>
        <dt class="invoice-body-up-left-img">
          <img width="70" height="70" src="./1561108150.png" alt="二维码">
        </dt>
        <dd class="black"><span class="l-s1 orange">机器编号：</span>0000000001</dd>
      </dl>
    </div>
    <div class="invoice-body-up-center ta-c fl">
      <p class="invoice-body-up-center-title l-s2">增值税电子普通发票</p>
      <div class="invoice-body-up-center-border"></div>
    </div>
    <div class="invoice-body-up-right fl lh24">
      <p>
        <span class="invoice-right-label w50">发票代码</span><span>：</span>
        <span class="black">{{jsonData.invoiceCode}}</span>
      </p>
      <p>
        <span class="invoice-right-label w50">发票号码</span><span>：</span>
        <span class="black">{{jsonData.invoiceNumber}}</span>
      </p>
      <p>
        <span class="invoice-right-label w50">开票日期</span><span>：</span>
        <span class="black"> {{dateList[0]}} <span class="orange">年</span> {{dateList[1]}} <span class="orange">月</span> {{dateList[2]}} <span
            class="orange">日</span></span>
      </p>
      <p>
        <span class="invoice-right-label w50">校验码</span><span>：</span>
        <span class="black">73165 09840 76544 48548</span>
      </p>
    </div>
  </div>
  <div class="invoice-table">
    <div class="invoice-table-header ovh">
      <div class="invoice-table-header-left fl ta-c l-s2 lh20">
        购买方
      </div>
      <div class="invoice-table-header-center1 fl ovh lh19">
        <p>
          <span class="invoice-right-label w88">名称</span><span>：</span>
          <span class="black">044031800211</span>
        </p>
        <p>
          <span class="invoice-right-label w88">纳税人识别号</span><span>：</span>
          <span class="black">805421210</span>
        </p>
        <p>
          <span class="invoice-right-label w88">地址、电话</span><span>：</span>
          <span class="black">重庆开票</span>
        </p>
        <p>
          <span class="invoice-right-label w88">开户行及账号</span><span>：</span>
          <span class="black">73165 09840 76544 48548</span>
        </p>
      </div>
      <div class="invoice-table-header-left fl ta-c l-s2 lh20">
        密码区
      </div>
      <div class="invoice-table-header-right fl ovh l-s1 black lh20 fz16">
        {{ password }}
      </div>
    </div>
    <div class="invoice-table-content ps-r">
      <div class="border-right-height ps-al-203"></div>
      <div class="border-right-height ps-al-260"></div>
      <div class="border-right-height ps-al-306"></div>
      <div class="border-right-height ps-al-394"></div>
      <div class="border-right-height ps-al-489"></div>
      <div class="border-right-height ps-al-607"></div>
      <div class="border-right-height ps-al-638"></div>
      <div class="invoice-table-content-header ovh ta-c lh20">
        <div class="fl w204">货物或应税劳务、服务名称</div>
        <div class="fl w57">规格型号</div>
        <div class="fl w46">单位</div>
        <div class="fl w88">数量</div>
        <div class="fl w95">单 价</div>
        <div class="fl w118">金 额</div>
        <div class="fl w31">税率</div>
        <div class="fl w117">税 额</div>
      </div>
      <div class="invoice-map ovh">
        <div class="invoice-table-content-center ovh black ta-r" v-for="(item,index) in details" :key="index">
          <div class="fl w204 ta-l">{{item.name}}</div>
          <div class="fl w57">{{ item.type }}</div>
          <div class="fl w46">{{item.unit}}</div>
          <div class="fl w88">{{ item.number }}</div>
          <div class="fl w95">{{ item.price }}</div>
          <div class="fl w118">{{item.price * item.number}}</div>
          <div class="fl w31 ta-r">{{item.taxRate}}</div>
          <div class="fl w117">{{item.taxMount}}</div>
        </div>
      </div>
      <div class="invoice-table-content-bottom ta-r ovh black">
        <div class="fl w204 ta-c l-s38 orange">合计</div>
        <div class="fl w57"></div>
        <div class="fl w46"></div>
        <div class="fl w88"></div>
        <div class="fl w95"></div>
        <div class="fl w118"> &yen;{{jsonData.amountPayable}}</div>
        <div class="fl w31"></div>
        <div class="fl w117"> &yen;{{jsonData.amountPayable * 0.1}}</div>
      </div>
    </div>
    <div class="invoice-table-content2 ovh">
      <div class="fl w552 ovh lh29">
        <p class="fl w192 black"><span class="orange">（共计）</span> &yen;{{jsonData.amountPayable}}</p>
      </div>
    </div>
      <div class="invoice-table-bottom ovh">
        <div class="invoice-table-header-left fl ta-c l-s2 lh20">销售方</div>
        <div class="invoice-table-header-center1 fl ovh lh19">
          <p>
            <span class="invoice-right-label w88">名称</span><span>：</span>
            <span class="black">044031800211</span>
          </p>
          <p>
            <span class="invoice-right-label w88">纳税人识别号</span><span>：</span>
            <span class="black">805421210</span>
          </p>
          <p>
            <span class="invoice-right-label w88">地址、电话</span><span>：</span>
            <span class="black">重庆开票</span>
          </p>
          <p>
            <span class="invoice-right-label w88">开户行及账号</span><span>：</span>
            <span class="black">73165 09840 76544 48548</span>
          </p>
        </div>
        <div class="invoice-table-header-left fl ta-c l-s2 lh34">
          备 注
        </div>
        <div class="fz12 invoice-table-header-right fl ovh l-s1 black lh20"></div>
      </div>
    </div>
    <div class="invoice-table-footer ovh black">
      <div class="fl m-l14 w212"><span class="orange">收款人：</span>沈俊涛</div>
      <div class="fl m-l14 w160"><span class="orange">复核：</span>沈俊涛</div>
      <div class="fl m-l14 w150"><span class="orange">开票人：</span>沈俊涛</div>
      <div class="fl m-l14 w150"><span class="orange">销售方：（章）</span></div>
    </div>
  </div>
</template>




<script>
export default {
  data: function() {
    return {
      password: '/2/75+<311+54*3667<<>>5<++9+/ <7-4+18-//0*12*25-1*9/17975203 <601*00<2+84*3625>6>**<-+/3>17-4+18-//0*12*25/8>*5/',
      jsonData: {},
      dateList: [],
      year: undefined,
      month: undefined,
      day: undefined,
      details: [
        {
          name:'手机',
          type: '1',
          unit: '件',
          number: '1',
          price: '100',
          taxRate: '10%',
          taxMount: '10'
        }
      ]
    }
  },
  methods: {
    getInfo() {
      const id = this.$route.params.id
      getInfoById(id).then(res => {
        this.jsonData = res.data.data
        const date = this.jsonData.invoiceDate;
        this.dateList = date.split("-");

        console.log(this.jsonData)
      })
      var datetime = new Date();
      this.year = datetime.getFullYear();
      this.month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
      this.day = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    }
  },
  created() {
    // this.getInfo()
  }
}
</script>











<style scoped>
p,
dl,
dt,
dd {
  margin: 0;
  padding: 0;
}

.fl {
  float: left;
}
.fz10 {
  font-size: 10px;
}
.fz12 {
  font-size: 12px;
}
.fz16 {
  font-size: 16px;
}

.black {
  color: #000;
}

.orange {
  color: #ab6226;
}

.l-s1 {
  letter-spacing: 1px;
}

.l-s2 {
  letter-spacing: 2px;
}

.l-s38 {
  letter-spacing: 38px;
}

.ta-c {
  text-align: center;
}

.ta-l {
  text-align: left;
}

.ta-r {
  text-align: right;
}

.ovh {
  overflow: hidden;
}

.ps-r {
  position: relative;
}

.m-l14 {
  margin-left: 14px;
}
.m-r20 {
  margin-right: 20px;
}
.ps-al-203,
.ps-al-260,
.ps-al-306,
.ps-al-394,
.ps-al-489,
.ps-al-607,
.ps-al-638 {
  position: absolute;
  bottom: 0;
  left: 203px;
}
.ps-al-306 {
  left: 306px;
}

.ps-al-260 {
  left: 260px;
}

.ps-al-394 {
  left: 394px;
}

.ps-al-489 {
  left: 489px;
}

.ps-al-638 {
  left: 638px;
}

.ps-al-607 {
  left: 607px;
}

.ps-al-203 {
  left: 203px;
}

.lh19 {
  line-height: 19px;
}

.lh20 {
  line-height: 20px;
}

.lh24 {
  line-height: 24px;
}

.lh29 {
  line-height: 29px;
}

.lh34 {
  line-height: 34px;
}

.invoice-body {
  width: 814px;
  height: 526px;
  background: #fff;
  padding: 10px 24px 30px 30px;
  font-size: 10px;
  font-family: "宋体";
  margin: 0 auto;
}

.invoice-body-up {
  margin: 0 auto 12px;
  width: 760px;
  height: 100px;
}

.invoice-body-up-left {
  width: 230px;
  height: 100%;
}

.invoice-body-up-left-img {
  padding: 0 0 6px 16px;
}

.invoice-body-up-center {
  width: 298px;
}

.invoice-body-up-center-title {
  font-size: 24px;
  margin: 24px 0 12px;
}

.invoice-body-up-center-border {
  border-top: 2px solid #902121;
  border-bottom: 2px solid #b46a6a;
  height: 1px;
}

.invoice-body-up-right {
  width: 210px;
  padding-left: 20px;
}

.w29 {
  width: 29px;
  height: 100%;
  min-height: 10px;
}
.w31 {
  width: 31px;
  height: 100%;
  min-height: 10px;
}
.w44 {
  width: 44px;
  height: 100%;
  min-height: 10px;
}
.w46 {
  width: 46px;
  height: 100%;
  min-height: 10px;
}

.w50 {
  width: 50px;
  height: 100%;
  min-height: 10px;
}

.w57 {
  width: 57px;
  height: 100%;
  min-height: 10px;
}
.w74 {
  width: 74px;
  height: 100%;
  min-height: 10px;
}
.w77 {
  width: 77px;
  height: 100%;
  min-height: 10px;
}
.w81 {
  width: 81px;
  height: 100%;
  min-height: 10px;
}
.w88 {
  width: 88px;
  height: 100%;
  min-height: 10px;
}
.w90 {
  width: 90px;
  height: 100%;
  min-height: 10px;
}
.w91 {
  width: 91px;
  height: 100%;
  min-height: 10px;
}
.w95 {
  width: 95px;
  height: 100%;
  min-height: 10px;
}
.w97 {
  width: 97px;
  height: 100%;
  min-height: 10px;
}
.w117 {
  width: 117px;
  height: 100%;
  min-height: 10px;
}

.w118 {
  width: 118px;
  height: 100%;
  min-height: 10px;
}

.w126 {
  width: 126px;
  height: 100%;
  min-height: 10px;
}

.w150 {
  width: 150px;
  height: 100%;
  min-height: 10px;
}

.w160 {
  width: 160px;
  height: 100%;
  min-height: 10px;
}

.w170 {
  width: 170px;
  height: 100%;
  min-height: 10px;
}

.w192 {
  width: 192px;
  height: 100%;
  min-height: 10px;
}

.w203 {
  width: 203px;
  height: 100%;
  min-height: 10px;
}

.w204 {
  width: 204px;
  height: 100%;
  min-height: 10px;
}

.w212 {
  width: 212px;
  height: 100%;
  min-height: 10px;
}
.w215 {
  width: 215px;
  height: 100%;
  min-height: 10px;
}
.w360 {
  width: 360px;
  height: 100%;
  min-height: 10px;
}

.w552 {
  width: 552px;
  height: 100%;
  min-height: 10px;
}

.t-i4 {
  text-indent: 4px;
}

.invoice-right-label {
  display: inline-block;
  text-align-last: justify;
  text-align: justify;
}

.invoice-table {
  border: 1px solid #902121;
  width: 758px;
  height: 353px;
  margin: 0 auto;
}

.invoice-table-header {
  height: 78px;
  border-bottom: 1px solid #902121;
  width: 100%;
}

.invoice-table-header-left {
  width: 24px;
  border-right: 1px solid #902121;
  color: #ab6226;
  height: 72px;
  padding: 6px 0 0 2px;
}

.invoice-table-header-center1 {
  width: 398px;
  border-right: 1px solid #902121;
  height: 78px;
  padding-left: 8px;
}

.invoice-table-header-right {
  width: 252px;
  height: 78px;
  padding: 0 20px 0 24px;
  word-break: break-all;
}

.invoice-table-content {
  height: 166px;
}

.invoice-table-content-header {
  height: 20px;
  width: 100%;
}
.invoice-map{
  height: 128px;
}

.invoice-table-content-center {
  height: 16px;
  line-height: 16px;
}

.invoice-table-content-bottom {
  height: 18px;
}

.border-right-height {
  border-right: 1px solid #902121;
  height: 100%;
}

.invoice-table-content2 {
  height: 29px;
  border-bottom: 1px solid #902121;
  border-top: 1px solid #902121;
  width: 100%;
}

.invoice-table-bottom {
  height: 78px;
  width: 100%;
}

.invoice-table-footer {
  margin: 10px auto;
  width: 758px;
}

.invoice-list {
  width: 795px;
  background: #ffffff;
  padding: 55px 35px 30px 38px;
  font-size: 14px;
  font-family: "宋体";
  margin: 0 auto;
}
.invoice-list-title {
  height: 58px;
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  color: #ab6226;
}
.invoice-list-header {
  height: 100px;
  width: 739px;
  overflow: hidden;
  padding-left: 6px;
}
.invoice-list-header-name {
  height: 30px;
}
.invoice-list-header-name1 {
  width: 310px;
}
.invoice-list-header-name2 {
  width: 258px;
}
.invoice-list-table {
  width: 744px;
  border: 2px solid #902121;
  margin-bottom: 24px;
}
.invoice-list-table-title {
  border-bottom: 2px solid #c58c8c;
  width: 100%;
  height: 18px;
  line-height: 18px;
  color: #ab6226;
  text-align: center;
  overflow: hidden;
  font-size: 12px;
}
.invoice-list-table-title1,
.invoice-list-table-title2,
.invoice-list-table-title3,
.invoice-list-table-title4,
.invoice-list-table-title5,
.invoice-list-table-title6,
.invoice-list-table-title7,
.invoice-list-table-title8 {
  float: left;
  width: 30px;
  min-height: 18px;
  border-right: 1px solid #c58c8c;
}

.invoice-list-table-title2 {
  width: 214px;
}
.invoice-list-table-title3 {
  width: 72px;
}
.invoice-list-table-title4 {
  width: 43px;
}
.invoice-list-table-title5 {
  width: 76px;
}
.invoice-list-table-title6 {
  width: 80px;
}
.invoice-list-table-title7 {
  width: 90px;
}
.invoice-list-table-title8 {
  width: 28px;
}
.invoice-list-table-title9 {
  float: left;
  width: 96px;
}
.invoice-table-map {
  height: 900px;
  overflow: hidden;
  position: relative;
}
.invoice-border-line1,
.invoice-border-line2,
.invoice-border-line3,
.invoice-border-line4,
.invoice-border-line5,
.invoice-border-line6,
.invoice-border-line7,
.invoice-border-line8 {
  position: absolute;
  height: 100%;
  bottom: 0;
  border-right: 1px solid #c58c8c;
}
.invoice-border-line1 {
  left: 30px;
}
.invoice-border-line2 {
  left: 245px;
}
.invoice-border-line3 {
  left: 318px;
}
.invoice-border-line4 {
  left: 362px;
}
.invoice-border-line5 {
  left: 439px;
}
.invoice-border-line6 {
  left: 520px;
}
.invoice-border-line7 {
  left: 611px;
}
.invoice-border-line8 {
  left: 640px;
}
.invoice-table-map-item {
  width: 100%;
  line-height: 18px;
  text-align: center;
  font-size: 12px;
  overflow: hidden;
}
.invoice-table-bottom1 {
  width: 100%;
  line-height: 18px;
  text-align: center;
  font-size: 12px;
  overflow: hidden;
}
.invoice-table-bottom2 {
  width: 100%;
  text-align: center;
  font-size: 12px;
  overflow: hidden;
  border-top: 1px solid #c58c8c;
}
.invoice-table-bottom2-left {
  min-height: 40px;float: left;width: 30px;border-right: 1px solid #c58c8c;text-align: center;color: #ab6226;line-height: 40px;
}
.invoice-table-bottom2-right {
  min-height: 40px;
  float: left;
  width: 700px;
  text-align: left;
}
.invoice-table-footer{
  overflow:hidden;margin: 0 auto;width:744px;
}
.invoice-table-footer-left{
  float: left;width: 460px;
}
.invoice-table-footer-right{
  float: left;width: 240px;
}
</style>