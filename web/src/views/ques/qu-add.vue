<template>
  <div class="app-container">
    <el-form
      ref="postForm"
      :model="postForm"
      :rules="rules"
      label-position="left"
      label-width="150px"
    >
      <el-card>
        <el-form-item label="题目类型 " prop="quType">
          <el-select
            v-model="postForm.quType"
            :disabled="quTypeDisabled"
            class="filter-item"
            @change="handleTypeChange"
          >
            <el-option
              v-for="item in quTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input v-model="postForm.content" type="textarea" />
        </el-form-item>
      </el-card>

      <div class="filter-container" style="margin-top: 25px">
        <el-button
          v-if="addFlag"
          class="filter-item"
          type="primary"
          icon="el-icon-plus"
          size="small"
          plain
          @click="handleAdd"
        >
          添加
        </el-button>

        <el-table
          :data="postForm.answerList"
          :border="true"
          style="width: 100%"
        >
          <el-table-column label="选项内容">
            <template slot-scope="scope">
              <el-input v-model="scope.row.content" type="textarea" />
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="100px"
            v-if="addFlag"
          >
            <template slot-scope="scope">
              <el-button
                v-if="addFlag"
                type="danger"
                icon="el-icon-delete"
                circle
                @click="removeItem(scope.$index)"
              />
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div style="margin-top: 20px">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="info" @click="onCancel">返回</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { addQu, getQu } from "@/api/ques/qu";

export default {
  name: "QuDetail",
  data() {
    return {
      quTypeDisabled: false,
      itemImage: true,

      levels: [
        { value: 1, label: "普通" },
        { value: 2, label: "较难" },
      ],
      repoSelectOptions: [],
      quTypes: [
        {
          value: 1,
          label: "单选题",
        },
        {
          value: 2,
          label: "多选题",
        },
        {
          value: 3,
          label: "判断题",
        },
      ],

      addFlag: true,
      role: undefined,
      postForm: {
        isRight: undefined,
        content: "",
        analysis: "",
        repoIds: [],
        answerList: [],
      },
      rules: {
        content: [{ required: true, message: "题目内容不能为空！" }],

        quType: [{ required: true, message: "题目类型不能为空！" }],

        level: [{ required: true, message: "必须选择难度等级！" }],

        repoIds: [{ required: true, message: "至少要选择一个题库！" }],
      },
    };
  },
  created() {
    const id = this.$route.query.id;
    this.role = this.$route.query.role;
    if (typeof id !== "undefined") {
      this.quTypeDisabled = true;
      this.fetchData(id);
    }
  },
  methods: {
    handleTypeChange(v) {
      this.postForm.answerList = [];
      if (v === 3) {
        this.addFlag = false;
        this.postForm.answerList.push({
          isRight: true,
          content: "正确",
          analysis: "",
        });
        this.postForm.answerList.push({
          isRight: false,
          content: "错误",
          analysis: "",
        });
      }

      if (v === 4) {
        this.addFlag = false;
        this.postForm.answerList.push({
          isRight: true,
          content: "",
          analysis: "",
        });
      }

      if (v === 1 || v === 2) {
        this.postForm.answerList.push({
          isRight: false,
          content: "",
          analysis: "",
        });
        this.postForm.answerList.push({
          isRight: false,
          content: "",
          analysis: "",
        });
        this.postForm.answerList.push({
          isRight: false,
          content: "",
          analysis: "",
        });
        this.postForm.answerList.push({
          isRight: false,
          content: "",
          analysis: "",
        });
      }
    },

    // 添加子项
    handleAdd() {
      this.postForm.answerList.push({
        isRight: false,
        content: "",
        analysis: "",
      });
    },

    removeItem(index) {
      this.postForm.answerList.splice(index, 1);
    },

    fetchData(id) {
      getQu(id).then((response) => {
        this.postForm = response.data;
        if (this.postForm.quType == 4) {
          this.addFlag = false;
        }
      });
    },
    submitForm() {
      // let rightCount = 0;
      // this.postForm.answerList.forEach(function (item) {
      //   if (item.isRight) {
      //     rightCount += 1;
      //   }
      // });
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return;
        }

        addQu(this.postForm).then((response) => {
          this.postForm = response.data;
          this.$notify({
            title: "成功",
            message: "试题保存成功！",
            type: "success",
            duration: 2000,
          });

          if (this.role && this.role === "admin") {
            this.$router.push("/ques/qu-admin");
            return;
          }
          this.$router.push("/ques/qu-admin");
        });
      });
    },
    onCancel() {
      if (this.role && this.role === "admin") {
        this.$router.push("/ques/qu-admin");
        return;
      }
      this.$router.push("/ques/qu-admin");
    },
  },
};
</script>

<style scoped>
</style>

