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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          >删除</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="examList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="问卷ID"
        align="center"
        prop="id"
        :show-overflow-tooltip="true"
      />
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

      <el-table-column label="问卷开放时间" width="220px" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.timeLimit">
            {{ parseTime(scope.row.startTime, "{y}-{m}-{d}") }} ~
            {{ parseTime(scope.row.stopTime, "{y}-{m}-{d}") }}
          </span>
          <span v-else>不限时</span>
        </template>
      </el-table-column>

      <el-table-column label="可答卷次数" align="center" prop="num" />
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          {{ scope.row.status | examStateFilter }}
        </template>
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
            >预览</el-button
          >
          <el-dropdown
            size="mini"
            @command="(command) => handleCommand(command, scope.row)"
          >
            <span
              class="el-dropdown-link"
              style="font-size: 12px; color: #409eff"
            >
              <i class="el-icon-d-arrow-right el-icon--right"></i>更多
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                command="handlePublish"
                v-if="scope.row.status == 0"
                icon="el-icon-circle-check"
                >发布</el-dropdown-item
              >
              <el-dropdown-item
                command="handleDetail"
                v-if="scope.row.status != 0"
                icon="el-icon-view"
                >查看已答列表</el-dropdown-item
              >
              <el-dropdown-item
                command="handleStat"
                v-if="scope.row.status != 0"
                icon="el-icon-pie-chart"
                >统计分析</el-dropdown-item
              >
              <el-dropdown-item command="handleSetting" icon="el-icon-setting"
                >问卷风格设置</el-dropdown-item
              >
              <el-dropdown-item command="handleCopy" icon="el-icon-circle-check"
                >复制问卷链接</el-dropdown-item
              >
              <el-dropdown-item
                command="handleStop"
                v-if="scope.row.status == 1"
                icon="el-icon-circle-close"
                >停止</el-dropdown-item
              >
              <el-dropdown-item command="handleDelete" icon="el-icon-delete"
                >删除</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
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

    <el-dialog title="问卷详情" :visible.sync="openDetail">
      <el-table :data="detailList">
        <el-table-column property="nickName" label="考生姓名"></el-table-column>
        <el-table-column
          property="createTime"
          label="提交时间"
        ></el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleInfo(scope.row)"
              >查看答题详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total1 > 0"
        :total="total1"
        :page.sync="query.pageNum"
        :limit.sync="query.pageSize"
        @pagination="_detailList"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  listExam,
  delExam,
  statistical,
  editStatus,
  examAnswerList,
} from "@/api/ques/exam";

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
      dialogTableVisible: false,
      // 考试表格数据
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
      rowId: undefined,
      query: {
        pageNum: 1,
        pageSize: 10,
      },
      detailList: [],
      total1: 0,
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
      window.open(`/ques/preview?id=${row.id}&viewAdmin=true`, "_blank"); //注意第二个参数
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handlePublish":
          this.handlePublish(row);
          break;
        case "handleDelete":
          this.handleDelete(row);
          break;
        case "handleCopy":
          this.handleCopy(row);
          break;
        case "handleStop":
          this.handleStop(row);
          break;
        case "handleStat":
          this.handleStat(row);
          break;
        case "handleSetting":
          this.handleSetting(row);
          break;
        case "handleDetail":
          this.handleDetail(row);
          break;
        default:
          break;
      }
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
    handlePublish(row) {
      const id = row.id;
      this.$confirm('是否确认发布问卷编号为"' + id + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return editStatus(id, 1);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("发布成功");
        })
        .catch(function () {});
    },
    handleStop(row) {
      const id = row.id;
      this.$confirm('是否确认停止问卷编号为"' + id + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return editStatus(id, 2);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("发布成功");
        })
        .catch(function () {});
    },
    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExam(this.queryParams).then((response) => {
        this.examList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
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
    handleSetting(row) {
      this.$router.push("/ques/setting?id=" + row.id);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push("/ques/exam-detail");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push("/ques/exam-detail?id=" + row.id);
    },
    handleInfo(row) {
      window.open(`/ques/view?id=${row.answerId}`, "_blank"); //注意第二个参数
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除问卷编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delExam(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    handleStat(row) {
      console.log(row, "sss");
      this.$router.push(`/ques/stat?id=${row.id}`);
    },

    handleDetail(row) {
      this.rowId = row.id;
      this._detailList();
      this.openDetail = true;
    },

    async _detailList() {
      let res = await examAnswerList(this.rowId, this.query);
      this.detailList = res.data.records;
      this.total1 = res.data.total;
    },
  },
};
</script>