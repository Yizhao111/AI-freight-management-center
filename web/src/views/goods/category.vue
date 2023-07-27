<template>
    <div class="app-container">
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
            <el-form-item label="类目编号" prop="name">
                <el-input v-model="queryParams.id" type="number" placeholder="请输入类目编号" clearable style="width: 240px"
                    size="small" @keyup.enter.native="handleQuery" />
            </el-form-item>

            <el-form-item label="类目名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入类目名称" clearable style="width: 240px" size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery"
                    v-hasPermi="['busi:shopCategory:list']">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-col :span="1.5">
                    <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleCreate"
                        v-hasPermi="['busi:shopCategory:add']">新增</el-button>
                </el-col>
            </el-col>
        </el-row>

        <!-- 查询结果 -->
        <el-table :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" fit highlight-current-row
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" row-key="id">
            <el-table-column align="center" label="类目ID" prop="id">
            </el-table-column>
            <el-table-column align="center" label="类目名" prop="name">
            </el-table-column>
            <el-table-column align="center" label="简介" prop="desc" :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['busi:shopCategory:edit']">编辑</el-button>
                    <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['busi:shopCategory:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 添加或修改对话框 -->
        <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
            <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px">
                <el-form-item label="类目名称" prop="name">
                    <el-input v-model="dataForm.name"></el-input>
                </el-form-item>
                <el-form-item label="级别" prop="level">
                    <el-select v-model="dataForm.level" @change="onLevelChange" style="width:100%">
                        <el-option label="一级类目" value="l1"> </el-option>
                        <el-option label="二级类目" value="l2"> </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="父类目" prop="pid" v-if="dataForm.level === 'l2'">
                    <el-select v-model="dataForm.pid" style="width:100%">
                        <el-option v-for="item in catL1" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="类目简介" prop="desc">
                    <el-input v-model="dataForm.desc"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button v-if="dialogStatus == 'create'" type="primary" @click="createData">确定</el-button>
                <el-button v-else type="primary" @click="updateData">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
  
<script>
import {
    listCategory,
    listL1,
    createCategory,
    updateCategory,
    deleteCategory,
} from "@/api/goods/category";

export default {
    name: "Category",
    computed: {},
    data() {
        return {
            list: undefined,
            total: 0,
            listLoading: true,
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                id: undefined,
                name: undefined,
            },
            catL1: {},
            dataForm: {
                id: undefined,
                name: "",
                level: "l2",
                pid: 1,
                desc: "",
                iconUrl: undefined,
                picUrl: "",
            },
            dialogFormVisible: false,
            dialogStatus: "",
            textMap: {
                update: "编辑",
                create: "创建",
            },
            rules: {
                name: [{ required: true, message: "类目名不能为空", trigger: "blur" }],
            },
            downloadLoading: false,
        };
    },
    created() {
        this.getList();
        this.getCatL1();
    },
    methods: {
        getList() {
            this.listLoading = true;
            listCategory(this.queryParams)
                .then((response) => {
                    this.list = response.data;
                    this.listLoading = false;
                })
                .catch(() => {
                    this.list = [];
                    this.total = 0;
                    this.listLoading = false;
                });
        },
        getCatL1() {
            listL1().then((response) => {
                this.catL1 = response.data;
            });
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.queryParams.id = undefined;
            this.handleQuery();
        },
        reset() {
            this.resetForm("dataForm");
            this.dataForm = {
                id: undefined,
                name: "",
                level: "l2",
                pid: 1,
                desc: "",
                iconUrl: undefined,
                picUrl: "",
            }
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        filterLevel: function (value, row) {
            return row.level === value;
        },
        onLevelChange: function (value) {
            this.pid = undefined;
            this.dataForm.pid = undefined;
        },
        handleCreate() {
            this.reset(); 
            this.dialogStatus = "create";
            this.dialogFormVisible = true;
            this.$nextTick(() => {
                this.$refs["dataForm"].clearValidate();
            });
            this.resetForm.pid = 1
        },
        uploadIconUrl: function (response) {
            this.dataForm.iconUrl = response.data.full;
        },
        uploadPicUrl: function (response) {
            this.dataForm.picUrl = response.data.full;
        },
        createData() {
            this.$refs["dataForm"].validate((valid) => {
                if (valid) {
                    createCategory(this.dataForm).then((response) => {
                        this.list.unshift(response.data);
                        this.dialogFormVisible = false;
                        this.getList();
                        this.$notify({
                            title: "成功",
                            message: "创建成功",
                            type: "success",
                            duration: 2000,
                        });
                    });
                }
            });
        },
        handleUpdate(row) {
            console.log(row);
            this.dataForm = row;
            this.dialogStatus = "update";
            this.dialogFormVisible = true;
            this.$nextTick(() => {
                this.$refs["dataForm"].clearValidate();
            });
        },
        updateData() {
            this.$refs["dataForm"].validate((valid) => {
                if (valid) {
                    updateCategory(this.dataForm).then(() => {
                        for (const v of this.list) {
                            if (v.id === this.dataForm.id) {
                                const index = this.list.indexOf(v);
                                this.list.splice(index, 1, this.dataForm);
                                break;
                            }
                        }
                        this.dialogFormVisible = false;
                        this.getList();
                        this.$notify({
                            title: "成功",
                            message: "更新成功",
                            type: "success",
                            duration: 2000,
                        });
                    });
                }
            });
        },
        handleDelete(row) {
            deleteCategory(row.id).then((response) => {
                this.$notify({
                    title: "成功",
                    message: "删除成功",
                    type: "success",
                    duration: 2000,
                });
                this.getList()
            });
        },
    },
};
</script>
<style scoped>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
}

.avatar {
    width: 120px;
    height: 120px;
    display: block;
}
</style>