import Vue from 'vue'
import VueRouter from 'vue-router'
import MessagesList from 'pages/MessageList.vue'
import Auth from 'pages/Auth.vue'
import Profile from 'pages/Profile.vue'
import UserList from 'pages/UserList.vue'
import UserBooks from 'pages/UserBooks.vue'
import ShoppingCart from 'pages/ShoppingCart.vue'
import Main from 'pages/Main.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/', component: Main },
    { path: '/books', component: MessagesList },
    { path: '/auth', component: Auth },
    { path: '/profile', component: Profile },
    { path: '/users/list', component: UserList},
    { path: '/user/books', component: UserBooks},
    { path: '/shopping_cart', component: ShoppingCart},
    { path: '*', component: MessagesList },
]

export default new VueRouter({
    mode: 'history',
    routes
})
