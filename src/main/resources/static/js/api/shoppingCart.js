import Vue from 'vue'


const shoppingCart = Vue.resource('/shoppingCart{/id}')
const buyBooks = Vue.resource('/shoppingCart/buy_books')
const findTotalPrice = Vue.resource('/shoppingCart/find_total_price')

export default {
    add: idBook => shoppingCart.save({}, idBook),
    update: (item, quantity) => shoppingCart.update({id: item.id}, quantity),
    remove: item => shoppingCart.remove({id: item.id}, {}),
    buy: () => buyBooks.update({}),
    find: () => findTotalPrice.get({})
}