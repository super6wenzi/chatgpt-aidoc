import utils from './utils'
// import scrollReveal from "scrollreveal";
import {
    // upload,
    // getUser,
    // updateUser,
} from '@/api/api';
import { global } from '@/store/pinia/index';

const Mixin = {
	data() {},
	methods: {
        /**
         * 页面跳转
         * @author LHB 2024-04-13
         * @parm url => 要跳转的链接
         * @parm type => 跳转类型
         */
		PATH_TO(url:string,type:number = 1) {
			if (!url) return;
            let uncheck = [
                // "/pages/articleDetail/articleDetail",
            ]
            if(!uncheck.includes(url.replace(/[?].*/, ""))&&!uni.getStorageSync("TOKEN")){
                return utils.Tips({title: "请登录后操作!", endtime: 1000},{tab: 2,url: "/pages/login/login"});
            }
            switch(type){
                case 1:
                    uni.navigateTo({
                        url: url
                    })
                    break;
                case 2:
                    uni.redirectTo({
                        url: url
                    })
                    break;
                case 3:
                    uni.reLaunch({
                        url: url
                    })
                    break;
                case 4:
                    uni.navigateBack();
                    break;
                case 5:
                    uni.switchTab({
                        url: url
                    })
                    break;
            }
		},
        /**
         * 预览图片
         * @author LHB 2024-04-13
         * @parm list => 图片列表数组形式
         * @parm index => 展示第几张图片
         */
        PRVIEW_IMG (list:Array<string>,index:number){
            let imgList = [];
            for(const i of list){
                if(i.indexOf("http") > -1 || i.indexOf("https") > -1){
                    imgList.push(i)
                }else{
                    imgList.push(`${this.$baseUrl}${i}`)
                }
            }
            uni.previewImage({
                urls: imgList,
                current: index,
                indicator: "number"
            })
        },


        /**
         * 上传图片
         * @author LHB 2024-04-13
         * @parm count => 一次可上传数量 number
         * @parm from => 选取范围 相册、相机 arry
         * @parm imgType => 需要校验的类型 arry
         * @parm imgSize => 需要校验的大小 number
         */
        UPLOAD_IMG(count:number=1,from:Array<string>=['album','camera'],imgType:Array<string>,imgSize:number=5){
            let maxSize = imgSize * 1024 * 1024;
            return new Promise((reslove:any, reject:any) => {
                uni.chooseImage({
                    count: count,
                    sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album','camera'], //从相册选择
                    success(res:any) {
                        // // 校验图片格式
                        // if(imgType.length>0&&!imgType.includes(res.tempFilePaths[0].split(".")[1])){
                        //     // uni.hideLoading();
                        //     reject({msg: "请选择正确的图片格式!"});
                        // }
                        // // 校验图片质量
                        // if(imgSize&&res.tempFiles[0].size>maxSize){
                        //     // uni.hideLoading();
                        //     reject({msg: `图片大小不得超过${imgSize}M!`});
                        // }
                        // uni.showLoading({
                        //     mask: true,
                        //     title: "上传中..."
                        // })
                        // uni.uploadFile({
                        //     url: upload(),
                        //     // #ifndef H5
                        //     filePath: res.tempFilePaths[0],
                        //     // #endif
                        //     // #ifdef H5
                        //     file: res.tempFiles[0],
                        //     // #endif
                        //     name: "file",
                        //     header: {
                        //         // #ifndef H5
                        //         "Content-Type": "multipart/form-data",
                        //         // #endif
                        //         "token": uni.getStorageSync("TOKEN")
                        //     },
                        //     success(result) {
                        //         uni.hideLoading();
                        //         let rest = JSON.parse(result.data);
                        //         reslove({
                        //             type: "image",
                        //             url: rest.url,
                        //             name: rest.url.substring(rest.url.lastIndexOf('/')+1,rest.url.lastIndexOf('.')),
                        //         })
                        //         // if(rest.code==200){
                        //         //     reslove({
                        //         //         type: "image",
                        //         //         url: rest.url,
                        //         //         name: rest.url.substring(rest.url.lastIndexOf('/')+1,rest.url.indexOf('.')),
                        //         //     })
                        //         // }else{
                        //         //     reject({msg: "图片上传失败!"});
                        //         // }
                        //     },
                        //     fail() {
                        //         uni.hideLoading();
                        //         reject({msg: "图片上传失败!"});
                        //     }
                        // })
                    }
                })
            })
        },
        /**
         * 上传文件
         * @author LHB 2024-04-13
         * @parm count => 一次可上传数量 number
         * @parm imgType => 需要校验的类型 arry
         * @parm imgSize => 需要校验的大小 number
         */
        UPLOAD_FILE(count:number=1,imgType:Array<string>=["pdf"],imgSize:number=5){
            let maxSize = imgSize * 1024 * 1024;
            return new Promise((reslove, reject) => {
                wx.chooseMessageFile({
                    count: 1,
                    type: "file",
                    extension: imgType,
                    success(res:any) {
                        // // 校验文件格式
                        // if(imgType.length>0&&!imgType.includes(res.tempFiles[0].path.split(".")[1])){
                        //     // uni.hideLoading();
                        //     reject({msg: "请选择正确的文件格式!"});
                        // }
                        // // 校验图片质量
                        // if(imgSize&&res.tempFiles[0].size>maxSize){
                        //     // uni.hideLoading();
                        //     reject({msg: `文件大小不得超过${imgSize}M!`});
                        // }
                        // uni.showLoading({
                        //     mask: true,
                        //     title: "上传中..."
                        // })
                        // uni.uploadFile({
                        //     url: upload(),
                        //     // #ifndef H5
                        //     filePath: res.tempFiles[0].path,
                        //     // #endif
                        //     // #ifdef H5
                        //     file: res.tempFiles[0].path,
                        //     // #endif
                        //     name: "file",
                        //     header: {
                        //         "Content-Type": "multipart/form-data",
                        //         "token": uni.getStorageSync("TOKEN")
                        //     },
                        //     success(result) {
                        //         uni.hideLoading();
                        //         let rest = JSON.parse(result.data);
                        //         reslove({
                        //             type: res.tempFiles[0].type||"file",
                        //             url: rest.url,
                        //             name: rest.url.substring(rest.url.lastIndexOf('/')+1,rest.url.lastIndexOf('.')),
                        //         })
                        //         // if(rest.code==200){
                        //         //     reslove({
                        //         //         type: res.tempFiles[0].type||"file",
                        //         //         url: rest.url,
                        //         //         name: rest.url.substring(rest.url.lastIndexOf('/')+1,rest.url.indexOf('.')),
                        //         //     })
                        //         // }else{
                        //         //     reject({msg: "文件上传失败!"});
                        //         // }
                        //     },
                        //     fail() {
                        //         uni.hideLoading();
                        //         reject({msg: "文件上传失败!"});
                        //     }
                        // })
                    },
                    fail(){
                        // uni.hideLoading();
                    }
                })
            })
        },
        /**
         * 上传文件-兼容H5 H5端的图片文件混合直接走这边 单图片上传直接用上面的图片上传
         * @author LHB 2024-04-13
         * @parm count => 一次可上传数量 number
         * @parm imgType => 需要校验的类型 arry
         * @parm imgSize => 需要校验的大小 number
         */
        UPLOAD_MEDIA(count:number=1,mediaType:Array<string>=["jpg","pdf"],imgSize:number=5){
            // let maxSize = imgSize * 1024 * 1024;
            return new Promise((reslove, reject) => {
                uni.chooseFile({
                  count: count, //默认100
                  extension: mediaType,
                	success: function (res:any) {
                        console.log(res.tempFiles[0])
                        // // 校验文件格式
                        // if(mediaType.length>0&&!mediaType.includes(res.tempFiles[0].type.split("/")[1])){
                        //     // uni.hideLoading();
                        //     reject({msg: "请选择正确的文件格式!"});
                        // }
                        // // 校验图片质量
                        // if(imgSize&&res.tempFiles[0].size>maxSize){
                        //     // uni.hideLoading();
                        //     reject({msg: `文件大小不得超过${imgSize}M!`});
                        // }
                        // uni.showLoading({
                        //     mask: true,
                        //     title: "上传中..."
                        // })
                        // uni.uploadFile({
                        //     url: upload(),
                        //     // #ifndef H5
                        //     filePath: res.tempFiles[0].path,
                        //     // #endif
                        //     // #ifdef H5
                        //     file: res.tempFiles[0].path,
                        //     // #endif
                        //     name: "file",
                        //     header: {
                        //         "Content-Type": "multipart/form-data",
                        //         "token": uni.getStorageSync("TOKEN")
                        //     },
                        //     success(result) {
                        //         uni.hideLoading();
                        //         let rest = JSON.parse(result.data);
                        //         reslove({
                        //             type: res.tempFiles[0].type.split("/")[0].indexOf("image")>=0?"image":"file",
                        //             url: rest.url,
                        //             name: rest.url.substring(rest.url.lastIndexOf('/')+1,rest.url.lastIndexOf('.')),
                        //         })
                        //         // if(rest.code==200){
                        //         //     reslove({
                        //         //         type: res.tempFiles[0].type.split("/")[0].indexOf("image")>=0?"image":"file",
                        //         //         url: rest.url,
                        //         //         name: rest.url.substring(rest.url.lastIndexOf('/')+1,rest.url.indexOf('.')),
                        //         //     })
                        //         // }else{
                        //         //     reject({msg: "文件上传失败!"});
                        //         // }
                        //     },
                        //     fail() {
                        //         uni.hideLoading();
                        //         reject({msg: "文件上传失败!"});
                        //     }
                        // })
                	}
                });
            })
        },
        /**
         * 更新用户信息
         * @author LHB 2024-04-13
         * @parm obj 入参 avatar nickname
         */
        UPDATEUSER(){
            // this.UPLOAD_IMG(1,[],["jpg","png"],5).then((res:any)=>{
            //     uni.showLoading({
            //         title: "更新中..."
            //     })
            //     updateUser({
            //         "avatar": res.url
            //     }).then((result:any)=>{
            //         uni.hideLoading();
            //         if(result.code===200){
            //             this.GETUSUER();
            //             utils.Tips({title: "更新成功!"},"");
            //         }else{
            //             utils.Tips({title: res.msg||"更新用户信息失败"},"");
            //         }
            //     }).catch((err:any)=>{
            //         uni.hideLoading();
            //         utils.Tips({title: err.msg||"更新用户信息失败"},"");
            //     })
            // }).catch((err:any)=>{
            //     uni.hideLoading();
            // })
        },
        /**
         * 获取用户信息
         * @author LHB 2024-04-13
         * @parm token
         */
        GETUSUER(){
        //     getUser().then((res:any)=>{
        //         if(res.code===200){
        //             global().userInfo = {...res.data};
        //         }else{
        //             utils.Tips({title: res.msg||"登录失败!"},"");
        //         }
        //     }).catch((err:any)=>{
        //         utils.Tips({title: err.msg||"登录失败!"},"");
        //     })
        },
	}
};

export default Mixin;