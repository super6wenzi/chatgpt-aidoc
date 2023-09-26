<template>
    <view class="body">
        <foodNav :title="APP_NAME"></foodNav>
        <view class="body-step">
            <block v-for="(item,index) in stepList" :key="index">
                <view class="body-step-item" :class="{ '_active': stepIndex===index }">
                    <view class="body-step-item-title">{{item.title}}</view>
                    <view class="body-step-item-side">{{index + 1}}</view>
                </view>
                <view class="body-step-block" v-if="index<2">
                    <view class="body-step-block-item"></view>
                    <view class="body-step-block-item"></view>
                    <view class="body-step-block-item"></view>
                    <view class="body-step-block-item"></view>
                    <view class="body-step-block-item"></view>
                </view>
            </block>
        </view>
        <blcok v-if="stepIndex===0">
            <view class="body-title">你的餐饮品牌名称</view>
            <input class="body-int" v-model="from.name" type="text" placeholder-class="plo" placeholder="例: 小龙坎" 
                placeholder-style="font-size: 35rpx;font-weight: bold;">
            <view class="body-title _marginTop">
                您的主营品类
                <label>最多可填3类，至少填1类</label>
            </view>
            <input class="body-int" v-model="from.category[0]" type="text" placeholder-class="plo" placeholder="例：正宗重庆火锅" 
                placeholder-style="font-size: 35rpx;font-weight: bold;">
            <input class="body-int" v-model="from.category[1]" type="text" placeholder-class="plo" placeholder="例：当天现杀牛蛙" 
                placeholder-style="font-size: 35rpx;font-weight: bold;">
            <input class="body-int" v-model="from.category[2]" type="text" placeholder-class="plo" placeholder="例：椰子甜品" 
                placeholder-style="font-size: 35rpx;font-weight: bold;">
            <view class="body-title _marginTop">餐厅的地址</view>
             <!-- @tap="cityIn" -->
            <input class="body-int" v-model="from.address" type="text" placeholder-class="plo" placeholder="例：南京玄武区" 
                placeholder-style="font-size: 35rpx;font-weight: bold;">
        </blcok>
        <blcok v-if="stepIndex===1">
            <view class="body-wrap">
                <view class="body-wrap-item">
                    <label>客流量（日/人）</label>
                    <input class="body-wrap-item-int" v-model="from.passengerFlow" placeholder="例：10" type="number" 
                        placeholder-style="font-size: 35rpx;font-weight: bold;">
                </view>
                <view class="body-wrap-item">
                    <label>客单价（元/人）</label>
                    <input class="body-wrap-item-int" v-model="from.price" placeholder="例：10.00" type="digit" 
                        placeholder-style="font-size: 35rpx;font-weight: bold;">
                </view>
            </view>
            <view class="body-wrap">
                <view class="body-wrap-item">
                    <label>客户群体（岁）</label>
                    <input class="body-wrap-item-int" v-model="from.startCustomerGroups" placeholder="例：5" type="number" 
                        placeholder-style="font-size: 35rpx;font-weight: bold;">
                </view>
                <view class="body-wrap-item">
                    <label style="opacity: 0;">占位</label>
                    <input class="body-wrap-item-int" :disabled="!from.startCustomerGroups" v-model="from.endCustomerGroups" :min="from.startCustomerGroups" 
                        placeholder="例：10" type="number" placeholder-style="font-size: 35rpx;font-weight: bold;">
                </view>
            </view>
            <view class="body-wrap">
                <view class="body-wrap-item solar">
                    <label>客户群体性别</label>
                    <view class="solar-side solar-sex">
                        <view class="solar-side-item" :class="{ '_choose': from.customerSex === '男' }" @tap="from.customerSex = '男'">
                            男
                        </view>
                        <view class="solar-side-item" :class="{ '_choose': from.customerSex === '女' }" @tap="from.customerSex = '女'">
                            女
                        </view>
                        <view class="solar-side-item" :class="{ '_choose': from.customerSex === '全部' }" @tap="from.customerSex = '全部'">
                            全部
                        </view>
                    </view>
                </view>
            </view>
            <view class="body-wrap">
                <view class="body-wrap-item solar">
                    <label>客流旺季</label>
                    <view class="solar-side">
                        <view class="solar-side-item" :class="{ '_choose': from.peakSeason.includes(String(item)) }" v-for="(item,index) in 12" :key="index"
                            @tap="monthIn(item)">
                            {{item}}<label>月</label>
                        </view>
                    </view>
                </view>
            </view>
        </blcok>
        <blcok v-if="stepIndex===2">
            <view class="body-spot">品牌亮点</view>
            <view class="body-dec">观点仅供参考：您的品牌定位//您品牌与同类的区别/您的核心竞争力/您的特色产品及介绍/您希望传达怎样的品牌价值</view>
            <textarea class="body-textin" v-model="from.characteristic" maxlength="9999" placeholder="可对您的品牌亮点进行详细的描述，描述的越详细易懂，生成结果会越好" 
                placeholder-class="plo"></textarea>
        </blcok>
        <view class="body-submit" @tap="next">{{stepIndex==2?"去生成":"下一步"}}</view>
        <view class="safe_footer"></view>
    </view>
    <!-- 地址选择器 -->
    <u-picker v-model="showCity" @confirm="cityTake" mode="region"></u-picker>
</template>

<script lang="ts" setup>
    import { addBrand, getBrandDetail } from "@/api/api";
    import { APP_NAME } from "@/config/app";
    // import { global } from "@/store/pinia/index";
    // 全局配置---------------------------------------------
	const { proxy } = getCurrentInstance() as any;
    // 数据------------------------------------------------
    const id:Ref<string> = ref("");
    const stepList:Array<object> = reactive([
        {title: "基础资料"},
        {title: "详细亮点 "},
        {title: "生成选择"},
    ])
    const stepIndex:Ref<number> = ref(0);
    // 表单数据
    // {[key:string]:string|Array<string>}
    const from:any = reactive({
        name: "",
        category: [],
        address: "",
        price: "",
        customerSex: "",
        peakSeason: [],
        passengerFlow: "",
        startCustomerGroups: "",
        endCustomerGroups: "",
        characteristic: ""
    })
    // 方法------------------------------------------------
    
    // 地址选择器
    const showCity:Ref<boolean> = ref(false);
    // 是否显示地址选择器
    function cityIn(){
        if(!from.address){
            showCity.value = true;
            return
        }
        let reg='(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.             +岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)'
        if(from.address.match(reg).length<4){
            showCity.value = true;
            return
        }
    }
    // 地址选择器回调事件
    function cityTake(e:any){
        from.address = `${e.province.name}-${e.city.name}-${e.area.name}`
    }
    
    // 旺季选择
    function monthIn(e:any){
        let idx = from.peakSeason.indexOf(String(e))
        if(idx > -1){
            from.peakSeason.splice(idx, 1);
        } else {
            from.peakSeason.push(String(e))
        }
    }
    
    function next(){
        if(!from.name)return proxy.$utils.Tips({title: "请输入你的餐饮品牌名称!"});
        if(from.category.join("|")==="")return proxy.$utils.Tips({title: "请输入您的主营品类!"});
        if(!from.address)return proxy.$utils.Tips({title: "请输入餐厅的地址!"});
        if(stepIndex.value===0){
            stepIndex.value++;
            return
        }
        if(!from.passengerFlow)return proxy.$utils.Tips({title: "请输入客流量!"});
        if(!from.price)return proxy.$utils.Tips({title: "请输入客单价!"});
        if(!from.startCustomerGroups&&!from.endCustomerGroups)return proxy.$utils.Tips({title: "请输入客户群体!"});
        if(!from.customerSex)return proxy.$utils.Tips({title: "请选择客户群体性别!"});
        if(from.peakSeason.length<=0)return proxy.$utils.Tips({title: "请选择客流旺季!"});
        if(stepIndex.value===1){
            stepIndex.value++;
            return
        }
        if(!from.characteristic)return proxy.$utils.Tips({title: "请输入品牌亮点!"});
        from.category = from.category.join("|");
        from.peakSeason = from.peakSeason.join("|");
        if (id.value) {
            from.id = id.value;
        }
        addBrand(from).then((res:any)=>{
            uni.$emit("RefreshBrand");
            proxy.$utils.Tips({
                title: res.msg||"生成成功!"
            }, {
                tab: 5,
                url: `/pages/chooseType/chooseType?id=${res.data}`
            });
        }).catch((err:any)=>{
            proxy.$utils.Tips({title: err.msg||"生成失败!"});
        })
    }
    // 获取详情
    function getDetail() {
        uni.showLoading({
            title: "正在加载",
            mask: true
        })
        getBrandDetail(id.value).then((res:any) => {
            uni.hideLoading();
            let data = res.data;
            data.category = data.category.split("|");
            data.peakSeason = data.peakSeason.split("|");
            Object.assign(from, data)
        }).catch((err:any) => {
            uni.hideLoading();
            proxy.$utils.Tips({
                title: err.msg || "系统错误"
            })
        })
    }
    // 生命周期---------------------------------------------
    // 加载成功
    onLoad((options) => {
        if (options.id) {
            id.value = options.id;
            getDetail();
        }
    })
</script>

<style lang="scss" scoped>
@import "./style.scss";
</style>
