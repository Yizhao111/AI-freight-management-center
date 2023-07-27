<template>
  <div>
    <x-form ref="xForm" v-model="formData" :config="formConfig">
      <template #documentation>
        <el-popover
          placement="bottom-start"
          width="400"
          trigger="click"
          @show="$refs['iconSelect'].reset()"
        >
          <FlowIconSelect ref="iconSelect" @selected="selected" />
          <el-input
            slot="reference"
            v-model="formData.documentation"
            placeholder="点击选择图标"
            readonly
          >
            <svg-icon
              v-if="formData.documentation"
              slot="prefix"
              :icon-class="formData.documentation"
              class="el-input__icon"
              style="height: 32px; width: 16px"
            />
            <i v-else slot="prefix" class="el-icon-search el-input__icon" />
          </el-input>
        </el-popover>
      </template>
      <template #executionListener>
        <el-badge :value="executionListenerLength">
          <el-button
            size="small"
            @click="dialogName = 'executionListenerDialog'"
            >编辑</el-button
          >
        </el-badge>
      </template>
      <template #signal>
        <el-badge :value="signalLength">
          <el-button size="small" @click="dialogName = 'signalDialog'"
            >编辑</el-button
          >
        </el-badge>
      </template>
    </x-form>
    <executionListenerDialog
      v-if="dialogName === 'executionListenerDialog'"
      :element="element"
      :modeler="modeler"
      @close="finishExecutionListener"
    />
    <signalDialog
      v-if="dialogName === 'signalDialog'"
      :element="element"
      :modeler="modeler"
      @close="finishExecutionListener"
    />
  </div>
</template>

<script>
import mixinPanel from '../../common/mixinPanel'
import mixinExecutionListener from '../../common/mixinExecutionListener'
import signalDialog from './property/signal'
import FlowIconSelect from '@/components/FlowIconSelect'
import { commonParse } from '../../common/parseElement'
export default {
  components: {
    signalDialog,
    FlowIconSelect,
  },
  mixins: [mixinPanel, mixinExecutionListener],
  data() {
    return {
      signalLength: 0,
      formData: {},
    }
  },
  computed: {
    formConfig() {
      const _this = this
      return {
        inline: false,
        item: [
          {
            xType: 'select',
            name: 'processCategory',
            label: '流程分类',
            dic: { data: _this.categorys, label: 'name', value: 'id' },
          },
          {
            xType: 'input',
            name: 'id',
            label: '流程标识key',
            rules: [{ required: true, message: 'Id 不能为空' }],
          },
          {
            xType: 'input',
            name: 'name',
            label: '流程名称',
          },
          {
            xType: 'slot',
            name: 'documentation',
            label: '流程图标',
          },
          {
            xType: 'slot',
            name: 'executionListener',
            label: '执行监听器',
          },
          {
            xType: 'slot',
            name: 'signal',
            label: '信号定义',
          },
        ],
      }
    },
  },
  watch: {
    'formData.processCategory': function (val) {
      if (val === '') val = null
      this.updateProperties({ 'flowable:processCategory': val })
    },
    categorys: function (val) {
      if (!this.$route.query.deployId) {
        this.formData.processCategory=val[0].id
      }
    },
  },
  created() {
    this.formData = commonParse(this.element)
  },
  mounted() {},
  methods: {
    selected(name) {
      this.formData.documentation = name
    },
    computedSignalLength() {
      this.signalLength =
        this.element.businessObject.extensionElements?.values?.length ?? 0
    },
    finishSignal() {
      if (this.dialogName === 'signalDialog') {
        this.computedSignalLength()
      }
      this.dialogName = ''
    },
  },
}
</script>

<style>
</style>
