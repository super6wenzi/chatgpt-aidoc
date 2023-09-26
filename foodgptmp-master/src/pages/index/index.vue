<template>
    <view class="body">
        <foodNav :title="APP_NAME" :leftBack="false"></foodNav>
        <view class="title">
            {{ (list[current] && list[current].buildTitle) ? list[current].buildTitle : "选择想要生成的内容类型" }}
        </view>
        <view class="cate_row">
            <view class="cate_item" :class="{'active': index === current}" v-for="(item, index) in list" :key="index" @tap="current = index">
                {{ item.name }}
            </view>
        </view>
        <view class="list_area" v-for="(item, index) in list" :key="index" v-show="current === index">
            <view class="list_row" v-for="(type, idx) in item.buildType" :key="idx">
                <view class="list_icon">
                    <image :src="type.icon" mode="widthFix" style="width: 100%;"></image>
                </view>
                <view class="list_title">
                    {{ type.title }}
                </view>
                <view class="list_btn" @tap="goForm(type)">
                    {{ (list[current] && list[current].buildTips) ? list[current].buildTips : "生成" }}
                </view>
            </view>
        </view>
    </view>
</template>

<script lang="ts" setup>
	import {  onLoad, onShow,onShareAppMessage,onShareTimeline} from '@dcloudio/uni-app'
    import { APP_NAME } from "@/config/app";
    import { getBuildTypeList } from "@/api/api";
    import { global } from "@/store/pinia/index";
    // 全局配置---------------------------------------------
	const { proxy } = getCurrentInstance() as any;
    // 数据------------------------------------------------
    const list:Array<BrandTypeDetail> = reactive([]);
    const current:Ref<number> = ref(0);
    // 方法------------------------------------------------
    const getList = () => {
        uni.showLoading({
            title: "正在加载",
            mask: true
        })
        getBuildTypeList().then((res:any) => {
            uni.hideLoading();
            list.push(...res.data);
        }).catch((err:any) => {
            uni.hideLoading();
            proxy.$utils.Tips({
                title: err.msg || "系统错误"
            })
        })
    }
    // 前往
    const goForm = (type:BuildTypeDetail) => {
        if (global().token) {
            if (global().userInfo.times <= 0) {
                proxy.$utils.Tips({
                    title: `剩余生成次数为${global().userInfo.times}，暂时无法生成！`
                })
                return;
            }
            if (type.id) {
                uni.navigateTo({
                    url: `/pages/typeForm/typeForm?id=${type.id}&title=${type.title}&isShowNext=${type.isShowNext}`
                })
            }
        } else {
            uni.switchTab({
                url: "/pages/my/my"
            })
        }
    }
    // 生命周期---------------------------------------------
    // 加载成功
    onLoad(() => {
        getList()
    })
    onShow(() => {
        if (global().token) {
            global().updateUserinfo();
        }
    })
	
	let urlImg  = ''
	onShareAppMessage((res) => {
		console.log(res)
		
			return {
				title: "给你绝佳的文案灵感，让你的营销更具吸引力！",
				path: '/pages/index/index' ,
				imageUrl: urlImg,
				success: res => {
				},
				fail: err => {
				}
			};
		
	})
	onShareTimeline(() => {
		return {
		    title: '给你绝佳的文案灵感，让你的营销更具吸引力！',
		    imageUrl: urlImg
		}
	})
</script>

<style lang="scss" scoped>
    .body {
        padding-bottom: 100rpx;
        min-height: 100vh;
        position: relative;
    }
    .title {
        padding: 48rpx 64rpx 40rpx;
        font-size: 42rpx;
        line-height: 42rpx;
        color: #101010;
        font-family: "AlibabaPuHuiTi-2-95-ExtraBold";
    }
    .cate_row {
        display: flex;
        align-items: center;
        padding: 0 48rpx;
        overflow: auto;
        width: 100%;
        white-space: nowrap;
    }
    .cate_item {
        padding: 0 18rpx;
        line-height: 56rpx;
        background-color: #EAEAEA;
        color: #737373;
        transition-duration: 300ms;
        font-size: 28rpx;
        font-family: "AlibabaPuHuiTi-2-65-Medium";
        flex-shrink: 0;
        
        & + & {
            margin-left: 36rpx;
        }
        
        &.active {
            background-color: #000000;
            color: #FFFFFF;
        }
    }
    .list_area {
        margin-top: 44rpx;
        padding: 0 32rpx;
    }
    .list_row {
        border: 6rpx solid #101010;
        border-radius: 2rpx;
        padding: 16rpx;
        display: flex;
        align-items: center;
        
        & + & {
            margin-top: 12rpx;
        }
    }
    .list_icon {
        width: 40rpx;
        // height: 40rpx;
        font-size: 0;
        flex-shrink: 0;
        margin-right: 12rpx;
    }
    .list_title {
        width: 100%;
        font-size: 28rpx;
        color: #101010;
        font-family: "AlibabaPuHuiTi-2-85-Bold";
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
    // 开发者
    .foot_developer {
        position: absolute;
        bottom: 36rpx;
        left: 0;
        font-size: 24rpx;
        width: 100%;
        text-align: center;
        color: #b0b0b0;
    }
</style>
