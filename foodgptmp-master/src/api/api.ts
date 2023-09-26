import { HTTP_REQUEST_URL } from "@/config/app";
import request from "@/utils/request";

// /**
//  * 获取列表
//  * @param {Object} data (必填)
//  *  @property {String} cate (非必填) 分类Id
//  *  @property {String} city (非必填) 城市Id
//  *  @property {String | int} current (非必填) 分页参数
//  *  @property {String} isHot (非必填) 是否热门推荐
//  *  @property {String | int} size (非必填) 分页参数
// */
// export function getProductList(data:object) {
// 	return request.get(`product/getList`, data, {noAuth: true});
// }

// gpt接口------------------------------------------------------

/**
 * 添加品牌
 * @param {Object} data (必填)
 *  @property {String} brandId (必填) 品牌id
 *  @property {String} foodBuildId (必填) 生成的类型id
*/
export function createMessage(data:Object) {
	return request.post(`chatGPT/createMessage`, data);
}

/**
 * 生成gpt - 获取 生成gpt api
*/
export function chatGPT() {
	return (HTTP_REQUEST_URL + '/api/chatGPT/sse');
}

// 品牌---------------------------------------------------------

/**
 * 添加品牌
 * @param {Object} data (必填)
 *  @property {String} address (非必填) 餐厅地址
 *  @property {String} category (非必填) 主营品类
 *  @property {String} characteristic (非必填) 品牌特色
 *  @property {String} endCustomerGroups (非必填) 客户群体
 *  @property {String} name (非必填) 品牌名称
 *  @property {String} passengerFlow (非必填) 客流量
 *  @property {String} peakSeason (非必填) 客流旺季
 *  @property {String} price (非必填) 客单价
 *  @property {String} startCustomerGroups (非必填) 起始客户群体
*/
export function addBrand(data:Object) {
	return request.post(`foodBrand/addBrand`, data);
}

/**
 * 获取列表
 * @param {Object} data (必填)
 *  @property {String | int} current (非必填) 分页参数
 *  @property {String | int} size (非必填) 分页参数
*/
export function getBrandList(data:Object) {
	return request.get(`foodBrand/getList`, data);
}

/**
 * 获取列表
 * @param {String} id (非必填) id
*/
export function getBrandDetail(id:String) {
	return request.get(`foodBrand/getInfoById`, { id });
}

// 生成分类-----------------------------------------------------

/**
 * 获取品牌应填写的表单
 * @param {string} buildTypeId (必填) id
*/
export function getTypeForm(buildTypeId:string) {
	return request.get(`foodBuildType/getForm`, { buildTypeId });
}

/**
 * 获取列表
*/
export function getBuildTypeList() {
	return request.get(`foodBuildType/getList`, {}, {noAuth: true});
}

// 用户登录接口--------------------------------------------------

/**
 * 获取用户详情
*/
export function getUserInfo() {
	return request.get(`user/getUserInfo`);
}

/**
 * 完善用户信息
 * @param {Object} data (必填)
 *  @property {String} avatar (非必填) 
 *  @property {String} nick (非必填) 
*/
export function updateUserInfo(data:Object) {
	return request.get(`user/updateUserInfo`, data);
}

/**
 * 完善用户信息
 * @param {String} code (必填) 
*/
export function wxLogin(code:String) {
	return request.get(`user/wxLogin`, { code }, {noAuth: true});
}

/**
 * 文件管理 - 获取 上传文件 api
*/
export function upload() {
	return (HTTP_REQUEST_URL + '/api/file/webupload/upload');
}