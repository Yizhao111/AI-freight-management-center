<template>
    <div class="login">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
            <h3 class="title">PTW-TMS-注册</h3>
            <el-form-item prop="roleId">
                <el-select v-model="loginForm.roleId" placeholder="请选择要注册的用户" style="width: 100%">
                    <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId">
                    </el-option>
                    <template slot="prefix">
                        <svg-icon slot="prefix" icon-class="drag" class="el-input__icon input-icon" />
                    </template>
                </el-select>
            </el-form-item>
            <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="nickName">
                <el-input v-model="loginForm.nickName" type="text" auto-complete="off" placeholder="姓名">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="mobile">
                <el-input v-model="loginForm.mobile" type="text" auto-complete="off" placeholder="手机号">
                    <svg-icon slot="prefix" icon-class="phone" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="email">
                <el-input v-model="loginForm.email" type="text" auto-complete="off" placeholder="邮箱">
                    <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>

            <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
                    @keyup.enter.native="handleLogin">
                    <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>

            <el-form-item style="width:100%;">
                <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
                    @click.native.prevent="handleLogin">
                    <span v-if="!loading">注 册</span>
                    <span v-else>注 册 中...</span>
                </el-button>
            </el-form-item>
        </el-form>
        <!--  底部  -->
        <div class="el-login-footer">
            <span>Copyright © 2023 PTW-TMS All Rights Reserved.</span>
        </div>
    </div>
</template>
  
<script>
import { createUniqueString } from '@/utils/penint'
import { selectRole } from "@/api/common";
import axios from 'axios'

export default {
    name: "Login",
    data() {
        return {
            roleOptions: [],
            captchaUrl: `${process.env.VUE_APP_BASE_API}/auth/captcha`,
            randomId: createUniqueString(),
            codeUrl: "",
            cookiePassword: "",
            loginForm: {
                username: "",
                password: "",
                mobile: "",
                email: "",
                nickName: ''
            },
            loginRules: {
                roleId: [
                    { required: true, trigger: "change", message: "请选择要注册的角色" }
                ],
                username: [
                    { required: true, trigger: "blur", message: "用户名不能为空" }
                ],
                nickName: [
                    { required: true, trigger: "blur", message: "姓名不能为空" }
                ],
                mobile: [
                    { required: true, trigger: "blur", message: "手机号不能为空" }
                ],
                email: [
                    { required: true, trigger: "blur", message: "邮箱不能为空" }
                ],
                password: [
                    { required: true, trigger: "blur", message: "密码不能为空" }
                ],
                code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
            },
            loading: false,
            redirect: undefined
        };
    },
    watch: {
        $route: {
            handler: function (route) {
                this.redirect = route.query && route.query.redirect;
            },
            immediate: true
        }
    },
    created() {
        this._selectRole()
    },
    methods: {
        _selectRole() {
            axios({
                method: 'GET',
                url: `${process.env.VUE_APP_BASE_API}/system/common/selectRole`,
            }).then((res) => {
                this.roleOptions = res.data.data
            }).catch((e) => {

            })

        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    this.$store
                        .dispatch("Register", this.loginForm)
                        .then((res) => {
                            if (res.status == 200) {
                                this.msgSuccess("注册成功，3秒后跳转登录页");
                                setTimeout(() => {
                                    this.$router.push({ path: this.redirect || "/login" });
                                }, 3000);
                            }
                        })
                        .catch(() => {
                            this.loading = false;
                            this.getCode();
                        });
                }
            });
        }
    }
};
</script>
  
<style rel="stylesheet/scss" lang="scss">
.login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image: url("../assets/image/login-background.jpg");
    background-size: cover;
}

.title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
}

.login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    transform: translateX(-600px);
    padding: 25px 25px 5px 25px;

    .el-input {
        height: 38px;

        input {
            height: 38px;
        }
    }

    .input-icon {
        height: 39px;
        width: 14px;
        margin-left: 2px;
    }
}

.login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
}

.login-code {
    width: 33%;
    height: 38px;
    float: right;

    img {
        cursor: pointer;
        vertical-align: middle;
    }
}

.el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
}
</style>
  