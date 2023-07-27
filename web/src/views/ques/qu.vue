<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="题目类型" prop="quType">
        <el-select
          v-model="queryParams.quType"
          placeholder="请选择题目类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in quTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="难度等级" prop="level">
        <el-select
          v-model="queryParams.level"
          placeholder="请选择难度等级"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in levelOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item> -->
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
      <!-- <el-col :span="1.5">
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
      </el-col> -->
    </el-row>

    <el-table
      v-loading="loading"
      :data="quList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="题目ID" align="center" prop="id" />
      <el-table-column label="题目内容" align="center" prop="content" />
      <el-table-column
        label="题目类型"
        align="center"
        prop="quType"
        :formatter="quTypeFormat"
      />
      <!-- <el-table-column
        label="难度等级"
        align="center"
        prop="level"
        :formatter="levelFormat"
      /> -->
       <el-table-column
        label="发布人"
        align="center"
        prop="publishName"
      />
      <el-table-column
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
            v-if="user.comId == scope.row.comId"
            >修改</el-button
          >
          <el-button
            size="mini"
            v-if="user.comId == scope.row.comId"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            >删除</el-button
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
import { listQu, delQu } from "@/api/ques/qu";

export default {
  name: "Qu",
  computed: {
    user() {
      return this.$store.state.user;
    },
  },
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
      // 题目表格数据
      quList: [],
      // 弹出层标题
      title: "",
      levelOptions: [
        { dictValue: 1, dictLabel: "普通" },
        { dictValue: 2, dictLabel: "较难" },
      ],
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
        // {
        //   dictValue: 4,
        //   dictLabel: "简答题",
        // },
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: undefined,
        quType: undefined,
        level: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        content: [
          { required: true, message: "题目内容不能为空", trigger: "blur" },
        ],
        quType: [
          { required: true, message: "题目类型不能为空", trigger: "blur" },
        ],
        level: [
          { required: true, message: "1普通,2较难不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    console.log(this.user);
  },
  methods: {
    /** 查询题目列表 */
    getList() {
      this.loading = true;
      listQu(this.queryParams).then((response) => {
        this.quList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 题目类型字典翻译
    quTypeFormat(row, column) {
      return this.selectDictLabel(this.quTypeOptions, row.quType);
    },
    // 1普通,2较难字典翻译
    levelFormat(row, column) {
      return this.selectDictLabel(this.levelOptions, row.level);
    },
    // 取消按钮
    cancel() {
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        content: undefined,
        createTime: undefined,
        analysis: undefined,
        id: undefined,
        quType: undefined,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push("/ques/qu-detail");
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push("/ques/qu-detail?id=" + row.id);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除题目编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delQu(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
  },
};
</script>