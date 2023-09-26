<template>
    <view class="body">
        <foodNav :title="resultParams.foodBuildTitle || APP_NAME"></foodNav>
        <!-- GPT对话 -->
        <view class="content_item" :class="{'bg_white': item.fromUser === 'USER'}" v-for="(item, index) in contentList" :key="index">
            <view class="avatar">
                <block v-if="item.fromUser === 'USER'">
                    <image :src="global().userInfo.avatar" mode="aspectFill" v-if="global().userInfo.avatar"></image>
                    <image src="/static/images/avatar.png" mode="aspectFill" v-else></image>
                </block>
                <image src="/static/logo.png" mode="aspectFill" v-else ></image>
            </view>
            <view class="content">
                <text user-select >{{ item.content }}</text>
            </view>
        </view>
        <!-- 底部生成按钮 -->
        <view class="top_generate">
            <view class="generate_row" v-if="!inputFocus">
                <view class="generate_btn" @tap="getRegeneration">
                    <view class="btn_title">
                        <view class="title_icon">
                            <image src="/static/images/regeneration_icon.png" mode="aspectFit"></image>
                        </view>
                        <view class="title_text">
                            重新生成
                        </view>
                    </view>
                    <view class="btn_subtitle">
                        对结果不满意时 请点击我
                    </view>
                </view>
                <view class="generate_btn" @tap="getContinue">
                    <view class="btn_title">
                        <view class="title_icon">
                            <image src="/static/images/continue_icon.png" mode="aspectFit"></image>
                        </view>
                        <view class="title_text">
                            继续生成
                        </view>
                    </view>
                    <view class="btn_subtitle">
                        当我生成中断时 请点击我
                    </view>
                </view>
            </view>
        </view>
        <!-- 底部输入框 -->
        <view class="safe_footer"></view>
        <view class="keyboard_height" :style="{height: `${keyboardHeight}px`}" ></view>
        <view class="footer_wrapper" :style="{bottom: `${keyboardHeight}px`, 'padding-bottom': keyboardHeight?'0px':''}">
            <view class="footer_row">
                <view class="footer_input" :style="{'height': inputFocus?'':'88rpx'}">
                    <textarea v-model="content" placeholder="点击描述您的需求" confirm-type="send" :disabled="loading" :adjust-position="false" :maxlength="-1" auto-height @confirm="sendContent" @focus="inputFocus = true" @blur="inputFocus = false" disable-default-padding />
                </view>
            </view>
        </view>
        <!-- loading -->
        <view class="loading_mask" v-if="loading" @mousewheel.stop.prevent @touchmove.stop.prevent >
            <view class="loading_row" :style="{'top': `calc(${global().NavSafeHeight + global().NavHeight}px + 34rpx)`}">
                <u-loading mode="circle" color="#202020" size="30"></u-loading>
                <view class="loading_text">
                    努力生成中，请等等我
                </view>
            </view>
        </view>
    </view>
</template>

<script lang="ts" setup>
    import { APP_NAME } from "@/config/app";
    import { chatGPT, createMessage } from "@/api/api";
    import { global } from "@/store/pinia/index";
    // 全局配置---------------------------------------------
    const { proxy } = getCurrentInstance() as any;
    // 数据------------------------------------------------
    interface ResultParams {
        foodBuildTitle?: string;
    }
    // 请求参数
    const resultParams:ResultParams = reactive({});
    // 聊天id
    const messageId : Ref<string> = ref("");
    // 发送聊天内容
    const content : Ref<string> = ref("");
    // 内容详情
    const detail : GPTDetail = reactive({});
    // 聊天列表
    const contentList:Array<GPTDetail> = reactive([
        {
            fromUser: "GPT",
            content: ""
        }
    ]);
    const loading : Ref<boolean> = ref(false);
    const scrollTo : Ref<number> = ref(0);
    // 错误计数
    const errorCount : Ref<number> = ref(10);
    const setTimeoutLoading : Ref<number> = ref(0);
    // 软键盘弹出高度
    const keyboardHeight : Ref<number> = ref(0);
    // 输入框是否聚焦
    const inputFocus : Ref<boolean> = ref(false);
    // 方法------------------------------------------------
    function Utf8ArrayToStr(array : any) {
        let out : any, i : any, len : any, c : any;
        let char2 : any, char3 : any;

        out = "";
        len = array.length;
        i = 0;
        while (i < len) {
            c = array[i++];
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    // 0xxxxxxx
                    out += String.fromCharCode(c);
                    break;
                case 12: case 13:
                    // 110x xxxx   10xx xxxx
                    char2 = array[i++];
                    out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
                    break;
                case 14:
                    // 1110 xxxx  10xx xxxx  10xx xxxx
                    char2 = array[i++];
                    char3 = array[i++];
                    out += String.fromCharCode(((c & 0x0F) << 12) |
                        ((char2 & 0x3F) << 6) |
                        ((char3 & 0x3F) << 0));
                    break;
            }
        }
        return out;
    }
    
    const createMsg = () => {
        if (!loading.value) {
            // uni.showLoading({
            //     title: "正在生成",
            //     mask: true
            // })
            loading.value = true;
        }
        createMessage(resultParams).then((res:any) => {
            messageId.value = res.data;
            getDetail();
        }).catch((err:any) => {
            if (loading.value) {
                loading.value = false;
                // uni.hideLoading();
                proxy.$utils.Tips({
                    title: err.msg || "生成失败"
                }, {
                    tab: 3
                })
            }
        })
    }
    
    const getDetail = (type?:string) => {
        if (!loading.value) {
            // uni.showLoading({
            //     title: "正在生成",
            //     mask: true
            // })
            loading.value = true;
        }
        let data = {
            messageId: messageId.value,
            content: content.value
        }
        if (type !== undefined) {
            data["content"] = type;
        }
        scrollTo.value += 100;
        let lastIndex:number = contentList.length - 1;
        let requestTask = uni.request({
            url: chatGPT(),
            data: data,
            enableChunked: true,
            success: (res:any) => {
                if (res.statusCode === 500 && errorCount.value) {
                    // console.log(errorCount.value);
                    errorCount.value--;
                    getDetail();
                } else {
                    if (loading.value && !setTimeoutLoading.value) {
                        loading.value = false;
                        // uni.hideLoading();
                        if (!errorCount.value && !setTimeoutLoading.value) {
                            uni.showToast({
                                title: "生成失败！",
                                icon: "none"
                            })
                        }
                    }
                }
                // console.log("success--------------------:")
                // console.log(res);
            },
            fail: (err:any) => {
                if (loading.value) {
                    loading.value = false;
                    // uni.hideLoading();
                    uni.showToast({
                        title: err.msg || "生成失败",
                        icon: "none"
                    })
                }
                // console.log("err--------------")
                // console.log(err)
            }
        });
		console.log(111000);
        // #ifdef MP-WEIXIN
        requestTask.onChunkReceived((res : any) => {
            // console.log("-----------------------onChunkReceived");
            if (setTimeoutLoading) {
                clearTimeout(setTimeoutLoading.value);
                setTimeoutLoading.value = 0;
            }
            const arrayBuffer = res.data;
            const uint8Array = new Uint8Array(arrayBuffer);
            // const textDecoder = new TextDecoder("utf-8");
            // const text = textDecoder.decode(uint8Array);
            const text = Utf8ArrayToStr(uint8Array);
            // console.log(res);
            // console.log(uint8Array);
            // console.log(text);
            // const textArr = text.replace(/[\r\n]/g, "").split("data:");
            const textArr = text.split("data:");
			console.log(11444,res);
            for (let i = 0; i < textArr.length; i++) {
                if (!textArr[i].trim()) continue;
                let contentText = JSON.parse(textArr[i].trim()).content;
                contentList[lastIndex].content += contentText;
                scrollTo.value += (contentText.length * 10);
            }
            uni.pageScrollTo({
                scrollTop: scrollTo.value
            })
            // setTimeoutLoading.value = setTimeout(() => {
                if (loading.value) {
                    loading.value = false;
                    // uni.hideLoading();
                }
                setTimeoutLoading.value = 0;
            // }, 300)
            // for (let i = 0; i < text.length; i++) {
            //     console.log(`text---------:2:${i}`, text);
            // }
        });
        // requestTask.onHeadersReceived((res:any) => {
        //     console.log(res, "------------------------onHeadersReceived")
        // })
        // requestTask.offChunkReceived((res:any) => {
        //     console.log(res, "------------------------offChunkReceived")
        // })
        // requestTask.offHeadersReceived((res:any) => {
        //     console.log(res, "------------------------offHeadersReceived")
        // })
        // #endif
    }
    // 发送
    const sendContent = () => {
        if (!content.value) {
            proxy.$utils.Tips({
                title: "请输入您的需求"
            })
            return;
        }
        if (!loading.value) {
            // uni.showLoading({
            //     title: "正在生成",
            //     mask: true
            // })
            loading.value = true;
        }
        contentList.push({
            fromUser: "USER",
            content: content.value
        })
        contentList.push({
            fromUser: "GPT",
            content: ""
        })
        scrollTo.value += (200 + content.value.length * 10);
        getDetail();
        nextTick(() => {
            uni.pageScrollTo({
                scrollTop: scrollTo.value
            })
            content.value = "";
        })
    }
    // 重新获取
    const getRegeneration = () => {
        if (!loading.value) {
            // uni.showLoading({
            //     title: "正在生成",
            //     mask: true
            // })
            loading.value = true;
        }
        for (let a = contentList.length - 1; a >= 0; a--) {
            if (contentList[a].fromUser === "GPT") {
                contentList.splice(a, 1);
                break;
            }
        }
        contentList.push({
            fromUser: "GPT",
            content: ""
        })
        scrollTo.value += 100;
        getDetail("重新生成");
        nextTick(() => {
            uni.pageScrollTo({
                scrollTop: scrollTo.value
            })
        })
    }
    // 继续获取
    const getContinue = () => {
        if (!loading.value) {
            // uni.showLoading({
            //     title: "正在生成",
            //     mask: true
            // })
            loading.value = true;
        }
        contentList.push({
            fromUser: "GPT",
            content: ""
        })
        scrollTo.value += 100;
        getDetail("继续");
        nextTick(() => {
            uni.pageScrollTo({
                scrollTop: scrollTo.value
            })
        })
    }
    // 生命周期---------------------------------------------
    // 加载成功
    onLoad(() => {
        // 键盘高度变化
        uni.onKeyboardHeightChange((e:any) => {
            keyboardHeight.value = e.height;
        });
        let resultMessage = uni.getStorageSync("resultMessage");
        if (resultMessage) {
            try{
                Object.assign(resultParams, JSON.parse(decodeURIComponent(resultMessage)));
                createMsg();
            }catch(e){
                //TODO handle the exception
                proxy.$utils.Tips({
                    title: "生成类型数据错误"
                }, {
                    tab: 3
                })
            }
        } else {
            proxy.$utils.Tips({
                title: "未获取到生成类型数据"
            }, {
                tab: 3
            })
        }
    })
    // #ifdef MP-WEIXIN
    // 分享
    // onShareAppMessage(() => {
    //     return {
    //         title: APP_NAME.valueOf(),
    //         path: `/pages/resultPage/resultPage?resultMessage=${encodeURIComponent(JSON.stringify(resultParams))}`
    //     }
    // })
    // #endif
    onUnload(() => {
        uni.offKeyboardHeightChange();
    })
</script>

<style lang="scss" scoped>
    .body {
        padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom);
    }
    // GPT对话
    .content_item {
        display: flex;
        padding: 34rpx;
        background-color: #F3F3F3;
        
        &.bg_white {
            background-color: #FFFFFF;
        }
        
        .avatar {
            width: 76rpx;
            height: 76rpx;
            overflow: hidden;
            font-size: 0;
            border-radius: 14rpx;
            flex-shrink: 0;
            margin-right: 42rpx;
            margin-top: 6rpx;
        }
        
        .content {
            font-size: 22rpx;
            line-height: 34rpx;
            word-break: break-all;
        }
    }
    // 底部生成按钮
    .top_generate {
        height: 168rpx;
    }
    .generate_row {
        height: 168rpx;
        width: 100%;
        position: fixed;
        left: 0;
        display: flex;
        align-items: center;
        justify-content: space-around;
        z-index: 9;
        bottom: calc(124rpx + constant(safe-area-inset-bottom));
        bottom: calc(124rpx + env(safe-area-inset-bottom));
    }
    .generate_btn {
        width: 320rpx;
        height: 104rpx;
        background-color: #FFFFFF;
        border-radius: 14rpx;
        display: flex;
        align-items: center;
        flex-direction: column;
        justify-content: center;
        box-shadow: 0rpx 6rpx 12rpx 0rpx rgba(103, 103, 103, .1);
    }
    .btn_title {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 10rpx;
    }
    .title_icon {
        font-size: 0;
        width: 30rpx;
        height: 30rpx;
        margin-right: 12rpx;
    }
    .title_text {
        font-family: "AlibabaPuHuiTi-2-85-Bold";
        font-size: 26rpx;
        line-height: 26rpx;
        color: #101010;
    }
    .btn_subtitle {
        font-family: "AlibabaPuHuiTi-2-55-Regular";
        font-size: 18rpx;
        line-height: 18rpx;
        color: #929292;
    }
    // 底部输入框
    .safe_footer {
        height: 124rpx;
    }
    .keyboard_height {
        transition-duration: 225ms;
    }
    .footer_wrapper {
        transition-duration: 225ms;
        position: fixed;
        width: 100%;
        bottom: 0;
        left: 0;
        background-color: #F3F3F3;
        box-shadow: 0rpx 6rpx 18rpx 0rpx rgba(0, 0, 0, .1);
        padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom);
    }
    .footer_row {
        // height: 124rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 18rpx 0;
    }
    .footer_input {
        border: 2rpx solid #E0E0E0;
        border-radius: 18rpx;
        width: 678rpx;
        min-height: 88rpx;
        // max-height: 464rpx;
        max-height: 206rpx;
        background-color: #FFFFFF;
        overflow: auto;
        padding: 16rpx 0;
        
        textarea {
            width: 100%;
            min-height: 52rpx;
            // line-height: 48rpx;
            line-height: 34rpx;
            font-size: 28rpx;
            padding: 2rpx 40rpx;
            box-sizing: border-box;
        }
    }
    // loading
    .loading_mask {
        position: fixed;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        z-index: 999;
    }
    .loading_row {
        position: absolute;
        left: 40rpx;
        height: 70rpx;
        width: 670rpx;
        display: flex;
        padding: 20rpx;
        border-radius: 14rpx;
        background-color: #ffffff;
        box-shadow: 0rpx 6rpx 12rpx 0rpx rgba(103, 103, 103, .1);
        font-size: 0;
    }
    .loading_text {
        font-family: "AlibabaPuHuiTi-2-85-Regular";
        font-size: 28rpx;
        line-height: 28rpx;
        color: #030303;
        margin-left: 20rpx;
    }
</style>