<template>
    <view class="body">
        <foodNav :title="APP_NAME"></foodNav>
        <view class="title">
            选择这次需要优化的品牌
        </view>
        <view class="item" :class="{'active': index === chooseCurrent}" v-for="(item, index) in list" :key="index" @tap="chooseBrand(index)">
            <view class="choose_icon" :class="{'choosed': index === chooseCurrent}">
                <image src="/static/images/selected.png" v-show="index === chooseCurrent"></image>
            </view>
            <view class="item_center">
                <view class="brand_name">
                    {{ item.name }}
                </view>
                <view class="brand_detail">
                    <view class="detail_item">
                        品类：{{ item.category }}
                    </view>
                    <view class="detail_item">
                        地址：{{ item.address }}
                    </view>
                    <view class="detail_item">
                        客单：{{ item.price }}元/人
                    </view>
                    <view class="detail_item">
                        客流：{{ item.passengerFlow }}人/日
                    </view>
                </view>
            </view>
            <view class="btn_right">
                <view class="default_btn" v-show="index === chooseCurrent" @tap.stop="goEdit(item.id)">
                    编辑
                </view>
            </view>
        </view>
        <view class="item add_brand" @tap.stop="goAdd">
            <view class="add_icon">
                <image src="/static/images/add_icon.png" ></image>
            </view>
            <view class="add_text">
                点击新增餐厅
            </view>
        </view>
        <view class="next_btn" @tap.stop="goNext">
            下一步
        </view>
    </view>
</template>

<script lang="ts" setup>
    import { APP_NAME } from "@/config/app";
    import { getBrandList } from "@/api/api";
    // 全局配置---------------------------------------------
	const { proxy } = getCurrentInstance() as any;
    // 数据------------------------------------------------
    const list:Array<BrandDetail> = reactive([]);
    const current:Ref<number> = ref(1);
    const size:Ref<number> = ref(20);
    const loading:Ref<boolean> = ref(false);
    const loadend:Ref<boolean> = ref(false);
    const chooseCurrent:Ref<number> = ref(-1);
    // 方法------------------------------------------------
    const getList = () => {
        if (loading.value || loadend.value) return;
        uni.showLoading({
            title: "正在加载",
            mask: true
        })
        loading.value = true;
        getBrandList({
            current: current.value,
            size: size.value,
        }).then((res:any) => {
            uni.hideLoading();
            if (current.value === 1) {
                list.length = 0;
            }
            list.push(...res.data.records);
            loadend.value = current.value >= res.data.pages;
            if (!loadend.value) {
                current.value++;
            }
            loading.value = false;
        }).catch((err:any) => {
            uni.hideLoading();
            proxy.$utils.Tips({
                title: err.msg || "系统错误"
            })
        })
    }
    const refreshList = () => {
        current.value = 1;
        loadend.value = false;
        loading.value = false;
        getList();
    }
    // 切换选择
    const chooseBrand = (index:number) => {
        if (index === chooseCurrent.value) {
            chooseCurrent.value = -1;
        } else {
            chooseCurrent.value = index;
        }
    }
    // 编辑
    const goEdit = (id:string) => {
        uni.navigateTo({
            url: `/pages/addBrand/addBrand?id=${id}`
        })
    }
    // 新增
    const goAdd = () => {
        uni.navigateTo({
            url: `/pages/addBrand/addBrand`
        })
    }
    // 下一步
    const goNext = () => {
        if (chooseCurrent.value <= -1) {
            uni.showToast({
                title: "请选择品牌",
                icon: "none"
            })
            return;
        }
        uni.navigateTo({
            url: `/pages/chooseType/chooseType?id=${list[chooseCurrent.value].id}`
        })
    }
    // 生命周期---------------------------------------------
    // 加载成功
    onLoad(() => {
        getList();
        uni.$on("RefreshBrand", refreshList);
    })
    // 触底加载
    onReachBottom(() => {
        getList();
    })
</script>

<style lang="scss" scoped>
    .body {
        padding-bottom: 24rpx;
    }
    .title {
        font-family: "AlibabaPuHuiTi-2-95-ExtraBold";
        font-size: 42rpx;
        color: #101010;
        margin-top: 58rpx;
        margin-bottom: 48rpx;
        padding: 32rpx;
    }
    .item {
        width: 686rpx;
        margin: 0 auto;
        border: 6rpx solid #b7b7b7;
        border-radius: 4rpx;
        display: flex;
        align-items: center;
        color: #b7b7b7;
        padding: 36rpx 34rpx 42rpx 28rpx;
        
        & + & {
            margin-top: 10rpx;
        }
        
        &.active {
            border-color: #010101;
            color: #000000;
        }
        
        &.add_brand {
            padding: 0;
            border-color: #C3C3C3;
            flex-direction: column;
            height: 246rpx;
            justify-content: center;
        }
    }
    .choose_icon {
        width: 36rpx;
        height: 36rpx;
        border: 6rpx solid #C3C3C3;
        border-radius: 50%;
        flex-shrink: 0;
        margin-right: 30rpx;
        font-size: 0;
        
        &.choosed {
            border-width: 0;
        }
    }
    .item_center {
        width: 100%;
        padding-bottom: 14rpx;
        overflow: hidden;
    }
    .brand_name {
        font-family: "AlibabaPuHuiTi-2-85-Bold";
        font-size: 36rpx;
        line-height: 46rpx;
        padding-bottom: 6rpx;
    }
    .brand_detail {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
    }
    .detail_item {
        width: 48%;
        margin-top: 18rpx;
        font-family: "AlibabaPuHuiTi-2-75-SemiBold";
        font-size: 24rpx;
        line-height: 32rpx;
    }
    .btn_right {
        width: 118rpx;
        margin-left: 24rpx;
        flex-shrink: 0;
    }
    .default_btn {
        width: 100%;
        line-height: 64rpx;
        text-align: center;
        color: #FFFFFF;
        font-family: "AlibabaPuHuiTi-2-75-SemiBold";
        font-size: 24rpx;
        background-color: #101010;
    }
    .add_icon {
        width: 56rpx;
        height: 56rpx;
    }
    .add_text {
        color: #000000;
        font-size: 24rpx;
        line-height: 24rpx;
        margin-top: 24rpx;
    }
    .next_btn {
        width: 598rpx;
        line-height: 112rpx;
        text-align: center;
        margin: 146rpx auto 0;
        color: #FFFFFF;
        background-color: #161616;
        font-size: 36rpx;
    }
</style>
