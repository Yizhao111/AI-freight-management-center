var BASE_URL = `http://${location.host}/dev-api`

// 实时租户
$.ajax({
    type: 'GET',			//以post方法提交数据给服务器
    url: BASE_URL + "/busi/stat/listGoodsStat",				//提交数据到User
    dataType: "json",		//数据类型
    success: function (res) {			//回调函数
        var data = res.data
        var str = ''
        for (let index = 0; index < data.length; index++) {
            const element = data[index];
            str += `<li class='clearfix'> <span class='pulll_left' title='${element.name}'>${element.name}</span> <span class='pulll_right'>${element.createTime}</span> </li>`
        }
        document.getElementById("company").innerHTML = str
    }
});

// 实时订单
$.ajax({
    type: 'GET',			//以post方法提交数据给服务器
    url: BASE_URL + "/busi/stat/listOrderStat",				//提交数据到User
    dataType: "json",		//数据类型
    success: function (res) {			//回调函数
        var data = res.data
        var str = ''
        for (let index = 0; index < data.length; index++) {
            const element = data[index];
            // str += `<li class='clearfix'> <span class='pulll_left'>${element.companyName}</span> <span class='pulll_right'>到期时间：${element.activeTime}</span> </li>`

            str += ` <li>
        <p>${element.recipientName}-${element.orderSn}-${element.createTime}</p>
    </li>`
        }
        document.getElementById("ques").innerHTML = str
    }
});


// 实时问卷
$.ajax({
    type: 'GET',			//以post方法提交数据给服务器
    url: BASE_URL + "/busi/stat/count",				//提交数据到User
    dataType: "json",		//数据类型
    success: function (res) {			//回调函数
        var data = res.data
        /**
         * jrdt: 0
         * jrtk: 0
         * zrdt: 3
         * zrtk: 0
         */
        var d1 = data.carCount
        var d2 = data.customCount
        var d4 = data.goodsCount
        var d5 = data.orderCount
        document.getElementById("d1").innerText = d1
        document.getElementById("d2").innerText = d2
        document.getElementById("d4").innerText = d4
        document.getElementById("d5").innerText = d5


    }
});


