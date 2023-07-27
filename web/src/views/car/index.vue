<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="车牌号" prop="carNum">
                <el-input v-model="queryParams.carNum" placeholder="请输入车牌号" clearable size="small"
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
                    v-hasPermi="['busi:car:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['busi:car:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['busi:car:remove']">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="carList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="序号" align="center" prop="id" width="80">
                <template slot-scope="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="编号" align="center" prop="id" />
            <el-table-column label="车牌号" align="center" prop="carNum" />
            <el-table-column label="所属员工" align="center" prop="userId" :formatter="userIdFormat" />
            <el-table-column label="状态" align="center" prop="status">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.status == 1" type="danger">使用中</el-tag>
                    <el-tag v-if="scope.row.status == 2">未使用</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-view" size="mini" @click="handleView(scope.row)"
                        v-hasPermi="['busi:car:query']">详情</el-button>
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['busi:car:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['busi:car:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改车辆管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="130px">
                <el-form-item label="车牌号" prop="carNum">
                    <el-input v-model="form.carNum" placeholder="请输入车牌号" />
                </el-form-item>
                <el-form-item label="所属员工">
                    <el-select v-model="form.userId" placeholder="请选择员工" style="width:100%">
                        <el-option v-for="dict in userOptions" :key="dict.userId" :label="dict.nickName"
                            :value="dict.userId"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 查看详情对话框 -->
        <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
            <el-descriptions title="" direction="vertical" :column="2" border>
                <el-descriptions-item label="编号" :span="1">{{ form.id }}</el-descriptions-item>
                <el-descriptions-item label="车牌号" :span="1">{{ form.carNum }}</el-descriptions-item>
                <el-descriptions-item label="所属员工" :span="1">{{ userIdFormat(form) }}</el-descriptions-item>
                <el-descriptions-item label="状态" :span="1">{{ form.status == 1 ? '使用中' : '未使用' }}</el-descriptions-item>
            </el-descriptions>
        </el-dialog>

    </div>
</template>
  
<script>
import { listCar, getCar, delCar, addCar, updateCar } from "@/api/car";
import { userByRole } from '@/api/common'
export default {
    name: "Car",
    data() {
        return {
            userOptions: [],
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
            // 车辆管理表格数据
            carList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否显示详情
            openView: false,
            // 员工id字典
            userIdOptions: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                carNum: undefined,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                carNum: [
                    { required: true, message: "车牌号不能为空", trigger: "blur" },
                    {
                        pattern: /^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/,
                        message: "请输入正确的车牌号",
                        trigger: "blur"
                    }
                ],
                userId: [
                    { required: true, message: "员工不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
        this._userByRole()
    },
    methods: {
        _userByRole() {
            userByRole(4).then(res => {
                this.userOptions = res.data
            })
        },
        /** 查询车辆管理列表 */
        getList() {
            this.loading = true;
            listCar(this.queryParams).then(response => {
                this.carList = response.data.records;
                this.total = response.data.total;
                this.loading = false;
            });
        },
        // 员工id字典翻译
        userIdFormat(row, column) {
            var actions = [];
            Object.keys(this.userOptions).map((key) => {
                if (this.userOptions[key].userId == row.userId) {
                    actions.push(this.userOptions[key].nickName);
                    return false;
                }
            })
            return actions.join('');
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
                carNum: undefined,
                userId: undefined,
                status: undefined,
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
            this.title = "添加车辆管理";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getCar(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改车辆管理";
            });
        },
        /** 详情按钮操作 */
        handleView(row) {
            this.reset();
            const id = row.id || this.ids
            getCar(id).then(response => {
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
                        updateCar(this.form).then(response => {
                            if (response.status === 200) {
                                this.msgSuccess("修改成功");
                                this.open = false;
                                this.getList();
                            } else {
                                this.msgError(response.msg);
                            }
                        });
                    } else {
                        addCar(this.form).then(response => {
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
            this.$confirm('是否确认删除车辆管理编号为"' + ids + '"的数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function () {
                return delCar(ids);
            }).then(() => {
                this.getList();
                this.msgSuccess("删除成功");
            }).catch(function () { });
        },
    }
}
</script>