<template>
    <div style="display: flex">
        <div>
            <div>
                <el-select v-model="keywords" filterable remote reserve-keyword placeholder="请输入关键词"
                    :remote-method="remoteMethod" :loading="loading" :clearable="true" size="mini" @change="currentSelect"
                    style="width: 500px">
                    <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item" class="one-text">
                        <span style="float: left">{{ item.name }}</span>
                        <span style="float: right; color: #8492a6; font-size: 13px">{{
                            item.district
                        }}</span>
                    </el-option>
                </el-select>
            </div>
            <div id="container" class="container"></div>
        </div>
        <!-- <div class="info-box">
            纬度：{{ form.lat }}
            <br />
            经度：{{ form.lng }}
            <p>详细地址：{{ form.address }}</p>
        </div> -->
    </div>
</template>
   
<script>
import AMapLoader from "@amap/amap-jsapi-loader";
window._AMapSecurityConfig = {
    // 安全密钥
    securityJsCode: "475dd39759812a08e19cfe67a745252e",
};
export default {
    name: "TestIndex",
    data() {
        return {
            key: 'c016beab00d7325d1823f09a22739785',
            // 地图实例
            map: null,
            // 标记点
            marker: "",
            // 地址逆解析
            geoCoder: null,
            // 搜索提示
            AutoComplete: null,
            // 搜索关键字
            keywords: "",
            // 位置信息
            form: {
                lng: "",
                lat: "",
                address: "",
                adcode: "", //地区编码
            },
            // 搜索节流阀
            loading: false,
            // 搜索提示信息
            options: [],
        };
    },
    methods: {
        initMap() {
            AMapLoader.load({
                // 你申请的Key
                key: this.key,
                version: "2.0",
                // 需要用到的插件
                plugins: ["AMap.Geocoder", "AMap.AutoComplete"],
            })
                .then((AMap) => {
                    this.map = new AMap.Map("container", {
                        viewMode: "3D", //是否为3D地图模式
                        zoom: 5, //初始化地图级别
                        center: [105.602725, 37.076636], //初始化地图中心点位置
                    });
                    //地址逆解析插件
                    this.geoCoder = new AMap.Geocoder({
                        city: "010", //城市设为北京，默认：“全国”
                        radius: 1000, //范围，默认：500
                    });
                    // 搜索提示插件
                    this.AutoComplete = new AMap.AutoComplete({ city: "全国" });
                    //点击获取经纬度;
                    this.map.on("click", (e) => {
                        // 获取经纬度
                        this.form.lng = e.lnglat.lng;
                        this.form.lat = e.lnglat.lat;
                        // 清除点
                        this.removeMarker();
                        // 标记点
                        this.setMapMarker();
                        // 移动地图视图到选中的位置
                        this.map.setCenter([this.form.lng, this.form.lat]);
                        const lnglat = [this.form.lng, this.form.lat];
                        AMapLoader.load({
                            key: this.key, // 你申请的Key
                            version: "2.0", // 版本号
                            plugins: ["AMap.Geocoder"], // 使用的插件
                        }).then((AMap) => {
                            const geocoder = new AMap.Geocoder();
                            geocoder.getAddress(lnglat, (status, result) => {
                                if (status === "complete" && result.regeocode) {
                                    this.form.address = result.regeocode.formattedAddress;
                                }
                            });
                        });
                        this.$emit("mapForm", this.form)
                    });
                })
                .catch((err) => {
                    // 错误
                    console.log(err);
                });
        },
        // 标记点
        setMapMarker() {
            // 自动适应显示想显示的范围区域
            this.map.setFitView();
            this.marker = new AMap.Marker({
                map: this.map,
                position: [this.form.lng, this.form.lat],
            });
            // 逆解析地址
            this.toGeoCoder();
            this.map.add(this.marker);
        },
        // 清除点
        removeMarker() {
            if (this.marker) {
                this.map.remove(this.marker);
            }
        },
        // 逆解析地址
        toGeoCoder() {
            let lnglat = [this.form.lng, this.form.lat];
            this.geoCoder.getAddress(lnglat, (status, result) => {
                if (status === "complete" && result.regeocode) {
                    this.form.address = result.regeocode.formattedAddress;
                }
            });
        },
        // 搜索
        remoteMethod(query) {
            if (query !== "") {
                this.loading = true;
                setTimeout(() => {
                    this.loading = false;
                    this.AutoComplete.search(query, (status, result) => {
                        this.options = result.tips;
                    });
                }, 200);
            } else {
                this.options = [];
            }
        },
        // 选中提示
        // currentSelect(val) {
        //     // 清空时不执行后面代码
        //     if (!val) {
        //         return;
        //     }
        //     this.form = {
        //         lng: val.location.lng,
        //         lat: val.location.lat,
        //         address: val.district + val.address,
        //         adcode: val.adcode,
        //     };
        //     // 清除点
        //     this.removeMarker();
        //     // 标记点
        //     this.setMapMarker();
        // },
        currentSelect(val) {
            // 清空时不执行后面代码
            if (!val) {
                return;
            }
            this.form = {
                lng: val.location.lng,
                lat: val.location.lat,
                address: val.district + val.address,
                adcode: val.adcode,
            };
            // 清除点
            this.removeMarker();
            // 标记点
            this.setMapMarker();
            // 移动地图视图到选中的位置
            this.map.setCenter([val.location.lng, val.location.lat]);
            // 在这里调用逆地理编码获取详细地址
            const lnglat = [val.location.lng, val.location.lat];
            AMapLoader.load({
                key: this.key, // 你申请的Key
                version: "2.0", // 版本号
                plugins: ["AMap.Geocoder"], // 使用的插件
            }).then((AMap) => {
                const geocoder = new AMap.Geocoder();
                geocoder.getAddress(lnglat, (status, result) => {
                    if (status === "complete" && result.regeocode) {
                        this.form.address = result.regeocode.formattedAddress;
                    }
                });
            });
            this.$emit("mapForm", this.form)
        },

    },
    mounted() {
        this.initMap();
        // // 获取初始地址
        // const initialLngLat = [this.form.lng, this.form.lat];
        // AMapLoader.load({
        //     key: this.key, // 你申请的Key
        //     version: "2.0", // 版本号
        //     plugins: ["AMap.Geocoder"], // 使用的插件
        // }).then((AMap) => {
        //     const geocoder = new AMap.Geocoder();
        //     geocoder.getAddress(initialLngLat, (status, result) => {
        //         if (status === "complete" && result.regeocode) {
        //             this.form.address = result.regeocode.formattedAddress;
        //             this.$emit("mapForm", this.form);
        //         }
        //     });
        // });
    },
};
</script>
   
<style>
.container {
    width: 500px;
    height: 300px;
}
</style>