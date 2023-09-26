<template>
    <view class="food_nav">
        <view v-if="safeTop" :style="{'height': `${global().NavSafeHeight + global().NavHeight}px`}"></view>
        <view class="nav_wrapper" v-if="showNav">
            <view class="safe_top" :style="{'height': `${global().NavSafeHeight}px`}"></view>
            <view class="title_row" :style="{'height': `${global().NavHeight}px`, 'padding-right': global().NavSafeRight?`${global().NavSafeRight}px`:''}">
                <view class="back_icon" v-if="leftBack">
                    <u-icon name="arrow-left" v-if="showBack" @tap="goBack"></u-icon>
                    <u-icon name="home" v-else @tap="goHome"></u-icon>
                </view>
                <view class="title_text">
                    {{ title }}
                </view>
            </view>
        </view>
    </view>
</template>

<script setup lang="ts" name="foodNav">
	// 全局配置----------------------------------------
	import { global } from '@/store/pinia/index';
	// const { proxy } = getCurrentInstance() as any;
	// 组件配置----------------------------------------
	defineProps({
        title: {
            type: String,
            default: () => {
                return "";
            }
        },
        showNav: {
            type: Boolean,
            default: () => {
                return true;
            }
        },
        safeTop: {
            type: Boolean,
            default: () => {
                return true;
            }
        },
        leftBack: {
            type: Boolean,
            default: () => {
                return true;
            }
        },
	})
	// 数据--------------------------------------------
    const showBack:Ref<boolean> = ref(false);
	// 方法--------------------------------------------
    const goBack = () => {
        uni.navigateBack();
    }
    const goHome = () => {
        uni.switchTab({
            url: "/pages/index/index"
        })
    }
	// 抛出方法----------------------------------------
	// 生命周期----------------------------------------
    onMounted(() => {
        let pages = getCurrentPages();
        if (pages.length <= 1) {
            showBack.value = false;
        } else {
            showBack.value = true;
        }
    })
</script>

<style lang="scss" scoped>
    .nav_wrapper {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        background-color: #FFFFFF;
        z-index: 99;
    }
    .title_row {
        display: flex;
        align-items: center;
        padding: 0 32rpx;
    }
    .back_icon {
        font-weight: bold;
        margin-right: 16rpx;
    }
    .title_text {
        font-family: "AlibabaPuHuiTi-2-85-Bold";
        font-size: 40rpx;
        font-weight: bold;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }
</style>