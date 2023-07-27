<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="问卷名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入问卷名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开放类型" prop="openType">
        <el-select
          v-model="queryParams.openType"
          placeholder="请选择开放类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in openTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
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
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="examList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="问卷名称"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="问卷开放类型"
        align="center"
        prop="openType"
        :formatter="openTypeFormat"
      />

      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
      </el-table-column>
      <el-table-column
        width="280px"
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { myAnswer } from "@/api/ques/exam";

export default {
  filters: {
    examStateFilter(value) {
      const map = {
        0: "待发布",
        1: "已发布",
        2: "已结束",
      };
      return map[value];
    },
  },
  name: "Exam",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 考试表格数据hvg b
      examList: [],
      // 考试状态字典
      stateOptions: [],
      gridData: [],
      openDetail: false,
      detailData: [],
      // 1公开2班级3定员字典
      openTypeOptions: [
        {
          dictValue: 1,
          dictLabel: "完全开放",
        },
        {
          dictValue: 2,
          dictLabel: "指定答者",
        },
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        openType: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handlePreview(row) {
      // window.location.href = `/ques/preview?id=${row.id}&viewAdmin=true`
      window.open(`/ques/view?id=${row.answerId}`, "_blank"); //注意第二个参数
    },

    /** 查询考试列表 */
    getList() {
      this.loading = true;
      myAnswer(this.queryParams).then((response) => {
        this.examList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    handleCopy(row) {
      var that = this;
      var url = `http://${location.host}/ques/preview?id=${row.id}`;
      this.$copyText(url).then(
        function (e) {
          that.msgSuccess("复制成功");
        },
        function (e) {
          console.log("copy arguments e:", e);
          alert("复制失败!");
        }
      );
    },
    // 考试状态字典翻译
    stateFormat(row, column) {
      return this.selectDictLabel(this.stateOptions, row.state);
    },
    // 1公开2部门3定员字典翻译
    openTypeFormat(row, column) {
      return this.selectDictLabel(this.openTypeOptions, row.openType);
    },
    // 取消按钮
    cancel() {
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        totalTime: undefined,
        state: undefined,
        title: undefined,
        qualifyScore: undefined,
        timeLimit: undefined,
        content: undefined,
        createTime: undefined,
        startTime: undefined,
        openType: undefined,
        stopTime: undefined,
        joinType: undefined,
        totalScore: undefined,
        level: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
  },
};
</script>