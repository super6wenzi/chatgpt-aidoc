import { createSSRApp } from "vue";
import uView from 'vk-uview-ui'
import utils from './utils/utils'
import Mixin from './utils/mixin'
import App from "./App.vue";

const pinia = createPinia();

export function createApp() {
	const app = createSSRApp(App);
	app.use(uView);
	app.use(pinia);
	app.mixin(Mixin);
	app.config.globalProperties.$utils = utils;
	return {
		app,
	};
}