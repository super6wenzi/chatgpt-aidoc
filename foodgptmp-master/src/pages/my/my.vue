<template>
    <view class="body">
        <foodNav :showNav="false" ></foodNav>
        <!-- <view class="body-avatar" :style="`--_pd: ${global().userInfo.hasOwnProperty('name')?'20rpx':'50rpx'};`" @tap="show=true">
            <image :class="{ '_radius': global().userInfo.avatar!=='../../static/images/avatar.png' }" :src="global().userInfo.avatar" mode=""></image>
        </view> -->
        <view class="body-avatar" :style="`--_pd: 20rpx;`" @tap="show=true">
            <image class="_radius" :src="global().userInfo.avatar" mode="" v-if="global().userInfo.avatar"></image>
            <image class="_radius" src="../../static/images/avatar.png" mode="" v-else></image>
        </view>
        <view class="body-name" @tap="show=true">{{global().userInfo.name?global().userInfo.name:"编辑头像昵称"}}</view>
        <!-- <view class="body-rank" @tap="show=true" v-if="global().userInfo.userType === '游客'">{{global().userInfo.userType?global().userInfo.userType:"游客"}}</view>
        <view class="body-vip" @tap="show=true" v-else-if="global().userInfo.vip === '宝餐会员'">{{global().userInfo.vip?global().userInfo.vip:"宝餐会员"}}</view> -->
        <view class="body-times">
            <view class="body-times-left">
                剩余生成次数
            </view>
            <view class="body-times-right">
                {{ global().userInfo.times?global().userInfo.times:0 }}次
            </view>
        </view>
		
		<view class="mt-20 another"  @tap="codePopup=true" style="display: flex;align-items: center;justify-content: center;">
			想添加适合自己的模型？
			<view class="list_btn">
				联系作者
			</view>
		</view>
		<view class="mt-20 another">
			<!-- 点击下方体验其他AI工具 -->
		    <view  class="mt-20 flex" >
		        跳转至 <view class="list_btn ml-20" @click="jump('唤灵')">
		        	唤灵AI
		        </view>
				<view class="list_btn ml-20" @click="jump('GG')">
					GGAI
				</view>
		    </view>
			
		</view>
    </view>
    <u-popup v-model="show" mode="center" border-radius="15">
        <form @submit="formsubmit">
            <view class="_popu">
                <!-- <view class="_popu-avatar" :style="`--_pd: ${(from.avatar || global().userInfo.avatar)?'10rpx':'30rpx'};`"> -->
                <view class="_popu-avatar" :style="`--_pd: 10rpx;`">
                    <!-- <image :class="{ '_radius': from.avatar }" :src="from.avatar?from.avatar:'../../static/images/avatar.png'" mode="aspectFill"></image> -->
                    <image class="_radius" :src="from.avatar" mode="aspectFill" v-if="from.avatar" ></image>
                    <image class="_radius" :src="global().userInfo.avatar" mode="aspectFill" v-else-if="global().userInfo.avatar" ></image>
                    <image class="_radius" src="/static/images/avatar.png" mode="aspectFill" v-else ></image>
                    
                    <button open-type="chooseAvatar" @chooseavatar="bindchooseavatar">获取头像</button>
                </view>
                <input type="nickname" name="nick" v-model="from.nick" placeholder="请输入您的用户名" placeholder-class="plo"
                    placeholder-style="text-align: center">
                <button class="_popu-sumbit" form-type="submit" hover-class="none">提交</button>
            </view>
        </form>
    </u-popup>
	<u-popup v-model="codePopup" mode="center" border-radius="15">
		<view class="_popu">
			<view class="" style="margin-bottom: 20rpx;">
				客服微信：
			</view>
			<image src="../../static/images/code.jpg" mode="widthFix" style="width: 400rpx;" class="mt-20"></image>
		</view>
	</u-popup>
</template>

<script setup lang="ts">
    import { upload, updateUserInfo } from "@/api/api";
    import { global } from "@/store/pinia/index";
// import { log } from "util";
    const { proxy } = getCurrentInstance() as any;

    // 微信登录弹窗显示隐藏
    const show:Ref<boolean> = ref(false);
	// 客服二维码显示隐藏
	const codePopup:Ref<boolean> = ref(false)
	
    // 表单数据
    const from:{[key:string]:string} = reactive({
        avatar: "",
        nick: ""
    })
    
    watch(
        () => global().userInfo,
        (nVal) => {
            from.nick = nVal.name;
        },
        {
            immediate: true,
            deep: true
        }
    )
    function jump(name){
		console.log(22111,name)
		if(name=='唤灵'){
			uni.navigateToMiniProgram({
				appId:'wxe584b75071908e37',
				path: 'pages/index/index',
					    //develop开发版；trial体验版；release正式版
					    envVersion: 'release', 
					    success(res:any) {
					      // 打开成功
					      console.log("跳转小程序成功！",res);
					    } 
			})
		}else{
			uni.navigateToMiniProgram({
				appId:'wxf08ba2dc886a21af',
				path: 'pages/main/form/index',
					    //develop开发版；trial体验版；release正式版
					    envVersion: 'release', 
					    success(res:any) {
					      // 打开成功
					      console.log("跳转小程序成功！",res);
					    } 
			})
		}
		
	}
    // 授权头像回调
    function bindchooseavatar(e:any){
        from.avatar = e.detail.avatarUrl
    }

    function formsubmit(e:any){
        from.nick = e.detail.value.nick;
        if(from.avatar=="" && !global().userInfo.avatar) return proxy.$utils.Tips({title: "请授权或上传头像!"});
        if(from.nick=="") return proxy.$utils.Tips({title: "请授权或输入您的用户名!"});
        if (!from.avatar) {
            updateUserInfo({
                "nick": from.nick,
            }).then((res:any)=>{
                show.value = false;
                if(res.code===200){
                    proxy.$utils.Tips({title: "更新成功!"});
                    global().updateUserinfo();
                }else{
                    proxy.$utils.Tips({title: res.msg||"更新失败!"});
                }
            }).catch((err:any)=>{
                show.value = false;
                proxy.$utils.Tips({title: err.msg||"更新失败!"});
            })
            return;
        }
        uni.uploadFile({
            url: upload(),
            // #ifndef H5
            filePath: from.avatar,
            // #endif
            // #ifdef H5
            file: from.avatar,
            // #endif
            name: "file",
            header: {
                "Content-Type": "multipart/form-data",
                "token": uni.getStorageSync("TOKEN")
            },
            success(result) {
                uni.hideLoading();
                let rest = JSON.parse(result.data);
                if(rest.url){
                    updateUserInfo({
                        "avatar": rest.url,
                        "nick": from.nick,
                    }).then((res:any)=>{
                        show.value = false;
                        if(res.code===200){
                            proxy.$utils.Tips({title: "更新成功!"});
                            global().updateUserinfo();
                        }else{
                            proxy.$utils.Tips({title: res.msg||"更新失败!"});
                        }
                    }).catch((err:any)=>{
                        show.value = false;
                        proxy.$utils.Tips({title: err.msg||"更新失败!"});
                    })
                }else{
                    show.value = false;
                    proxy.$utils.Tips({title: rest.msg||"图片上传失败!"});
                }
            },
            fail() {
                uni.hideLoading();
                show.value = false;
                proxy.$utils.Tips({title: "图片上传失败!"});
            }
        })
    }
    onShow(() => {
        if (!global().token) {
            global().updateToken();
        } else if (!global().userInfo.nickname) {
            global().updateUserinfo();
        }
    })
</script>

<style lang="scss" scoped>
.body{
    width: 100vw;
    min-height: 100vh;
    @include flex(column,initial,center);
    font-family: "AlibabaPuHuiTi-2-85-Bold";
	.another{
		width:100%;
		padding:0 80rpx;
		
	}
	.mt-20{
		margin-top: 20rpx;
	}
	.ml-20{
		margin-left: 20rpx;
	}
	.flex{
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.list_btn {
	    width: 118rpx;
	    line-height: 64rpx;
	    text-align: center;
	    flex-shrink: 0;
	    background-color: #101010;
	    font-size: 24rpx;
	    color: #FFFFFF;
	    font-family: "AlibabaPuHuiTi-2-85-Bold";
	}
    &-avatar{
        width: 353rpx;
        height: 353rpx;
        overflow: hidden;
        margin-top: 55rpx;
        border-radius: 50%;
        padding: var(--_pd);
        background-color: #EBEBEB;

        image{
            width: 100%;
            height: 100%;
        }

    }

    &-name{
        margin-top: 38rpx;
        @include font(#000,60rpx,bold);
    }

    &-rank{
        height: 56rpx;
        padding: 0 15px;
        min-width: 200rpx;
        margin-top: 45rpx;
        line-height: 56rpx;
        text-indent: 10rpx;
        letter-spacing: 10rpx;
        background-color: #ECECEC;
        @include font(#787878,28rpx,unset,center);
    }

    &-vip{
        height: 56rpx;
        padding: 0 15px;
        min-width: 200rpx;
        margin-top: 45rpx;
        margin-top: 10rpx;
        line-height: 56rpx;
        background-color: #000000;
        @include font(#D8C99D,28rpx,unset,center);
    }

    &-login{
        width: 450rpx;
        height: 90rpx;
        margin-top: 45rpx;
        line-height: 90rpx;
        text-indent: 50rpx;
        letter-spacing: 50rpx;
        background-color: #000;
        @include font(#D8C99D,35rpx,bold,center);
    }
    
    &-times {
        margin: 36rpx auto 0;
        width: 600rpx;
        line-height: 56rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        background-color: #ECECEC;
        padding: 18rpx 30rpx;
        
        &-left {
            color: #a9a9a9;
        }
        
        &-right {
            color: #060606;
        }
    }
    
    &-developer {
        position: absolute;
        bottom: 36rpx;
        left: 0;
        font-size: 24rpx;
        width: 100%;
        text-align: center;
        color: #b0b0b0;
    }
}

._popu{
    width: 80vw;
    min-height: 40vh;
    padding: 30rpx;
    @include flex(column,initial,center);

    &-avatar{
        width: 200rpx;
        height: 200rpx;
        margin-top: 30rpx;
        border-radius: 50%;
        position: relative;
        padding: var(--_pd);
        background-color: #f2f2f2;

        image{
            width: 100%;
            height: 100%;
        }

        button{
            top: 0;
            left: 0;
            opacity: 0;
            width: 100%;
            height: 100%;
            position: absolute;
        }

    }

    input{
        width: 70%;
        height: 90rpx;
        line-height: 90rpx;
        padding: 0 30rpx;
        margin-top: 50rpx;
        text-align: center;
        border-radius: 15rpx;
        border: 1rpx solid #000;
    }
    
    .plo {
        line-height: 90rpx;
    }

    &-sumbit{
        width: 100%;
        height: 90rpx;
        margin-top: 80rpx;
        line-height: 90rpx;
        border-radius: 10rpx;
        background-color: #000;
        @include font(#fff,35rpx,unset,center);
    }

}

._radius{
    overflow: hidden;
    border-radius: 50%;
}
</style>
