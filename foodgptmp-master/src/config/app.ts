let HTTP_REQUEST_URL:String = import.meta.env.VITE_BASE_URL;
let APP_NAME:String = import.meta.env.VITE_TITLE;
const HEADER:Object = {
    'content-type': 'application/json'
};
// 回话密钥名称 请勿修改此配置
const TOKENNAME:String = 'X-Token';
export {
    HTTP_REQUEST_URL,
    HEADER,
    TOKENNAME,
    APP_NAME
}
