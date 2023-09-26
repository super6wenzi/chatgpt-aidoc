<template>
    <view class="body" v-if="true">
        <u-empty text="敬请期待"></u-empty>
    </view>
    <view class="body" v-else>
        <view class="title">
            
        </view>
    </view>
</template>

<script lang="ts" setup>
    // 全局配置---------------------------------------------
    // 数据------------------------------------------------
    const previewVideo:VideoDetail = reactive({});
    const gptClassList:Array<VideoDetail> = reactive([]);
    const mjClassList:Array<VideoDetail> = reactive([]);
    // 方法------------------------------------------------
    const getPreviewVideo = () => {
        setTimeout(() => {
            const res = {
                id: 1,
                // url: "https://www.runoob.com/try/demo_source/movie.mp4"
            };
            Object.assign(previewVideo, res);
        }, 300)
    }
    const getGptClassList = () => {
        setTimeout(() => {
            gptClassList.length = 0;
            const res = [];
            gptClassList.push(...res)
        }, 300)
    }
    const getMjClassList = () => {
        setTimeout(() => {
            mjClassList.length = 0;
            const res = [];
            mjClassList.push(...res)
        }, 300)
    }
    // 生命周期---------------------------------------------
    // 加载完成
    onLoad(() => {
        uni.showLoading({
            title: "正在加载",
            mask: true
        })
        Promise.all([
            getPreviewVideo(),
            getGptClassList(),
            getMjClassList()
        ]).then(() => {
            uni.hideLoading();
        }).catch(() => {
            uni.hideLoading();
        })
        uni.$on("RefreshClassList", () => {
            uni.showLoading({
                title: "正在加载",
                mask: true
            })
            Promise.all([
                getGptClassList(),
                getMjClassList()
            ]).then(() => {
                uni.hideLoading();
            }).catch(() => {
                uni.hideLoading();
            })
        })
    })
</script>

<style lang="scss" scoped>
    .body {
        padding-top: 100rpx
    }
</style>
