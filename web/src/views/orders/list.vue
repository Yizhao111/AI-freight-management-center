<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="订单编号" prop="orderSn">
                <el-input v-model="queryParams.orderSn" placeholder="请输入订单编号" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['busi:orders:remove']">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="ordersList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="序号" align="center" prop="id" width="80">
                <template slot-scope="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="订单编号" align="center" prop="orderSn" :show-overflow-tooltip="true" />
            <el-table-column label="送货日期" align="center" prop="deliveryDate" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.deliveryDate, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="接收人" align="center" prop="recipientName" />
            <el-table-column label="电话" align="center" prop="phoneNumber" />
            <el-table-column label="邮编" align="center" prop="postalCode" />
            <el-table-column label="送货地址" align="center" prop="deliveryAddress" :show-overflow-tooltip="true" />
            <el-table-column label="货物状态" align="center" prop="status" :formatter="formatStatus" />
            <el-table-column label="订货时间" align="center" prop="createTime" width="180">
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-view" size="mini" @click="handleView(scope.row)"
                        v-hasPermi="['busi:orders:query']">详情</el-button>
                    <el-button size="mini" type="text" icon="el-icon-check" v-if="scope.row.status == '2'"
                        @click="handleUpdate(scope.row)" v-hasPermi="['busi:orders:edit']">完成录入</el-button>
                    <el-button size="mini" type="text" icon="el-icon-close" v-if="scope.row.status == '3'"
                        @click="handleUpdate1(scope.row)" v-hasPermi="['busi:orders:edit']">退货</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 查看详情对话框 -->
        <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
            <el-descriptions title="" direction="vertical" :column="4" border>
                <el-descriptions-item label="订单ID" :span="2">{{ form.orderId }}</el-descriptions-item>
                <el-descriptions-item label="订单编号" :span="2">{{ form.orderSn }}</el-descriptions-item>
                <el-descriptions-item label="送货地址" :span="2">{{ form.deliveryAddress }}</el-descriptions-item>
                <el-descriptions-item label="接收人" :span="1">{{ form.recipientName }}</el-descriptions-item>
                <el-descriptions-item label="电话" :span="1">{{ form.phoneNumber }}</el-descriptions-item>
                <el-descriptions-item label="邮编" :span="1">{{ form.postalCode }}</el-descriptions-item>
                <el-descriptions-item label="总额" :span="1">{{ form.totalPrice }}</el-descriptions-item>
                <el-descriptions-item label="送货日期" :span="1">{{ form.deliveryDate }}</el-descriptions-item>
                <el-descriptions-item label="货物状态" :span="1">{{ formatStatus(form) }}</el-descriptions-item>

                <el-descriptions-item label="商品详情" :span="4">
                    <el-table :data="form.goodsList" style="width: 100%">
                        <el-table-column prop="goodsName" label="商品名称" :show-overflow-tooltip="true"></el-table-column>
                        <el-table-column prop="number" label="订购数量"></el-table-column>
                        <el-table-column prop="price" label="单价"></el-table-column>
                    </el-table>
                </el-descriptions-item>
                <el-descriptions-item label="备注信息" :span="4">{{ form.notes }}</el-descriptions-item>

            </el-descriptions>

        </el-dialog>

    </div>
</template>
  
<script>
import { listUserAll } from '@/api/customUser'
import { listOrders, getOrders, delOrders, addOrders, updateOrders } from "@/api/orders/index";
import { getShopGoodsAll } from "@/api/goods/index";
import { listCarAll } from "@/api/car";
import SelectMap from '@/components/SelectMap/index'

export default {
    name: "Orders",
    components: {
        SelectMap
    },
    data() {
        return {
            carId: null,
            carOptions: [],
            customOptions: [],
            tableData: [
            ],
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
            // 订单表格数据
            ordersList: [],
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
                orderSn: undefined,
            },
            // 表单参数
            form: {
                goodsList: []
            },
            // 表单校验
            rules: {
                orderSn: [
                    { required: true, message: "订单编号不能为空", trigger: "blur" }
                ],
                notes: [
                    { required: true, message: "备注信息不能为空", trigger: "blur" }
                ],
                deliveryDate: [
                    { required: true, message: "送货日期不能为空", trigger: "blur" }
                ],
                orderType: [
                    { required: true, message: "订单类型不能为空", trigger: "blur" }
                ],
                deliveryAddress: [
                    { required: true, message: "送货地址不能为空", trigger: "blur" }
                ],
                customUserId: [
                    { required: true, message: "请选择客户", trigger: "change" }
                ],
                phoneNumber: [
                    { required: true, message: "电话不能为空", trigger: "blur" }
                ],
                postalCode: [
                    { required: true, message: "邮编不能为空", trigger: "blur" }
                ],
                status: [
                    { required: true, message: "货物状态不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        formatStatus(row) {
            var str = ''
            switch (row.status) {
                case '0':
                    str = '已订货'
                    break
                case '1':
                    str = '已确认送货'
                    break
                case '2':
                    str = '已领货配送'
                    break
                case '3':
                    str = '订单已完成'
                    break
                case '4':
                    str = '订单已退货'
                    break
            }
            return str;
        },
        _listCarAll() {
            listCarAll().then(res => {
                this.carOptions = res.data
            })
        },
        _listUserAll() {
            listUserAll().then(res => {
                this.customOptions = res.data
            })
        },
        handleValueChange(row) {
            var data = {
                goodsId: row.id,
                number: row.value
            }
            this.form.goodsList.push(data)
        },
        _getShopGoodsAll() {
            getShopGoodsAll().then(res => {
                this.tableData = res.data
            })
        },
        getMapForm(e) {
            this.form.deliveryAddress = e.address
            this.form.lat = e.lat
            this.form.lng = e.lng
        },
        /** 查询订单列表 */
        getList() {
            this.loading = true;
            listOrders(this.queryParams).then(response => {
                this.ordersList = response.data.records;
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
                goodsList: [],
                orderId: undefined,
                orderSn: undefined,
                totalPrice: undefined,
                productDescription: undefined,
                notes: undefined,
                deliveryDate: undefined,
                orderType: undefined,
                deliveryAddress: undefined,
                recipientName: undefined,
                customUserId: undefined,
                phoneNumber: undefined,
                postalCode: undefined,
                status: "0",
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
            this.ids = selection.map(item => item.orderId)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this._getShopGoodsAll()
            this._listUserAll()
            this.reset();
            this.open = true;
            this.title = "添加订单";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.$confirm('是否确认订单号为"' + row.orderSn + '"的订单已完成?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function () {
                var formdata = {
                    orderId: row.orderId,
                    status: '3'
                }
                updateOrders(formdata)
            }).then(() => {
                this.msgSuccess("订单完成");
                this.getList();
            }).catch(function () {
                this.getList();
             });

        },
        handleUpdate1(row) {
            this.$confirm('是否确认订单号为"' + row.orderSn + '"的订单办理退货?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function () {
                var formdata = {
                    orderId: row.orderId,
                    status: '4'
                }
                updateOrders(formdata)
            }).then(() => {
                this.getList();
                this.msgSuccess("已退货");
            }).catch(function () { });

        },
        /** 详情按钮操作 */
        handleView(row) {
            this.reset();
            const orderId = row.orderId || this.ids
            getOrders(orderId).then(response => {
                this.form = response.data;
                this.openView = true;
                this.title = "查看详情";
            });
        },

        /** 删除按钮操作 */
        handleDelete(row) {
            const orderIds = row.orderId || this.ids;
            this.$confirm('是否确认删除订单编号为"' + orderIds + '"的数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function () {
                return delOrders(orderIds);
            }).then(() => {
                this.getList();
                this.msgSuccess("删除成功");
            }).catch(function () { });
        },
    }
}
</script>