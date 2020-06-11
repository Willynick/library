import Vue from 'vue'

const profile = Vue.resource('/user/profile')
const files = Vue.resource('/user/profile/file')

export default {
    update: user => profile.update(user),
    updateFile: (file) => files.update(file)
}