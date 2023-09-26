// import { HTTP_REQUEST_URL } from "../config/app";
// console.log(HTTP_REQUEST_URL, 777);
// import axios from 'axios';

/* ts 类型定义-Tips */
interface tipsOpt {
    title ?: string | null;
    icon ?: string;
    endtime ?: number;
    tab ?: number;
    url ?: string;
    success ?: any;
}
export default {
    /**
     * 时间戳转成时间格式
     */
    // format(data : number, isMore : string) {
    //     let date = new Date(data);
    //     let YYYY = date.getFullYear();
    //     let MM : number | string = date.getMonth() + 1;
    //     MM = MM < 10 ? "0" + MM : MM;
    //     let DD : number | string = date.getDate();
    //     DD = DD < 10 ? "0" + DD : DD;
    //     let hh : number | string = date.getHours();
    //     hh = hh < 10 ? "0" + hh : hh;
    //     let mm : number | string = date.getMinutes();
    //     mm = mm < 10 ? "0" + mm : mm;
    //     let ss : number | string = date.getSeconds();
    //     ss = ss < 10 ? "0" + ss : ss;
    //     if (isMore == "yyyy-MM-dd HH:mm:ss") {
    //         return YYYY + "-" + MM + "-" + DD + " " + hh + ":" + mm + ":" + ss;
    //     } else if (isMore == "yyyy-MM-dd") {
    //         return YYYY + "-" + MM + "-" + DD;
    //     } else if (isMore == "HH:mm:ss") {
    //         return hh + ":" + mm + ":" + ss;
    //     } else if (isMore == "HH") {
    //         return hh;
    //     } else if (isMore == "mm") {
    //         return mm;
    //     } else if (isMore == "ss") {
    //         return ss;
    //     }
    // },
    format(dateI : number | string | Date, fmt : string) {
        let date: Date;
        if (dateI instanceof Date) {
            date = dateI;
        } else if ((typeof dateI === "number") || (typeof dateI === "string")) {
            date = new Date(dateI);
        } else {
            return;
        }
        var o = {
        	"M+": date.getMonth() + 1, //月份
        	"d+": date.getDate(), //日
        	"h+": date.getHours(), //小时
        	"m+": date.getMinutes(), //分
        	"s+": date.getSeconds(), //秒
        	"q+": Math.floor((date.getMonth() + 3) / 3), //季度
        	"S": date.getMilliseconds() //毫秒
        };
        
        if (/(y+)/.test(fmt)) {
        	fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        
        for (var k in o) {
        	if (new RegExp("(" + k + ")").test(fmt)) {
        		fmt = fmt.replace(
        			RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        	}
        }
        
        return fmt;
    },
    /**
     * opt  object | string
     * to_url object | string
     * 例:
     * this.Tips('/pages/test/test'); 跳转不提示
     * this.Tips({title:'提示'},'/pages/test/test'); 提示并跳转
     * this.Tips({title:'提示'},{tab:1,url:'/pages/index/index'}); 提示并跳转值table上
     * tab=1 一定时间后跳转至 table上
     * tab=2 一定时间后跳转至非 table上
     * tab=3 一定时间后返回上页面
     * tab=4 关闭所有页面，打开到应用内的某个页面
     * tab=5 关闭当前页面，跳转到应用内的某个页面
     */
    Tips: function (opt : tipsOpt, to_url : tipsOpt | string) {
        let title : string = "", icon : string = "", endtime : number = 2000, success : any = null;
        if (typeof opt == "string") {
            to_url = opt;
            opt = null;
        }
        if (opt) {
            title = opt.title || "";
            icon = opt.icon || "none";
            endtime = opt.endtime || 2000;
            success = opt.success;
        }
        if (title)
            uni.showToast({
                title: title,
                icon: icon,
                mask: true,
                duration: endtime,
                success,
            });
        if (to_url !== "" && to_url != undefined) {
            if (typeof to_url == "object") {
                let tab = to_url.tab || 1,
                    url = to_url.url || "";
                switch (tab) {
                    case 1:
                        //一定时间后跳转至 table
                        setTimeout(function () {
                            uni.switchTab({
                                url: url,
                            });
                        }, endtime);
                        break;
                    case 2:
                        //跳转至非table页面
                        setTimeout(function () {
                            uni.navigateTo({
                                url: url,
                            });
                        }, endtime);
                        break;
                    case 3:
                        //返回上页面
                        setTimeout(function () {
                            // #ifndef H5
                            uni.navigateBack({
                                delta: parseInt(url),
                            });
                            // #endif
                            // #ifdef H5
                            history.back();
                            // #endif
                        }, endtime);
                        break;
                    case 4:
                        //关闭所有页面，打开到应用内的某个页面
                        setTimeout(function () {
                            uni.reLaunch({
                                url: url,
                            });
                        }, endtime);
                        break;
                    case 5:
                        //关闭当前页面，跳转到应用内的某个页面
                        setTimeout(function () {
                            uni.redirectTo({
                                url: url,
                            });
                        }, endtime);
                        break;
                }
            } else if (typeof to_url == "function") {
                setTimeout(function () {
                    to_url && to_url();
                }, endtime);
            } else {
                //没有提示时跳转不延迟
                setTimeout(
                    function () {
                        uni.navigateTo({
                            url: to_url,
                        });
                    },
                    title ? endtime : 0
                );
            }
        }
    },
    /**
     * 数值转换
     */
    formatNumber(e : string, number : number = 3) {
        let reg = new RegExp(`(?=\\B(\\d{${number}})+$)`, "g");
        return e.replace(reg, " ");
    },
    /**
     * 电话的数值转换
     */
    formatPhone(val : string) {
        if (val) {
            const matches = /^(\d{3})(\d{4})(\d{4})$/.exec(val);
            if (matches) {
                return matches[1] + " " + matches[2] + " " + matches[3];
            }
        }
        return val;
    },
    /*
     * 合并数组
     */
    SplitArray(list : Array<Object>, sp : Array<Object>) {
        if (typeof list != "object") return [];
        if (sp === undefined) sp = [];
        for (var i = 0; i < list.length; i++) {
            sp.push(list[i]);
        }
        return sp;
    },
    $h: {
        //除法函数，用来得到精确的除法结果
        //说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
        //调用：$h.Div(arg1,arg2)
        //返回值：arg1除以arg2的精确结果
        Div: function (arg1 : any, arg2 : any) {
            arg1 = parseFloat(arg1);
            arg2 = parseFloat(arg2);
            var t1 : number = 0,
                t2 : number = 0,
                r1 : number,
                r2 : number;
            try {
                t1 = arg1.toString().split(".")[1].length;
            } catch (e) { }
            try {
                t2 = arg2.toString().split(".")[1].length;
            } catch (e) { }
            r1 = Number(arg1.toString().replace(".", ""));
            r2 = Number(arg2.toString().replace(".", ""));
            return this.Mul(r1 / r2, Math.pow(10, t2 - t1));
        },
        //加法函数，用来得到精确的加法结果
        //说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
        //调用：$h.Add(arg1,arg2)
        //返回值：arg1加上arg2的精确结果
        Add: function (arg1 : any, arg2 : any) {
            arg2 = parseFloat(arg2);
            var r1 : number, r2 : number, m : number;
            try {
                r1 = arg1.toString().split(".")[1].length;
            } catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            } catch (e) {
                r2 = 0;
            }
            m = Math.pow(100, Math.max(r1, r2));
            return (this.Mul(arg1, m) + this.Mul(arg2, m)) / m;
        },
        //减法函数，用来得到精确的减法结果
        //说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的减法结果。
        //调用：$h.Sub(arg1,arg2)
        //返回值：arg1减去arg2的精确结果
        Sub: function (arg1 : any, arg2 : any) {
            arg1 = parseFloat(arg1);
            arg2 = parseFloat(arg2);
            var r1 : number, r2 : number, m : number, n : number;
            try {
                r1 = arg1.toString().split(".")[1].length;
            } catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            } catch (e) {
                r2 = 0;
            }
            m = Math.pow(10, Math.max(r1, r2));
            //动态控制精度长度
            n = r1 >= r2 ? r1 : r2;
            return ((this.Mul(arg1, m) - this.Mul(arg2, m)) / m).toFixed(n);
        },
        //乘法函数，用来得到精确的乘法结果
        //说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
        //调用：$h.Mul(arg1,arg2)
        //返回值：arg1乘以arg2的精确结果
        Mul: function (arg1 : any, arg2 : any) {
            arg1 = parseFloat(arg1);
            arg2 = parseFloat(arg2);
            var m : number = 0,
                s1 : string = arg1.toString(),
                s2 : string = arg2.toString();
            try {
                m += s1.split(".")[1].length;
            } catch (e) { }
            try {
                m += s2.split(".")[1].length;
            } catch (e) { }
            return (
                (Number(s1.replace(".", "")) * Number(s2.replace(".", ""))) /
                Math.pow(10, m)
            );
        },
    },
    // //图片上传回调
    // async uploadFilePromise(url) {
    //     const _this = this;
    //     return new Promise((resolve, reject) => {
    //         uni.uploadFile({
    //             url: HTTP_REQUEST_URL + 'Upload/file', // 仅为示例，非真实的接口地址
    //             filePath: url,
    //             name: 'file',
    //             formData: {
    //                 'filename': 'file'
    //             },
    //             header: {
    //                 "content-type": "multipart/form-data",
    //             },
    //             success: (res) => {
    //                 resolve(res.data.data)
    //             }, fail: (err) => {
    //                 _this.Tips({ title: err.msg || "上传失败!" });
    //             }
    //         });
    //     })
    // },
};