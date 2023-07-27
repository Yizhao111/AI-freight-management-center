<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="订单号" prop="orderNo">
                <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable size="small"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- <Invoice></Invoice> -->
        <el-table v-loading="loading" :data="financeList">
            <el-table-column label="序号" align="center" prop="id" width="80">
                <template slot-scope="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column label="订单号" align="center" prop="orderNo" />
            <el-table-column label="类型" align="center" prop="type">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.type == 1">收入</el-tag>
                    <el-tag v-if="scope.row.type == 2" type="danger">支出</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="金额" align="center" prop="amount" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
    </div>
</template>
  
<script>
import { listFinance } from "@/api/finance";
import Invoice from '@/components/Invoice/index'

export default {
    name: "Finance",
    components: { Invoice },
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
            // 财务表格数据
            financeList: [],
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
                orderNo: undefined,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询财务列表 */
        getList() {
            this.loading = true;
            listFinance(this.queryParams).then(response => {
                this.financeList = response.data.records;
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
                orderNo: undefined,
                type: undefined,
                amount: undefined,
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
        }
    }
}
</script>