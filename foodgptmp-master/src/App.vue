<script setup lang="ts">
    import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
    import { global } from '@/store/pinia/index';

    // #ifdef MP-WEIXIN
    const menuButton = uni.getMenuButtonBoundingClientRect();
    // #endif
    // #ifndef H5
    const systemInfo = uni.getSystemInfoSync();
    // #endif
    const count:Ref<number> = ref(0);
    onLaunch(() => {
        let token = uni.getStorageSync("TOKEN");
        if (token) {
            // global().updateToken(token);
            global().updateUserinfo();
            global().token = uni.getStorageSync("TOKEN");
        }else{
            global().updateToken();
        }
        // #ifdef MP-WEIXIN
        autoUpdate();
        // #endif
        uni.loadFontFace({
            global: true,
            family: 'AlibabaPuHuiTi-2-85-Bold',
            source: 'url("https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-85-Bold/AlibabaPuHuiTi-2-85-Bold.ttf")',
            complete() {
                count.value++;
            }
        })
        uni.loadFontFace({
            global: true,
            family: 'AlibabaPuHuiTi-2-55-Regular',
            source: 'url("https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-55-Regular/AlibabaPuHuiTi-2-55-Regular.ttf")',
            complete() {
                count.value++;
            }
        })
        uni.loadFontFace({
            global: true,
            family: 'AlibabaPuHuiTi-2-65-Medium',
            source: 'url("https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-65-Medium/AlibabaPuHuiTi-2-65-Medium.ttf")',
            complete() {
                count.value++;
            }
        })
        uni.loadFontFace({
            global: true,
            family: 'AlibabaPuHuiTi-2-75-SemiBold',
            source: 'url("https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-75-SemiBold/AlibabaPuHuiTi-2-75-SemiBold.ttf")',
            complete() {
                count.value++;
            }
        })
        uni.loadFontFace({
            global: true,
            family: 'AlibabaPuHuiTi-2-95-ExtraBold',
            source: 'url("https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-95-ExtraBold/AlibabaPuHuiTi-2-95-ExtraBold.ttf")',
            complete() {
                count.value++;
            }
        })
    });

    watch(count,(nol,vol)=>{
        if(nol===1){
            uni.$emit("FontReady")
        }
    })

    onShow(() => {
        console.log("App Show");
    });
    onHide(() => {
        console.log("App Hide");
    });
    // 全局参数
    nextTick(() => {
        // #ifdef MP-WEIXIN
        global().NavSafeHeight = systemInfo.safeArea.top;
        global().NavHeight = menuButton.bottom + menuButton.top - systemInfo.safeArea.top * 2;
        global().NavSafeRight = systemInfo.windowWidth - menuButton.left;
        // #endif
        // #ifdef H5
        global().NavSafeHeight = 0;
        global().NavHeight = 44;
        // #endif
        // #ifdef APP-PLUS
        global().NavSafeHeight = systemInfo.safeArea.top;
        global().NavHeight = 44;
        // #endif
    })

    // #ifdef MP-WEIXIN
    function autoUpdate() { // 监听小程序版本更新
        // 获取小程序更新机制兼容
        if (uni.canIUse('getUpdateManager')) {
            const updateManager = uni.getUpdateManager()
            // 检查是否有新版本发布
            updateManager.onCheckForUpdate(function (res) {
                if (res.hasUpdate) {
                    //小程序有新版本，则静默下载新版本，做好更新准备
                    updateManager.onUpdateReady(function () {
                        uni.showModal({
                            title: '更新提示',
                            content: '新版本已经准备好，是否重启应用？',
                            success: function (res) {
                                if (res.confirm) {
                                    //新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                                    updateManager.applyUpdate()
                                } else if (res.cancel) {
                                    //如果需要强制更新，则给出二次弹窗，如果不需要，则这里的代码都可以删掉了
                                    uni.showModal({
                                        title: '温馨提示',
                                        content: '我们已经做了新的优化，请及时更新哦~',
                                        showCancel: false, //隐藏取消按钮，也可显示，取消会走res.cancel，然后从新开始提示
                                        success: function (res) {
                                            //第二次提示后，强制更新
                                            if (res.confirm) {
                                                // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                                                updateManager.applyUpdate()
                                            } else if (res.cancel) {
                                                //重新回到版本更新提示
                                                autoUpdate()
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    })
                    // 新的版本下载失败
                    updateManager.onUpdateFailed(function () {
                        uni.showModal({
                            title: '温馨提示',
                            content: '新版本已经上线，请您删除当前小程序，重新搜索打开',
                        })
                    })
                }
            })
        } else {
            // 提示用户在最新版本的客户端上体验
            uni.showModal({
                title: '温馨提示',
                content: '当前微信版本过低，可能无法使用该功能，请升级到最新版本后重试。'
            })
        }
    }
    // #endif
</script>
<style lang="scss">
    @import "static/css/icon.scss";
    @import "static/css/public.scss";

    /*每个页面公共css */
    view {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body {
        max-width: 100vw;
        background-color: #fff;
        padding-bottom: constant(safe-area-inset-bottom);
        /* 兼容 iOS < 11.2 */
        padding-bottom: env(safe-area-inset-bottom);
        /* 兼容 iOS >= 11.2 */
    }

    view,
    input {
        box-sizing: border-box;
    }

    image {
        width: 100%;
        height: 100%;
        vertical-align: middle;
    }

    ::-webkit-scrollbar {
        /*全局隐藏滚动条*/
        width: 0;
        height: 0;
        color: transparent;
    }

    scrol-view::-webkit-scrollbar {
        /*全局隐藏滚动条*/
        width: 0;
        height: 0;
        color: transparent;
    }

    .plo {
        /* #ifdef MP-WEIXIN */
        min-height: 1;
        line-height: 1;
        /* #endif */
        position: absolute;
        @include font(#C3C3C3, 28rpx);
    }

    ._stop {
        pointer-events: none;
        filter: grayscale(100%);
    }
</style>