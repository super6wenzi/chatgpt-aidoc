<template>
    <view class="body">
        <foodNav :title="title || APP_NAME"></foodNav>
        <!-- 表1 -->
        <view class="form" v-show="step === 0">
            <block v-for="(item, index) in formArr" :key="index">
                <view class="label_row">
                    {{ item.name }}
                </view>
                <view class="input_row" v-if="item.type == '1'">
                    <input type="text" v-model="params[`key${index}`]" :placeholder="item.tags" placeholder-class="placeholder_type" >
                </view>
                <picker v-else-if="item.type == '2' && item.selectVOList" :range="item.selectVOList" range-key="name" @change="pickerChange($event, index)">
                    <view class="input_row">
                        <view class="input_text">
                            <block v-if="!params[`key${index}`]">
                                <text class="placeholder_type">{{ item.tags }}</text>
                            </block>
                            <block v-for="(sel, idx) in item.selectVOList" :key="idx">
                                <text v-if="params[`key${index}`] === sel.value">{{ sel.name }}</text>
                            </block>
                        </view>
                        <view class="selector_icon">
                            <u-icon name="arrow-down" size="30" color="#000000"></u-icon>
                        </view>
                    </view>
                </picker>
            </block>
        </view>
        <!-- 表2 -->
        <view class="form" v-show="step === 1">
            <view class="input_title">
                您有什么特别想提到的点（选填）
            </view>
            <view class="input_subtitle">
                您可以给出多个想融入生成结果的内容,也可以空着
            </view>
            <view class="input_row">
                <input type="text" v-model="params[`keyE1`]" placeholder="1、" placeholder-class="placeholder_type" >
            </view>
            <view class="input_row">
                <input type="text" v-model="params[`keyE2`]" placeholder="2、" placeholder-class="placeholder_type" >
            </view>
            <view class="input_row">
                <input type="text" v-model="params[`keyE3`]" placeholder="3、" placeholder-class="placeholder_type" >
            </view>
            <view class="input_row">
                <input type="text" v-model="params[`keyE4`]" placeholder="4、" placeholder-class="placeholder_type" >
            </view>
            <view class="input_row">
                <input type="text" v-model="params[`keyE5`]" placeholder="5、" placeholder-class="placeholder_type" >
            </view>
        </view>
        <!-- 底部 -->
        <view class="safe_footer"></view>
        <view class="footer_wrapper" v-if="formArr.length">
            <view class="footer_row">
                <view class="next_btn" v-if="step === 0" @tap="nextStep">
					<block v-if="isShowNext == '1'">下一步</block>
					<block v-else>去生成</block>
                </view>
                <view class="next_btn" v-else-if="step === 1" @tap="goResult">
                    去生成
                </view>
            </view>
        </view>
    </view>
</template>

<script lang="ts" setup>
    import { APP_NAME } from "@/config/app";
    import { getTypeForm } from "@/api/api";
    import { global } from "@/store/pinia/index";
    // 全局配置---------------------------------------------
    const { proxy } = getCurrentInstance() as any;
    interface StrForm {
        [key:string]: string;
    }
    // 数据------------------------------------------------
    const step:Ref<number> = ref(0);
    const id:Ref<string> = ref("");
    const title:Ref<string> = ref("");
    const isShowNext:Ref<string> = ref("");
    const formArr:Array<FormDetail> = reactive([]);
    const params:StrForm = reactive({
        keyE1: "",
        keyE2: "",
        keyE3: "",
        keyE4: "",
        keyE5: ""
    });
    // 方法------------------------------------------------
    // 获取表单
    const getForm = () => {
        uni.showLoading({
            title: "正在获取",
            mask: true
        })
        getTypeForm(id.value).then((res:any) => {
            res.data.forEach((item:any, index:number) => {
                params[`key${index}`] = "";
                formArr.push(item);
            })
            uni.hideLoading();
        }).catch((err:any) => {
            uni.hideLoading();
            proxy.$utils.Tips({
                title: err.msg || "获取表单失败"
            }, {
                tab: 3
            })
        })
    }
    // picker变化
    const pickerChange = (e:any, index:number) => {
        params[`key${index}`] = formArr[index].selectVOList[e.detail.value].value;
    }
    // 下一步
    const nextStep = () => {
        for (let i = 0; i < formArr.length; i++) {
            if (!params[`key${i}`]) {
                uni.showToast({
                    title: `请完成 ${title.value} ${formArr[i].name}`,
                    icon: "none"
                })
                return;
            }
        }
		if (isShowNext.value == '1') {
			step.value++
		} else {
			goResult();
		}
    }
    // 去生成
    const goResult = () => {
        let resultArr = [];
        for (let i = 0; i < formArr.length; i++) {
            resultArr.push({
                name: formArr[i].name,
                value: params[`key${i}`]
            })
        }
        let extraStr:string = "";
        for (let i = 0; i < 5; i++) {
            if (params[`keyE${i}`]) {
                extraStr += `${extraStr?"、":""}${params[`keyE${i}`]}`;
            }
        }
        if (extraStr) {
            resultArr.push({
                name: "还有几个特别想提到的点",
                value: extraStr
            })
        }
        uni.setStorageSync("resultMessage", encodeURIComponent(JSON.stringify({ foodBuildId: id.value, foodBuildTitle: title.value, formDTOList: resultArr })));
        uni.navigateTo({
            url: `/pages/resultPage/resultPage`
        })
    }
    // 生命周期---------------------------------------------
    // 加载成功
    onLoad((options) => {
        if (options.id) {
            id.value = options.id;
            getForm();
        } else {
            proxy.$utils.Tips({
                title: "获取表单失败"
            }, {
                tab: 3
            })
        }
        if (options.title) {
            title.value = options.title;
        }
        if (options.isShowNext) {
            isShowNext.value = options.isShowNext;
        }
    })
</script>

<style lang="scss" scoped>
    .body {
        padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom);
    }
    .form {
        padding: 34rpx 34rpx 0;
    }
    .label_row {
        padding: 0 42rpx;
        font-size: 28rpx;
        line-height: 28rpx;
        color: #101010;
        font-family: "AlibabaPuHuiTi-2-65-Medium";
    }
    .input_row + .label_row, picker + .label_row {
        margin-top: 34rpx;
    }
    .input_row {
        margin-top: 22rpx;
        height: 90rpx;
        display: flex;
        align-items: center;
        background-color: #F4F4F4;
        padding: 0 32rpx 0 42rpx;
        font-size: 32rpx;
        line-height: 46rpx;
        font-family: "AlibabaPuHuiTi-2-55-Regular";
        
        input {
            width: 100%;
            height: 46rpx;
        }
        
        .placeholder_type {
            color: #A1A1A1;
        }
        
        .input_text {
            width: 100%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        
        .selector_icon {
            font-size: 0;
            margin-left: 12rpx;
            flex-shrink: 0;
        }
    }
    .input_title {
        padding: 0 42rpx;
        color: #101010;
        font-size: 28rpx;
        line-height: 28rpx;
        font-family: "AlibabaPuHuiTi-2-65-Medium";
    }
    .input_subtitle {
        padding: 0 42rpx;
        color: #101010;
        font-size: 24rpx;
        line-height: 24rpx;
        font-family: "AlibabaPuHuiTi-2-55-Regular";
        margin: 22rpx 0 34rpx;
    }
    // 底部
    .safe_footer {
        height: 200rpx;
    }
    .footer_wrapper {
        position: fixed;
        bottom: 0;
        left: 0;
        width: 100%;
        z-index: 999;
        padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom);
    }
    .footer_row {
        padding: 44rpx 0;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .next_btn {
        line-height: 112rpx;
        width: 598rpx;
        background-color: #161616;
        text-align: center;
        color: #FFFFFF;
        font-size: 36rpx;
        font-family: "AlibabaPuHuiTi-2-85-Bold";
    }
</style>
