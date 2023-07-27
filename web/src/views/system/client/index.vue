<template>
  <div class="app-container">
    <el-form
      ref="queryForm"
      :model="queryParams"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="客户端Id" prop="clientId">
        <el-input
          v-model="queryParams.clientId"
          placeholder="请输入客户端Id"
          clearable
          style="width: 240px"
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['client:delete']"
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['client:add']"
          type="success"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          >新增</el-button
        >
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户端Id" align="center" prop="clientId" />
      <el-table-column label="范围" align="center" prop="scope" />
      <el-table-column
        label="认证模式"
        align="center"
        prop="authorizedGrantTypes"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="访问令牌有效期(秒)"
        align="center"
        prop="accessTokenValidity"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.accessTokenValidity }}s</span>
        </template>
      </el-table-column>
      <el-table-column
        label="刷新令牌有效期(秒)"
        align="center"
        prop="refreshTokenValidity"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.refreshTokenValidity }}s</span>
        </template>
      </el-table-column>
      <el-table-column
        label="重定向地址"
        align="center"
        prop="webServerRedirectUri"
        :show-overflow-tooltip="true"
      />

      <el-table-column label="自动授权" align="center" prop="isSuccess">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.autoapprove == 1" type="success">是</el-tag>
          <el-tag v-if="scope.row.autoapprove == 0" type="danger">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['client:decrypt']"
            size="mini"
            type="text"
            icon="el-icon-unlock"
            @click="handleViewSecret(scope.row)"
            >密钥</el-button
          >
          <el-button
            v-hasPermi="['client:delete']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
          <el-button
            v-hasPermi="['client:update']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            >修改</el-button
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

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-form-item label="客户端Id" prop="clientId">
          <el-input
            v-model="form.clientId"
            placeholder="请输入客户端Id"
            :disabled="!type || type != 'add'"
          />
        </el-form-item>
        <el-form-item label="客户端密钥" prop="clientSecret">
          <el-input
            v-model="form.clientSecret"
            placeholder="请输入客户端密钥"
          />
        </el-form-item>
        <el-form-item label="范围" prop="scope">
          <el-input v-model="form.scope" placeholder="请输入范围" />
        </el-form-item>
        <el-form-item label="认证模式" prop="types">
          <el-select
          @change="selectChangeHandle" 
            multiple
            style="width: 100%"
            v-model="form.types"
            placeholder="请选择认证模式"
          >
            <el-option
              v-for="item in grantTypesOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="重定向地址" prop="webServerRedirectUri">
          <el-input
            v-model="form.webServerRedirectUri"
            placeholder="请输入重定向地址"
          />
        </el-form-item>
        <el-form-item label="访问令牌有效期（秒）" prop="accessTokenValidity">
          <el-input
            v-model="form.accessTokenValidity"
            type="number"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item label="刷新令牌有效期（秒）" prop="refreshTokenValidity">
          <el-input
            v-model="form.refreshTokenValidity"
            type="number"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item label="是否自动授权" prop="autoapprove">
          <el-select
            style="width: 100%"
            v-model="form.autoapprove"
            placeholder="请选择是否自动授权"
          >
            <el-option
              v-for="item in autoapproveOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  list,
  viewClientSecret,
  del,
  check,
  addClient,
  updateClient,
} from "@/api/system/client";
import { validURL, isIntegerGreaterThanZero } from "@/utils/penint";
export default {
  name: "Operlog",
  data() {
    var valideteClient = (rule, value, callback) => {
      check(value).then((response) => {
        if (!response.data) {
          callback(new Error("该客户端Id已经存在，不能重复添加"));
        } else {
          callback();
        }
      });
    };
    var valideteUrl = (rule, value, callback) => {
      if (value !== "" && value != null && !validURL(value)) {
        callback(new Error("请输入正确的URL"));
      } else {
        callback();
      }
    };
    var valideteAccessTokenValidity = (rule, value, callback) => {
      if (!isIntegerGreaterThanZero(value)) {
        callback(new Error("请输入大于零的整数"));
      } else if (value.toString().length > 11) {
        callback(new Error("长度不能超过11个字符"));
      } else {
        callback();
      }
    };
    var valideteRefreshTokenValidity = (rule, value, callback) => {
      if (value === null || value === "") {
        callback();
      } else if (!isIntegerGreaterThanZero(value)) {
        callback(new Error("请输入大于零的整数"));
      } else if (value.toString().length > 11) {
        callback(new Error("长度不能超过11个字符"));
      } else {
        callback();
      }
    };

    return {
      grantTypesOptions: [
        {
          value: "authorization_code",
          label: "authorization_code",
        },
        {
          value: "refresh_token",
          label: "refresh_token",
        },
        {
          value: "client_credentials",
          label: "client_credentials",
        },
        {
          value: "password",
          label: "password",
        },
        {
          value: "implicit",
          label: "implicit",
        },
      ],
      autoapproveOptions: [
        {
          value: 1,
          label: "是",
        },
        {
          value: 0,
          label: "否",
        },
      ],
      rules: {
        clientId: [
          { required: true, message: "请输入客户端Id", trigger: "blur" },
          {
            min: 3,
            max: 20,
            message: "长度在 3 到 20 个字符",
            trigger: "blur",
          },
          {
            validator: (rule, value, callback) => {
              if (this.type === "add") {
                check(value).then((response) => {
                  if (!response.data) {
                    callback(new Error("该客户端Id已经存在，不能重复添加"));
                  } else {
                    callback();
                  }
                });
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
          // { required: true, validator: valideteClient, trigger: "blur" },
        ],
        scope: [
          { max: 100, message: "长度不能超过100个字符", trigger: "blur" },
          { required: true, message: "请输入范围", trigger: "blur" },
        ],
        webServerRedirectUri: [
          { required: false, validator: valideteUrl, trigger: "blur" },
        ],
        accessTokenValidity: [
          { required: true, message: "请输入访问令牌有效期", trigger: "blur" },
          {
            required: true,
            validator: valideteAccessTokenValidity,
            trigger: "blur",
          },
        ],
        refreshTokenValidity: [
          { required: true, message: "请输入刷新令牌有效期", trigger: "blur" },
          {
            required: true,
            validator: valideteRefreshTokenValidity,
            trigger: "blur",
          },
        ],
        clientSecret: [
          { required: true, message: "请输入客户端密钥", trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
        types: [
          { required: true, message: "请选择认证模式", trigger: "change" },
        ],
        autoapprove: [
          { required: true, message: "请选择是否自动授权", trigger: "change" },
        ],
      },
      // 数据操作类型：add/update 控制修改时表单验证
      type: "",
      title: undefined,
      // 遮罩层
      loading: true,
      // 选中数组
      clientIds: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      moduleOptions: undefined,
      createTime: undefined,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {
        clientId: "",
        // resourceIds: '',
        clientSecret: "",
        scope: "",
        authorizedGrantTypes: "",
        webServerRedirectUri: "",
        accessTokenValidity: null,
        refreshTokenValidity: null,
        autoapprove: "",
        types: [],
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        clientId: undefined,
      },
      isSuccessValue: "",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.clientId) {
            this.form.authorizedGrantTypes = this.form.types.join(",");
            updateClient(this.form).then((res) => {
              if (res.status == 200) {
                this.msgSuccess("客户端修改成功");
                this.open = false;
                this.$refs["form"].resetFields();
                this.getList();
              }
            });
          } else {
            this.form.authorizedGrantTypes = this.form.types.join(",");
            addClient(this.form).then((res) => {
              if (res.status == 200) {
                this.msgSuccess("客户端新增成功");
                this.open = false;
                this.$refs["form"].resetFields();
                this.getList();
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    selectChangeHandle(){
      this.$forceUpdate()
    },
    cancel() {
      this.open = false;
      this.getList();
    },
    // 新增客户端
    handleAdd() {
      this.open = true;
      this.title = "新增客户端";
      this.type = "add";
    },
    handleUpdate(row) {
      this.open = true;
      this.title = "修改客户端";
      this.form = row;
      this.form.types = row.authorizedGrantTypes.split(",");
    },
    handleViewSecret(row) {
      viewClientSecret(row.clientId).then((res) => {
        this.msgSuccess("该客户端秘钥值：" + res.msg);
      });
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then((response) => {
        this.list = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.clientIds = selection.map((item) => item.clientId);
      this.multiple = !selection.length;
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const clientIds = row.clientId || this.clientIds;
      this.$confirm(
        '是否确认删除客户端id为"' + clientIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return del(clientIds);
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
