import { defineStore } from 'pinia';
import { wxLogin, getUserInfo } from '@/api/api';
const baseUserInfo = {
    avatar: "../../static/images/avatar.png",
    nickname: "",
    times: 0,
}
export const global = defineStore("global",{
    state: () =>{
        return{
            NavSafeHeight: 0,
            NavSafeRight: 0,
            NavHeight: 0,
            // 用户信息
            userInfo: reactive(JSON.parse(JSON.stringify(baseUserInfo))),
            token: ref("")
        }
    },
    actions: {
        updateUserinfo() {
            getUserInfo().then((res:any) => {
                if (res.code == 200) {
                    Object.assign(this.userInfo, res.data);
                }else{
                    uni.showToast({
                        mask: true,
                        icon: "none",
                        duration: 2000,
                        title: res.msg ||"获取用户信息失败!"
                    })
                }
            }).catch((err:any) => {
                uni.showToast({
                    mask: true,
                    icon: "none",
                    duration: 2000,
                    title: err.msg ||"获取用户信息失败!"
                })
            })
        },
        logOuts(){
            this.userInfo = JSON.parse(JSON.stringify(baseUserInfo));
            this.token = "";
        },
        updateToken() {
            const _this = this;
            uni.login({
                success:(code:any)=>{
					console.log(115555,code);
                    wxLogin(code.code).then((res:any)=>{
                        uni.setStorageSync("TOKEN", res.data);
                        _this.token = res.data;
                        _this.updateUserinfo();
                    })
                }
            })
        }
    }
})