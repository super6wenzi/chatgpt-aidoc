// import {
//     HTTP_REQUEST_URL,
//     // HEADER
// } from '@/config/app.ts';
import { HTTP_REQUEST_URL } from '@/config/app';
import utils from './utils'
import { global } from "@/store/pinia/index";
/**
 * 发送请求
 */
function baseRequest(url:string, method:any, data:any, {noAuth = false,noVerify = false}:{noAuth:Boolean,noVerify:Boolean}) {
    const token = uni.getStorageSync("TOKEN")
    if (!noAuth && !token) {
        utils.Tips({ title: '请先登录', endtime: 1000 }, {
            tab: 1,
            url: "/pages/my/my"
        });
        return Promise.reject({
            msg: "请先登录"
        });
    }
    return new Promise((reslove, reject) => {
        uni.request({
            url: HTTP_REQUEST_URL + "/api/" + url,
            method: method || 'GET',
            header: {
                "content-type": "application/json",
                "token": token
            },
            data: data,
            success: (res:any) => {
                // if(res.statusCode===500){// 接口异常抛出
                //     return reject({code: res.statusCode,msg: res.data});
                // }
                if (noVerify) {
                    reslove(res.data);
                } else if(res.statusCode == 200) {
                    if (res.data.code == 200) {
                        reslove(res.data);
                    } else if(res.data.code == 400) {
                        reject(res.data || '系统错误');
                    } else{
                        reject(res.data || '系统错误');
                    }
                    // utils.Tips({title: res.errMsg||" Ошибка запроса"});
                    // reject(res.data || '系统错误');
                } else if(res.statusCode == 500) {
                    global().logOuts();
                    utils.Tips({ title: res.data || '请先登录', endtime: 1000 }, {
                        tab: 1,
                        url: "/pages/my/my",
                        endtime: 3000
                    });
                    reject(res.data || '请先登录');
                } else {
                   // utils.Tips({title: "系统错误"},{});
                   // reject(res.data || '系统错误');
                   return reject({code: res.statusCode,msg: res.data});
                }
            },
            fail: (message:any) => {
                console.log(message)
                reject('系统错误');
                utils.Tips({title: message.msg||"系统错误"},{})
            }
        })
    });
}

const request:any = {};

['options', 'get', 'post', 'put', 'head', 'delete', 'trace', 'connect'].forEach((method) => {
    request[method] = (api:any, data:any, opt:any) => baseRequest(api, method, data, opt || {})
});



export default request;
