// 品牌
declare interface BrandDetail {
    id?: string;
    address?: string; // 餐厅地址
    category?: string; // 主营品类
    characteristic?: string; // 品牌特色
    endCustomerGroups?: number; // 客户群体
    name?: string; // 品牌名称
    passengerFlow?: number; // 客流量
    peakSeason?: string; // 客流旺季
    price?: number; // 客单价
    startCustomerGroups?: number; // 起始客户群体
}
// 品牌类型
declare interface BrandTypeDetail {
    id?: string;
    title?: string;
    name?: string;
    buildType?: Array<BuildTypeDetail>;
}
// 品牌类型 生成类型 详情
declare interface BuildTypeDetail {
    id?: string;
    title?: string;
    icon?: string;
	isShowNext?: string;
}
// gtp详情
declare interface GPTDetail {
    id?: string;
    content?: string;
    collected?: boolean;
    fromUser?: string;
}
// video详情
declare interface VideoDetail {
    id?: string;
    url?: string;
    title?: boolean;
}
// 表单详情
declare interface FormDetail {
    name: string;
    type: string; // 1：输入框 2：下拉选项
    selectVOList?: Array<{
        name: string;
        value: string;
    }>;
}