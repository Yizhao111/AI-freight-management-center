<script>
import { getToken } from '@/utils/auth'
import { deepClone } from '@/utils/index'
import render from '@/components/render/render.js'

const ruleTrigger = {
  'el-input': 'blur',
  quill: 'blur',
  'el-input-number': 'blur',
  'el-select': 'change',
  'el-radio-group': 'change',
  'el-checkbox-group': 'change',
  'el-cascader': 'change',
  'el-time-picker': 'change',
  'el-date-picker': 'change',
  'el-rate': 'change',
  'el-upload': 'blur',
}

const layouts = {
  colFormItem(h, scheme) {
    const config = scheme.__config__
    const listeners = buildListeners.call(this, scheme)

    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null
    if (config.showLabel === false) labelWidth = '0'
    return (
      <el-col span={config.span}>
        <el-form-item
          label-width={labelWidth}
          prop={scheme.__vModel__}
          label={config.showLabel ? config.label : ''}
        >
          <render conf={scheme} on={listeners} />
        </el-form-item>
      </el-col>
    )
  },
  rowFormItem(h, scheme) {
    let child = renderChildren.apply(this, arguments)
    if (scheme.type === 'flex') {
      child = (
        <el-row
          type={scheme.type}
          justify={scheme.justify}
          align={scheme.align}
        >
          {child}
        </el-row>
      )
    }
    return (
      <el-col span={scheme.span}>
        <el-row gutter={scheme.gutter}>{child}</el-row>
      </el-col>
    )
  },
}

function renderFrom(h) {
  const { formConfCopy } = this
  return (
    <el-row gutter={formConfCopy.gutter}>
      <el-form
        size={formConfCopy.size}
        label-position={formConfCopy.labelPosition}
        disabled={formConfCopy.disabled}
        label-width={`${formConfCopy.labelWidth}px`}
        ref={formConfCopy.formRef}
        // model不能直接赋值 https://github.com/vuejs/jsx/issues/49#issuecomment-472013664
        props={{ model: this[formConfCopy.formModel] }}
        rules={this[formConfCopy.formRules]}
      >
        {renderFormItem.call(this, h, formConfCopy.fields)}
        {formConfCopy.formBtns && formBtns.call(this, h)}
      </el-form>
    </el-row>
  )
}

function formBtns(h) {
  return (
    <el-col>
      <el-form-item size="large">
        <el-button type="primary" onClick={this.submitForm}>
          提交
        </el-button>
        <el-button onClick={this.resetForm}>重置</el-button>
      </el-form-item>
    </el-col>
  )
}

function renderFormItem(h, elementList) {
  if (!elementList) {
    return
  }
  return elementList.map((scheme) => {
    const config = scheme.__config__
    const layout = layouts[config.layout]

    if (layout) {
      return layout.call(this, h, scheme)
    }
    throw new Error(`没有与${config.layout}匹配的layout`)
  })
}

function renderChildren(h, scheme) {
  const config = scheme.__config__
  if (!Array.isArray(config.children)) return null
  return renderFormItem.call(this, h, config.children)
}

// event为当前的值
function setValue(event, config, scheme) {
  this.$set(config, 'defaultValue', event)
  this.$set(this[this.formConf.formModel], scheme.__vModel__, event)
}

function buildListeners(scheme) {
  const config = scheme.__config__
  const methods = this.formConf.__methods__ || {}
  const listeners = {}
  // 给__methods__中的方法绑定this和event
  Object.keys(methods).forEach((key) => {
    listeners[key] = (event) => methods[key].call(this, event)
  })
  // 响应 render.js 中的 vModel $emit('input', val)
  listeners.input = (event) => setValue.call(this, event, config, scheme)
  listeners.setUpload = (event, file, fileList) => {
    // setValue.call(this, event, config, scheme)
    setUpload.call(this, event, config, scheme, file, fileList)
  }
  listeners.deleteUpload = (file, fileList) => {
    deleteUpload.call(this, config, scheme, file, fileList)
  }
  listeners.setPreview = (file) => {
    if (isImageType(file.url)) {
      this.$alert(
        '<img src="' +
          file.url +
          '" style="width:90%;height:80vh;object-fit:contain;">',
        '',
        {
          dangerouslyUseHTMLString: true,
          customClass: 'previewImage',
          closeOnPressEscape: true,
          showConfirmButton: false,
          center: true,
        }
      )
    } else {
      window.open(file.url, '_blank')
    }
  }
  listeners.defaultValue = (event) => setValue.call(this, event, config, scheme)
  return listeners
}
function isImageType(url) {
  // toLowerCase() 将字符串转换为小写，返回一个新的字符串
  var index = url.lastIndexOf('.')
  var ext = url.substr(index + 1)
  return (
    ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(
      ext.toLowerCase()
    ) !== -1
  )
}
function setUpload(event, config, scheme, file, fileList) {
  var newValue = this[this.formConf.formModel][scheme.__vModel__]
  newValue.push({ name: file.name, url: event.data })
  this.$set(config, 'defaultValue', newValue)
  this.$set(this[this.formConf.formModel], scheme.__vModel__, newValue)
  if (scheme.hasOwnProperty('file-list')) {
    this.$set(scheme, 'file-list', newValue)
  }
  // if (scheme.multiple) {
  //   console.log(event.data)
  //   this.$set(config.defaultValue, config.defaultValue.length, event.data)
  //   this.$set(
  //     this[this.formConf.formModel],
  //     scheme.__vModel__,
  //     config.defaultValue
  //   )
  // } else {
  //   var newValue = []
  //   fileList.forEach((element) => {
  //     newValue.push({ name: element.name, url: element.url })
  //   })
  //   this.$set(config, 'defaultValue', JSON.stringify(newValue))
  //   this.$set(
  //     this[this.formConf.formModel],
  //     scheme.vModel,
  //     JSON.stringify(newValue)
  //   )
  // }
}
function deleteUpload(config, scheme, file, fileList) {
  var newValue = []
  fileList.forEach((element) => {
    newValue.push({ name: element.name, url: element.url })
  })
  this.$set(config, 'defaultValue', newValue)
  this.$set(this[this.formConf.formModel], scheme.__vModel__, newValue)
  if (scheme.hasOwnProperty('file-list')) {
    this.$set(scheme, 'file-list', newValue)
  }
}

export default {
  components: {
    render,
  },
  props: {
    formConf: {
      type: Object,
      required: true,
    },
  },
  data() {
    const data = {
      formConfCopy: deepClone(this.formConf),
      [this.formConf.formModel]: {},
      [this.formConf.formRules]: {},
    }
    this.initFormData(data.formConfCopy.fields, data[this.formConf.formModel])
    this.buildRules(data.formConfCopy.fields, data[this.formConf.formRules])
    return data
  },
  methods: {
    initFormData(componentList, formData) {
      if (!componentList) {
        return
      }
      componentList.forEach((cur) => {
        const config = cur.__config__
        if (cur.__config__.dataType == 'dynamicS') {
          const dictList = []
          this.getDicts(cur.action).then((res) => {
            res.data.forEach((dict) => {
              var value = dict.dictValue
              var label = dict.dictLabel
              var a = {}
              a.value = value
              a.label = label
              dictList.push(a)
            })
            cur.__slot__.options = dictList
          })
        }
        if (cur.action && config.tag == 'el-upload') {
          cur.headers.Authorization = getToken()
        }
        if (cur.__vModel__) formData[cur.__vModel__] = config.defaultValue
        if (config.children) this.initFormData(config.children, formData)
      })
    },
    buildRules(componentList, rules) {
      if (!componentList) {
        return
      }
      componentList.forEach((cur) => {
        const config = cur.__config__
        if (Array.isArray(config.regList)) {
          if (config.required) {
            const required = {
              required: config.required,
              message: cur.placeholder,
            }
            if (Array.isArray(config.defaultValue)) {
              required.type = 'array'
              required.message = `请至少选择一个${config.label}`
            }
            required.message === undefined &&
              (required.message = `${config.label}不能为空`)
            config.regList.push(required)
          }
          rules[cur.__vModel__] = config.regList.map((item) => {
            item.pattern && (item.pattern = eval(item.pattern))
            item.trigger = ruleTrigger && ruleTrigger[config.tag]
            return item
          })
        }
        if (config.children) this.buildRules(config.children, rules)
      })
    },
    resetForm() {
      this.formConfCopy = deepClone(this.formConf)
      this.formConfCopy.fields.forEach((cur) => {
        const config = cur.__config__
        if (config.dataType == 'dynamicS') {
          // 字典值
          const dictList = []
          this.getDicts(cur.action).then((res) => {
            res.data.forEach((dict) => {
              var value = dict.dictValue
              var label = dict.dictLabel
              var a = {}
              a.value = value
              a.label = label
              dictList.push(a)
            })

            cur.__slot__.options = dictList
          })
        }

        if (cur.action && config.tag == 'el-upload') {
          cur.headers.Authorization = getToken()
        }
      })
      this.$refs[this.formConf.formRef].resetFields()
    },
    submitForm() {
      // console.log(this.formConfCopy,'formConfCopy')
      // console.log(this.formConf,'formConf')
      this.$refs[this.formConf.formRef].validate((valid) => {
        if (!valid) return false
        // 传值给父组件
        // console.log(this[this.formConf.formModel],'表单拿到的值')
        let formConf = this.formConfCopy
        formConf.formBtns = false
        formConf.disabled = true
        formConf.fields.forEach((item) => {
          if (
            item.__config__.tag == 'el-upload' &&
            item.__config__.defaultValue.length > 0
          ) {
            item['file-list'] = item.__config__.defaultValue
          }
        })
        this.$emit('getFormData', this[this.formConf.formModel], formConf)
        // this.$emit('formConf', this.formConfCopy)
        return true
      })
    },
    getFileName(name) {
      if (name.lastIndexOf('/') > -1) {
        return name.slice(name.lastIndexOf('/') + 1).toLowerCase()
      } else {
        return ''
      }
    },
  },
  render(h) {
    return renderFrom.call(this, h)
  },
}
</script>
