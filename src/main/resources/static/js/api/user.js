import Vue from 'vue'

const makeAdmin = Vue.resource('/user{/id}/makeadmin')
const giveSubscription = Vue.resource('/user{/id}/givesubscription')
const giveAccessToBook = Vue.resource('/user{/id}/give_access_to_book')

export default {
    update: (usr) => makeAdmin.update({id: usr.id}, {}),
    setSubscription: (usr) => giveSubscription.update({id: usr.id}, {}),
    setAccessToBook: (usr, idBooks) => giveAccessToBook.update({id: usr.id}, idBooks),
}