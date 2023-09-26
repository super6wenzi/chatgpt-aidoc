<template>
    <view class="body">
        <foodNav :title="APP_NAME"></foodNav>
        <view class="title">
            <view>选择想要生成的</view>
            <view>内容类型</view>
        </view>
        <view class="item" v-for="(item, index) in list" :key="index">
            <view class="item_title">
                {{ item.name }}
            </view>
            <view class="item_row" v-for="(type, idx) in item.buildType" :key="idx">
                <view class="item_icon">
                    <image :src="type.icon" mode="aspectFit"></image>
                </view>
                <view class="item_name line1">
                    {{ type.title }}
                </view>
                <view class="generate_btn" @tap="generateGPT(type)">
                    生成
                </view>
            </view>
        </view>
    </view>
</template>

<script lang="ts" setup>
    import { APP_NAME } from "@/config/app";
    import { getBuildTypeList } from "@/api/api";
    import { global } from "@/store/pinia/index";
    // 全局配置---------------------------------------------
	const { proxy } = getCurrentInstance() as any;
    // 数据------------------------------------------------
    const id:Ref<string> = ref("");
    const list:Array<BrandTypeDetail> = reactive([]);
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
    // 生成
    const generateGPT = (type:BrandTypeDetail) => {
        if (type.id) {
            uni.navigateTo({
                url: `/pages/resultPage/resultPage?brandId=${id.value}&foodBuildId=${type.id}&title=${type.title}`
            })
        }
    }
    // 生命周期---------------------------------------------
    // 加载成功
    onLoad((options) => {
        if (options.id) {
            id.value = options.id;
            getList();
        } else {
            proxy.$utils.Tips({
                title: "未获取到品牌，请重新选择"
            }, {
                tab: 3
            })
        }
    })
</script>

<style lang="scss" scoped>
    .title {
        font-family: "AlibabaPuHuiTi-2-95-ExtraBold";
        margin-top: 44rpx;
        margin-bottom: 48rpx;
        padding: 0 64rpx;
        font-size: 42rpx;
        line-height: 46rpx;
    }
    .item {
        padding: 0 30rpx;
        
        & + & {
            margin-top: 6rpx;
        }
    }
    .item_title {
        font-family: "AlibabaPuHuiTi-2-85-Bold";
        font-size: 28rpx;
        line-height: 58rpx;
        padding: 0 32rpx;
    }
    .item_row {
        display: flex;
        align-items: center;
        height: 106rpx;
        border: 6rpx solid #101010;
        border-radius: 2rpx;
        padding: 0 16rpx;
        
        & + & {
            margin-top: 14rpx;
        }
    }
    .item_icon {
        width: 40rpx;
        height: 40rpx;
        font-size: 0;
        flex-shrink: 0;
        margin-right: 6rpx;
    }
    .item_name {
        width: 100%;
        font-size: 28rpx;
        font-family: "AlibabaPuHuiTi-2-85-Bold";
        color: #101010;
    }
    .generate_btn {
        flex-shrink: 0;
        width: 118rpx;
        line-height: 64rpx;
        text-align: center;
        background-color: #101010;
        color: #FFFFFF;
        font-size: 24rpx;
        font-family: "AlibabaPuHuiTi-2-85-Bold";
        margin-left: 12rpx;
    }
</style>
