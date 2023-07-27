<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="客户姓名" prop="realName">
                <el-input v-model="queryParams.realName" placeholder="请输入客户姓名" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="单位名称" prop="companyName">
                <el-input v-model="queryParams.companyName" placeholder="请输入单位名称" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="移动电话" prop="phone">
                <el-input v-model="queryParams.phone" placeholder="请输入移动电话" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="queryParams.idCard" placeholder="请输入身份证号" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['auth:customUser:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['auth:customUser:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['auth:customUser:remove']">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="序号" align="center" prop="id" width="80">
                <template slot-scope="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="用户名" align="center" prop="username" />
            <!-- <el-table-column label="密码" align="center" prop="password" /> -->
            <el-table-column label="客户姓名" align="center" prop="realName" />
            <el-table-column label="单位名称" align="center" prop="companyName" />
            <el-table-column label="移动电话" align="center" prop="phone" />
            <el-table-column label="座机" align="center" prop="tel" />
            <el-table-column label="身份证号" align="center" prop="idCard" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
                <template slot-scope="scope">
                    <el-button icon="el-icon-view" type="text" size="mini" @click="handleView(scope.row)"
                        v-hasPermi="['auth:customUser:query']">详情</el-button>
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['auth:customUser:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['auth:customUser:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改auth对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="130px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="form.username" placeholder="请输入用户名" maxlength="20" show-word-limit />
                </el-form-item>
                <!-- <el-form-item label="密码" prop="password">
                    <el-input v-model="form.password" placeholder="请输入密码" />
                </el-form-item> -->
                <el-form-item label="客户姓名" prop="realName">
                    <el-input v-model="form.realName" placeholder="请输入客户姓名" maxlength="10" show-word-limit />
                </el-form-item>
                <el-form-item label="单位名称" prop="companyName">
                    <el-input v-model="form.companyName" placeholder="请输入单位名称" maxlength="50" show-word-limit />
                </el-form-item>
                <el-form-item label="移动电话" prop="phone">
                    <el-input v-model="form.phone" placeholder="请输入移动电话"  maxlength="11" show-word-limit/>
                </el-form-item>
                <el-form-item label="座机" prop="tel">
                    <el-input v-model="form.tel" placeholder="请输入座机" />
                </el-form-item>
                <el-form-item label="身份证号" prop="idCard">
                    <el-input v-model="form.idCard" placeholder="请输入身份证号"  maxlength="18" show-word-limit/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 查看详情对话框 -->
        <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
            <el-descriptions title="" direction="vertical" :column="3" border>
                <el-descriptions-item label="主键ID" :span="1">{{ form.id }}</el-descriptions-item>
                <el-descriptions-item label="用户名" :span="1">{{ form.username }}</el-descriptions-item>
                <!-- <el-descriptions-item label="密码" :span="1">{{ form.password }}</el-descriptions-item> -->
                <el-descriptions-item label="客户姓名" :span="1">{{ form.realName }}</el-descriptions-item>
                <el-descriptions-item label="单位名称" :span="1">{{ form.companyName }}</el-descriptions-item>
                <el-descriptions-item label="移动电话" :span="1">{{ form.phone }}</el-descriptions-item>
                <el-descriptions-item label="座机" :span="1">{{ form.tel }}</el-descriptions-item>
                <el-descriptions-item label="身份证号" :span="1">{{ form.idCard }}</el-descriptions-item>
                <el-descriptions-item label="创建时间" :span="1">{{ form.createTime }}</el-descriptions-item>
            </el-descriptions>

        </el-dialog>

    </div>
</template>
  
<script>
import { listUser, getUser, delUser, addUser, updateUser } from "@/api/customUser";

export default {
    name: "User",
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
            // auth表格数据
            userList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否显示详情
            openView: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                realName: undefined,
                companyName: undefined,
                phone: undefined,
                idCard: undefined,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                username: [{ required: true, message: "用户名不能为空", trigger: "blur" }],
                password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
                realName: [{ required: true, message: "客户姓名不能为空", trigger: "blur" }],
                companyName: [{ required: true, message: "单位名称不能为空", trigger: "blur" }],
                phone: [
                    { required: true, message: "移动电话不能为空", trigger: "blur" },
                    {
                        pattern: /^1\d{10}$/,
                        message: "请输入正确的手机号码",
                        trigger: "blur",
                    },
                ],
                tel: [
                    { required: true, message: "座机不能为空", trigger: "blur" },
                    {
                        pattern: /^0\d{2,3}-?\d{7,8}(-\d{1,6})?$/,
                        message: "请输入正确的座机号码",
                        trigger: "blur",
                    },
                ],
                idCard: [
                    { required: true, message: "身份证号不能为空", trigger: "blur" },
                    {
                        pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
                        message: "请输入正确的身份证号码",
                        trigger: "blur",
                    },
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询auth列表 */
        getList() {
            this.loading = true;
            listUser(this.queryParams).then(response => {
                this.userList = response.data.records;
                this.total = response.data.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                id: undefined,
                username: undefined,
                password: undefined,
                realName: undefined,
                companyName: undefined,
                phone: undefined,
                tel: undefined,
                idCard: undefined,
                createTime: undefined
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
            this.ids = selection.map(item => item.id)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加auth";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getUser(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改auth";
            });
        },
        /** 详情按钮操作 */
        handleView(row) {
            this.reset();
            const id = row.id || this.ids
            getUser(id).then(response => {
                this.form = response.data;
                this.openView = true;
                this.title = "查看详情";
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != undefined) {
                        updateUser(this.form).then(response => {
                            if (response.status === 200) {
                                this.msgSuccess("修改成功");
                                this.open = false;
                                this.getList();
                            } else {
                                this.msgError(response.msg);
                            }
                        });
                    } else {
                        addUser(this.form).then(response => {
                            if (response.status === 200) {
                                this.msgSuccess("新增成功");
                                this.open = false;
                                this.getList();
                            } else {
                                this.msgError(response.msg);
                            }
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$confirm('是否确认删除auth编号为"' + ids + '"的数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function () {
                return delUser(ids);
            }).then(() => {
                this.getList();
                this.msgSuccess("删除成功");
            }).catch(function () { });
        },
    }
}
</script>