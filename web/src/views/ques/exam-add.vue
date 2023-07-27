<template>
  <div class="app-container">
    <h3>问卷配置</h3>
    <el-card style="margin-top: 20px">
      <el-form
        ref="postForm"
        :model="postForm"
        :rules="rules"
        label-position="left"
        label-width="120px"
      >
        <el-form-item label="问卷名称" prop="title">
          <el-input v-model="postForm.title" />
        </el-form-item>

        <el-form-item label="问卷描述" prop="content">
          <el-input v-model="postForm.content" type="textarea" />
        </el-form-item>

        <el-form-item label="可答卷次数" prop="num">
          <el-input-number v-model="postForm.num" />
        </el-form-item>

        <el-form-item label="是否限时">
          <el-checkbox v-model="postForm.timeLimit" />
        </el-form-item>

        <el-form-item
          v-if="postForm.timeLimit"
          label="问卷时间"
          prop="dateValues"
        >
          <el-date-picker
            v-model="postForm.dateValues"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          />
        </el-form-item>
      </el-form>
    </el-card>

    <h3>答题选择</h3>
    <el-card style="margin-top: 20px">
      <div style="margin-bottom: 20px">
        已选择:<br /><br />
        <el-tag
          v-for="(item, index) in selectList"
          :key="index"
          style="margin-right: 10px"
        >
          {{ item.content }}
        </el-tag>
      </div>
      <el-table
        v-loading="loading"
        :data="list"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
        ref="tablea"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
          :reserve-selection="true"
        />
        <el-table-column label="题目ID" align="center" prop="id" />
        <el-table-column label="题目内容" align="center" prop="content" />
        <el-table-column
          label="题目类型"
          align="center"
          prop="quType"
          :formatter="quTypeFormat"
        />
        <el-table-column label="发布人" align="center" prop="publishName" />
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>
    <h3>权限配置</h3>
    <el-card style="margin-top: 20px">
      <el-radio-group v-model="postForm.openType" style="margin-bottom: 20px">
        <el-radio :label="1" border>完全公开</el-radio>
        <el-radio :label="2" border>指定答者</el-radio>
      </el-radio-group>

      <el-alert
        v-if="postForm.openType === 1"
        title="开放的，任何答者都可以进行考试！"
        type="warning"
      />

      <div v-show="postForm.openType === 2">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
        >
          <!-- <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
              >重置</el-button
            >
          </el-form-item> -->
        </el-form>
        已选择:<br /><br />
        <el-tag
          v-for="(item, index) in selectUserList"
          :key="index"
          style="margin-right: 10px"
        >
          {{ item.nickName }}
        </el-tag>
        <br /><br />
        <el-table
          v-loading="loading"
          :data="userList"
          ref="tableb"
          @selection-change="handleUserSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="用户账号"
            align="center"
            prop="username"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="姓名"
            align="center"
            prop="nickName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="邮箱"
            align="center"
            prop="email"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="userTotal > 0"
          :total="userTotal"
          :page.sync="userQueryParams.pageNum"
          :limit.sync="userQueryParams.pageSize"
          @pagination="getUserList"
        />
      </div>
    </el-card>

    <div style="margin-top: 20px">
      <el-button type="primary" @click="handleSave">保存</el-button>
    </div>
  </div>
</template>

<script>
import { getExam, addExam } from "@/api/ques/exam";
import { listUserAll } from '@/api/system/user'

import { listQu } from "@/api/ques/qu";

export default {
  name: "ExamDetail",
  data() {
    return {
      userList: [],
      userTotal: 0,
      userLoading: true,
      loading: true,
      total: 0,
      list: [],
      queryParams: {
        pageNum: 1,
        pageSize: 50,
      },
      att: [],
      userQueryParams: {
        pageNum: 1,
        pageSize: 50,
        attributes: "",
      },
      options: [
        {
          value: "1",
          label: "运动",
        },
        {
          value: "2",
          label: "数码",
        },
        {
          value: "3",
          label: "美妆",
        },
        {
          value: "4",
          label: "母婴",
        },
      ],
      selectList: [],
      selectUserList: [],
      quTypeOptions: [
        {
          dictValue: 1,
          dictLabel: "单选题",
        },
        {
          dictValue: 2,
          dictLabel: "多选题",
        },
        {
          dictValue: 3,
          dictLabel: "判断题",
        },
      ],
      step: 1,
      defaultProps: {
        label: "label",
      },
      levels: [
        { value: 0, label: "不限" },
        { value: 1, label: "普通" },
        { value: 2, label: "较难" },
      ],
      treeLoading: false,
      quDialogShow: false,
      quDialogType: 1,
      excludes: [],

      scoreDialog: false,
      scoreBatch: 0,

      // 题库
      repoList: [],

      // 题目列表
      quList: [[], [], [], []],
      quEnable: [false, false, false, false],
      postForm: {
        // 可答卷次数
        num: 1,
        // 题目列表
        selectList: [],
        // 答者列表
        selectUserList: [],
        // 开放类型
        openType: 1,
        dateValues: [],
      },
      rules: {
        title: [{ required: true, message: "考试名称不能为空！" }],
        content: [{ required: true, message: "考试名称不能为空！" }],
        open: [{ required: true, message: "考试权限不能为空！" }],
        num: [{ required: true, message: "可答卷次数不能为空！" }],
        dateValues: [{ required: true, message: "问卷时间不能为空！" }],
        ruleId: [{ required: true, message: "考试规则不能为空" }],
        password: [{ required: true, message: "考试口令不能为空！" }],
      },
    };
  },

  watch: {},
  created() {
    this.getList();
    this.getUserList();
  },
  mounted() {
    const id = this.$route.query.id;
    if (typeof id !== "undefined") {
      this.fetchData(id);
    }
  },
  methods: {
    handleQuery() {
      this.getUserList();
    },
    resetQuery() {
      this.att = [];
      this.getUserList();
    },
    getRowKeys(row) {
      return row.id;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      const a = selection.map((item) => {
        var id = item.id;
        var content = item.content;
        const a = {
          id: id,
          content: content,
        };
        return a;
      });
      this.selectList = a;
    },
    // 多选框选中数据
    handleUserSelectionChange(selection) {
      const a = selection.map((item) => {
        const a = {
          userId: item.userId,
          nickName: item.nickName,
        };
        return a;
      });
      this.selectUserList = a;
    },
    // 题目类型字典翻译
    quTypeFormat(row, column) {
      return this.selectDictLabel(this.quTypeOptions, row.quType);
    },
    /** 查询用户列表 */
    async getUserList() {
      this.userloading = true;
      this.userQueryParams.attributes = "";
      var a = "";
      if (this.att.length > 0) {
        this.att.forEach((e) => {
          a += e + ",";
        });
        a = a.substring(0, a.length - 1);
        this.userQueryParams.attributes = a;
      }
      let response = await listUserAll()
      this.userList = response.data.records;
      this.userTotal = response.data.total;
      console.log(this.userList, 'uuuuuuuuuuuuuu');
      this.userLoading = false;
    },
    async getList() {
      this.loading = true;
      let response = await listQu(this.queryParams);
      if (response.status == 200) {
        this.list = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      }
    },
    handleSave() {
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return;
        }
        if (this.selectList.length == 0) {
          this.msgError("请勾选问卷题目");
          return;
        }

        if (this.postForm.openType == 2 && this.selectUserList.length == 0) {
          this.msgError("请勾选答者用户");
          return;
        }
        this.$confirm("确实要提交保存吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.submitForm();
        });
      });
    },
    async fetchData(id) {
      const that = this;
      let response = await getExam(id);
      this.postForm = response.data;
      this.selectList = this.postForm.selectList;
      this.selectUserList = this.postForm.selectUserList;
      var ids = [];
      var userIds = [];
      if (this.selectList.length > 0) {
        this.selectList.forEach((e) => {
          ids.push(e.id);
        });
        this.list.forEach((row) => {
          if (ids.indexOf(row.id) >= 0) {
            this.$refs.tablea.toggleRowSelection(row, true); //点击选中
          }
        });
      }
      if (this.selectUserList.length > 0) {
        this.selectUserList.forEach((e) => {
          userIds.push(e.userId);
        });

        this.userList.forEach((row) => {
          if (userIds.indexOf(row.userId) >= 0) {
            this.$refs.tableb.toggleRowSelection(row, true); //点击选中
          }
        });
      }
    },

    submitForm() {
      // 校验和处理数据
      this.postForm.selectList = this.selectList;
      if (this.postForm.openType == 2) {
        this.postForm.selectUserList = this.selectUserList;
      }
      addExam(this.postForm).then(() => {
        this.$notify({
          title: "成功",
          message: "问卷保存成功！",
          type: "success",
          duration: 2000,
        });

        this.$router.push("/ques/exam");
      });
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.deptName.indexOf(value) !== -1;
    },

    repoChange(e, row) {
      if (e != null) {
        row.totalRadio = e.radioCount;
        row.totalMulti = e.multiCount;
        row.totalJudge = e.judgeCount;
        row.saqCount = e.saqCount;
      } else {
        row.totalRadio = 0;
        row.totalMulti = 0;
        row.totalJudge = 0;
        row.saqCount = 0;
      }
    },
  },
};
</script>

<style scoped>
</style>

