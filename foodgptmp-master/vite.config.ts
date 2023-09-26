import { defineConfig } from 'vite';
import path from 'path';
// 自动导入vue中hook reactive ref等
import AutoImport from 'unplugin-auto-import/vite';
import uni from '@dcloudio/vite-plugin-uni';
const resolve = (dir: string) => path.join(__dirname, dir);

// https://vitejs.dev/config/
export default defineConfig({
    resolve: {
        alias: {
            '~@': resolve('src'),
            comps: resolve('src/components'),
            apis: resolve('src/apis'),
            views: resolve('src/views'),
            utils: resolve('src/utils'),
            routes: resolve('src/routes'),
            styles: resolve('src/styles')
        }
    },
    plugins: [
        uni(),
        AutoImport({
            include: [
                /\.[tj]sx?$/, //.ts,.tsx,.js,.jsx
                /\.vue$/,
                /\.vue\?vue/, //.vue
                /\.md$/
            ],
            //安装两行后你会发现在组件中不用再导入ref，reactive等
            imports: ['vue', 'vue-router', 'pinia','uni-app'],
            //存放的位置
            dts: 'src/auto-import.d.ts'
        })
    ],
    server: {
        // proxy: {
        //     "/api": {
        //         target: "",
        //         // target: "",
        //         changeOrigin: true,
        //         rewrite: (path) => path.replace(/^\/api/, "/api")
        //     }
        // }
    }
});
