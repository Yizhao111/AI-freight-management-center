<template>
    <div class="admin-home">
        <div id="map" style="width: 100%;height:100vh;"></div>
    </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader';
import { getLnglat } from '@/api/orders/index'
export default {
    data() {
        return {
            map: null,
            points: []
        };
    },
    methods: {
        _getLnglat() {
            getLnglat().then(res => {
                this.points = res.data
                //添加聚合组件
                this.map.plugin(["AMap.MarkerClusterer"], () => {
                    new AMap.MarkerClusterer(
                        this.map,     // 地图实例
                        this.points, // 海量点数据，数据中需包含经纬度信息字段 lnglat
                    )
                });
            })
        },
        initMarker() {
            // var points = [
            //     { weight: 1, lnglat: ["108.939621", "34.343147"] },
            //     { weight: 1, lnglat: ["108.939621", "34.343147"] },
            //     { weight: 1, lnglat: ["108.939621", "34.343147"] },
            //     { weight: 1, lnglat: ["108.939621", "34.343147"] },
            //     { weight: 1, lnglat: ["108.939621", "34.343147"] },
            //     { weight: 1, lnglat: ["108.939621", "34.343147"] },
            //     { weight: 1, lnglat: ["113.370643", "22.938827"] },
            //     { weight: 1, lnglat: ["112.985037", "23.15046"] },
            //     { weight: 1, lnglat: ["112.985037", "23.15046"] },
            //     { weight: 1, lnglat: ["112.985037", "23.15046"] },
            //     { weight: 1, lnglat: ["112.985037", "23.15046"] },
            //     { weight: 1, lnglat: ["110.361899", "20.026695"] },
            //     { weight: 1, lnglat: ["110.361899", "20.026695"] },
            //     { weight: 1, lnglat: ["110.361899", "20.026695"] },
            //     { weight: 1, lnglat: ["110.361899", "20.026695"] },
            //     { weight: 1, lnglat: ["110.361899", "20.026695"] },
            //     { weight: 1, lnglat: ["121.434529", "31.215641"] },
            // ];
            // //添加聚合组件
            // this.map.plugin(["AMap.MarkerClusterer"], () => {
            //     new AMap.MarkerClusterer(
            //         this.map,     // 地图实例
            //         points, // 海量点数据，数据中需包含经纬度信息字段 lnglat
            //     )
            // });
        },
        initMap() {
            AMapLoader.load({
                key: "c016beab00d7325d1823f09a22739785",             // 申请好的Web端开发者Key，首次调用 load 时必填
                version: "2.0",      // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
                plugins: ['AMap.ToolBar', 'AMap.Driving', 'AMap.MarkerClusterer'],
                // 需要使用的的插件列表，如比例尺'AMap.Scale'等
            }).then((AMap) => {
                this.map = new AMap.Map("map", {  //设置地图容器id
                    zoom: 6,           //初始化地图级别
                    center: [105.602725, 37.076636], //初始化地图中心点位置
                });
                this._getLnglat()
            }).catch(e => {
                console.log(e);
            })

        }

    },
    created() {
        this.initMap();
    },


};
</script>


<style lang="scss" scoped>
.admin-home {
    color: #444444;



}
</style>

