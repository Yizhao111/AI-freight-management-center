<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
            <el-form-item label="商品名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入商品名称" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="商品所属类目" prop="categoryId">
                <el-cascader v-model="categoryId" :options="categoryOptions" :props="{ value: 'id', label: 'name' }"
                    style="width:100%" @change="handleChange"></el-cascader>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['busi:shopGoods:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['busi:shopGoods:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['busi:shopGoods:remove']">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="shopGoodsList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="序号" align="center" prop="id" width="80">
                <template slot-scope="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="商品编号" align="center" prop="goodsSn" :show-overflow-tooltip="true"/>
            <el-table-column label="商品名称" align="center" prop="name" :show-overflow-tooltip="true" />
            <el-table-column label="商品单价" align="center" prop="price" :show-overflow-tooltip="true" />
            <el-table-column label="商品所属类目" align="center" prop="categorySource" :formatter="categoryFormat" />
            <el-table-column label="商品简介" align="center" prop="brief" :show-overflow-tooltip="true" />
            <el-table-column label="是否上架" align="center" prop="isOnSale">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.isOnSale">是</el-tag>
                    <el-tag v-if="!scope.row.isOnSale" type="danger">否</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="商品单位" align="center" prop="unit" />
            <el-table-column label="库存" align="center" prop="sku" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-view" size="mini" @click="handleView(scope.row)"
                        v-hasPermi="['busi:shopGoods:query']">详情</el-button>
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['busi:shopGoods:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['busi:shopGoods:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改商品对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="130px">
                <el-form-item label="商品名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入商品名称" maxlength="50" show-word-limit />
                </el-form-item>
                <el-form-item label="商品单价" prop="price">
                    <el-input-number v-model="form.price" :precision="2" :step="0.1" :min="0.00" :max="99999999"></el-input-number>
                </el-form-item>
                <el-form-item label="商品所属类目" prop="categoryId">
                    <el-cascader v-model="categoryId2" :options="categoryOptions" :props="{ value: 'id', label: 'name' }"
                        style="width:100%" @change="handleChange"></el-cascader>
                </el-form-item>
                <el-form-item label="商品简介" prop="brief">
                    <el-input v-model="form.brief" placeholder="请输入商品简介" maxlength="100" show-word-limit />
                </el-form-item>
                <el-form-item label="是否上架" prop="isOnSale">
                    <el-radio-group v-model="form.isOnSale">
                        <el-radio-button :label="true">是</el-radio-button>
                        <el-radio-button :label="false">否</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="商品单位" prop="unit">
                    <el-input v-model="form.unit" placeholder="请输入商品单位，例如件、盒" maxlength="10" show-word-limit />
                </el-form-item>
                <el-form-item label="库存" prop="sku">
                    <el-input v-model="form.sku" placeholder="请输入库存" type="number" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 查看详情对话框 -->
        <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
            <el-descriptions title="" direction="vertical" :column="3" border v-if="openView">
                <el-descriptions-item label="主键自增ID" :span="1">{{ form.id }}</el-descriptions-item>
                <el-descriptions-item label="商品编号" :span="1">{{ form.goodsSn }}</el-descriptions-item>
                <el-descriptions-item label="商品名称" :span="1">{{ form.name }}</el-descriptions-item>
                <el-descriptions-item label="单价" :span="1">{{ form.price }}</el-descriptions-item>
                <el-descriptions-item label="商品所属类目" :span="1">{{ categoryFormat(form) |
                    categoryFilter }}</el-descriptions-item>
                <el-descriptions-item label="商品简介" :span="1">{{ form.brief }}</el-descriptions-item>
                <el-descriptions-item label="是否上架" :span="1">{{ form.isOnSale ? '是' : '否' }}</el-descriptions-item>
                <el-descriptions-item label="商品单位" :span="1">{{ form.unit }}</el-descriptions-item>
                <el-descriptions-item label="库存" :span="1">{{ form.sku }}</el-descriptions-item>
            </el-descriptions>

        </el-dialog>

    </div>
</template>
  
<script>
import { listShopGoods, getShopGoods, delShopGoods, addShopGoods, updateShopGoods } from "@/api/goods/index";
import {
    optionsCategory
} from "@/api/goods/category";
export default {
    name: "ShopGoods",
    data() {
        return {
            categoryOptions: [],
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
            // 商品表格数据
            shopGoodsList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否显示详情
            openView: false,
            categoryId: null,
            categoryId2: null,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                name: undefined,
                categoryId: undefined,
                isOnSale: undefined,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                goodsSn: [
                    { required: true, message: "商品编号不能为空", trigger: "blur" }
                ],
                price: [
                    { required: true, message: "商品单价不能为空", trigger: "blur" }
                ],
                name: [
                    { required: true, message: "商品名称不能为空", trigger: "blur" }
                ],
                categoryId: [
                    { required: true, message: "商品所属类目ID不能为空", trigger: "blur" }
                ],
                brief: [
                    { required: true, message: "商品简介不能为空", trigger: "blur" }
                ],
                isOnSale: [
                    { required: true, message: "是否上架不能为空", trigger: "blur" }
                ],
                unit: [
                    { required: true, message: "商品单位，例如件、盒，默认为件不能为空", trigger: "blur" }
                ],
                sku: [
                    { required: true, message: "库存不能为空", trigger: "blur" }
                ],
            }
        };
    },
    
    created() {
        this.getList();
        this._optionsCategory()
    },
    methods: {
        categoryFormat(row) {
            const checkedNodes = JSON.parse(row.categorySource)
            const firstLevelItem = this.categoryOptions.find(item => item.id === checkedNodes[0])
            if (firstLevelItem) {
                const secondLevelItem = firstLevelItem.children.find(item => item.id === checkedNodes[1])
                if (secondLevelItem) {
                    return `${firstLevelItem.name}/${secondLevelItem.name}`
                }
            }

        },
        handleChange(e) {
            this.queryParams.categoryId = e[e.length - 1]
        },
        handleChange(e) {
            this.form.categoryId = e[e.length - 1]
            this.form.categorySource = JSON.stringify(e)
        },
        _optionsCategory() {
            optionsCategory().then(res => {
                this.categoryOptions = res.data
            })
        },
        /** 查询商品列表 */
        getList() {
            this.loading = true;
            listShopGoods(this.queryParams).then(response => {
                this.shopGoodsList = response.data.records;
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
            this.categoryId = null
            this.categoryId2 = null
            this.form = {
                id: undefined,
                goodsSn: undefined,
                name: undefined,
                categoryId: undefined,
                brief: undefined,
                isOnSale: undefined,
                picUrl: undefined,
                unit: undefined,
                sku: undefined,
                detail: undefined,
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
            this.categoryId = null
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
            this.title = "添加商品";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getShopGoods(id).then(response => {
                this.form = response.data;
                this.categoryId2 = JSON.parse(this.form.categorySource)
                this.open = true;
                this.title = "修改商品";
            });
        },
        /** 详情按钮操作 */
        handleView(row) {
            this.reset();
            const id = row.id || this.ids
            getShopGoods(id).then(response => {
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
                        updateShopGoods(this.form).then(response => {
                            if (response.status === 200) {
                                this.msgSuccess("修改成功");
                                this.open = false;
                                this.getList();
                            } else {
                                this.msgError(response.msg);
                            }
                        });
                    } else {
                        addShopGoods(this.form).then(response => {
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
            this.$confirm('是否确认删除商品编号为"' + ids + '"的数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function () {
                return delShopGoods(ids);
            }).then(() => {
                this.getList();
                this.msgSuccess("删除成功");
            }).catch(function () { });
        },
    }
}
</script>