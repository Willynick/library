import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import 'api/resource'
import router from 'router/router'
import App from 'pages/App.vue'
import store from 'store/store'
import { connect } from './util/ws'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'vuetify/dist/vuetify.min.css'
import VueConfetti from 'vue-confetti'

if (frontendData.profile) {
    connect();
}

Vue.use(Vuetify)
Vue.use(BootstrapVue)
Vue.use(VueConfetti)

new Vue({
    el: '#app',
    store,
    router,
    render: a => a(App),
    vuetify: new Vuetify({})
})
