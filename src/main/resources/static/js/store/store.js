import Vue from 'vue'
import Vuex from 'vuex'
import booksApi from 'api/books'
import commentApi from 'api/comment'
import profileApi from 'api/profile'
import userApi from 'api/user'
import shoppingCartApi from 'api/shoppingCart'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        books: books,
        ...frontendData,
        lastEditedBook: 0,
        users: users,
        allBooks: allBooks,
        numberOfItems: 0,
        totalPrice: 0
    },
    getters: {
        sortedBooks: state => (state.books || []).sort((a, b) => -(a.id - b.id)),
        sortedUsers: state => (state.users || []).sort((a, b) => -(a.id - b.id)),
        userBooks: (state, getters) => (state.users[getters.idOfProfile].hasSubscription === true) ? getters.sortedAllBooks : (getters.sortedAllBooks || []).filter(book => state.users[getters.idOfProfile].idBooks.find(oid => oid === book.id)),
        sortedAllBooks: state => (state.allBooks || []).sort((a, b) => -(a.id - b.id)),
        userBooksInShoppingCart: (state, getters) => (getters.sortedAllBooks || []).filter(book => state.shoppingCart.items.find(item => item.idBook === book.id)),
        idOfProfile: (state) => {
            return state.users.findIndex(item => item.id === state.profile.id)
        }
    },
    mutations: {
        addBookMutation(state, book) {
            state.books = [
                ...state.books,
                book
            ]

            state.allBooks = [
                ...state.allBooks,
                book
            ]

            state.lastEditedBook = 0;
        },
        updateBookMutation(state, book) {
            const updateIndex = state.books.findIndex(item => item.id === book.id)

            state.lastEditedBook = updateIndex;

            state.books = [
                ...state.books.slice(0, updateIndex),
                book,
                ...state.books.slice(updateIndex + 1)
            ]

            state.allBooks = [
                ...state.allBooks.slice(0, updateIndex),
                book,
                ...state.allBooks.slice(updateIndex + 1)
            ]
        },
        removeBookMutation(state, book) {
            const deletionIndex = state.books.findIndex(item => item.id === book.id)

            if (deletionIndex > -1) {
                state.books = [
                    ...state.books.slice(0, deletionIndex),
                    ...state.books.slice(deletionIndex + 1)
                ]

                state.allBooks = [
                    ...state.allBooks.slice(0, deletionIndex),
                    ...state.allBooks.slice(deletionIndex + 1)
                ]
            }

            state.lastEditedBook = 0;
        },
        addCommentMutation(state, comment) {
            const updateIndex = state.books.findIndex(item => item.id === comment.book.id)
            const book = state.books[updateIndex]

            if (!book.comments.find(it => it.id === comment.id)) {
                state.books = [
                    ...state.books.slice(0, updateIndex),
                    {
                        ...book,
                        comments: [
                            ...book.comments,
                            comment
                        ]
                    },
                    ...state.books.slice(updateIndex + 1)
                ]
            }
        },
        addBookPageMutation(state, books) {
            const targetBooks = state.books
                .concat(books)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {})

            state.books = Object.values(targetBooks)
        },
        updateTotalPagesMutation(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        },
        setFilteredBooksMutation(state, filters) {
            state.books = []
        },
        updateProfileMutation(state, newProfile) {
            state.profile = newProfile;
        },
        updateUserMutation(state, newUser) {
            const updateIndex = state.users.findIndex(item => item.id === newUser.id)

            state.users = [
                ...state.users.slice(0, updateIndex),
                newUser,
                ...state.users.slice(updateIndex + 1)
            ]
        },
        addItemCartMutation(state, newItemCart) {
            state.shoppingCart.items = [
                ... state.shoppingCart.items,
                newItemCart
            ]
        },
        updateItemCartMutation(state, newItemCart) {
            const updateIndex = state.shoppingCart.items.findIndex(item => item.id === newItemCart.id);

            state.shoppingCart.items = [
                ...state.shoppingCart.items.slice(0, updateIndex),
                newItemCart,
                ...state.shoppingCart.items.slice(updateIndex + 1)
            ]
        },
        removeItemCartMutation(state, itemCart) {
            const deletionIndex = state.shoppingCart.items.findIndex(item => item.id === itemCart.id)

            if (deletionIndex > -1) {
                state.shoppingCart.items = [
                    ...state.shoppingCart.items.slice(0, deletionIndex),
                    ...state.shoppingCart.items.slice(deletionIndex + 1)
                ]
            }
        },
        setNumberOfItemsMutation(state, value) {
            state.numberOfItems = value;
        },
        setShoppingCartEmpty(state) {
            state.shoppingCart.items = [];
        },
        setTotalPriceMutation(state, value) {
            state.totalPrice = value;
        }
    },
    actions: {
        async addBookAction({commit, state}, book) {
            const result = await booksApi.add(book)
            const data = await result.json()
            const index = state.books.findIndex(item => item.id === data.id)

            if (index > -1) {
                commit('updateBookMutation', data)
            } else {
                commit('addBookMutation', data)
            }
        },
        async updateBookAction({state, commit}, book) {
            const result = await booksApi.update(book)
            const data = await result.json()

            commit('updateBookMutation', data)
        },
        async updateBookFileAction({commit, state}, image) {
            const result = await booksApi.updateFile(state.books[state.lastEditedBook], image)
            const data = await result.json()
            commit('updateBookMutation', data)
        },
        async updateBookPdfFileAction({commit, state}, pdfFile) {
            const result = await booksApi.updatePdfFile(state.books[state.lastEditedBook], pdfFile)
            const data = await result.json()
            commit('updateBookMutation', data)
        },
        async removeBookAction({commit}, book) {
            const result = await booksApi.remove(book.id)

            if (result.ok) {
                commit('removeBookMutation', book)
            }
        },
        async addCommentAction({commit, state}, comment) {
            const response = await commentApi.add(comment)
            const data = await response.json()
            commit('addCommentMutation', data)
        },
        async loadPageAction({commit, state}, filters) {

            const response = await booksApi.pageWithFilters(filters.filterAuthor, filters.filterTitle,state.currentPage + 1)
            const data = await response.json()

            commit('addBookPageMutation', data.books)
            commit('updateTotalPagesMutation', data.totalPages)
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        },
        async updateProfileAction({commit, state}, newProfile) {
            const result = await profileApi.update(newProfile)
            const data = await result.json()
            commit('updateProfileMutation', data)
        },
        async updateProfileFileAction({commit, state}, image) {
            const result = await profileApi.updateFile(image)
            const data = await result.json()
            commit('updateProfileMutation', data)
        },
        async updateUserAction({commit, state}, user) {
            const result = await userApi.update(user)
            const data = await result.json()

            commit('updateUserMutation', data)
        },
        async setUserSubscriptionAction({commit, state}, user) {
            const result = await userApi.setSubscription(user)
            const data = await result.json()

            commit('updateUserMutation', data)
        },
        async setAccessToBookAction({commit, state}, info) {
            const user = info.usr;
            const idBooks = info.idBooks;

            const result = await userApi.setAccessToBook(user, idBooks)
            const data = await result.json()

            commit('updateUserMutation', data)
        },
        async addToShoppingCartAction({commit, state}, idBook) {
            const result = await shoppingCartApi.add(idBook)
            const data = await result.json()

            commit('addItemCartMutation', data)
        },
        async updateToShoppingCartAction({commit, state}, info) {
            const itemCart = info.itemCart;
            const quantity = info.quantity;

            const result = await shoppingCartApi.update(itemCart, quantity)
            const data = await result.json()

            commit('updateItemCartMutation', data)
        },
        async deleteFromShoppingCartAction({commit, actions}, itemCart) {
            const result = await shoppingCartApi.remove(itemCart)

            if (result.ok) {
                commit('removeItemCartMutation', itemCart)
            }
        },
        async setNumberOfItemsAction({commit}, value) {
            commit('setNumberOfItemsMutation', value)
        },
        async buyBooksAction({commit, state}) {

            const result = await shoppingCartApi.buy();
            const data = await result.json()

            commit('updateProfileMutation', data)
            commit('setShoppingCartEmpty');
        },
        async findTotalPriceAction({commit, state}) {
            const result = await shoppingCartApi.find();
            const data = await result.json()

            commit('setTotalPriceMutation', data);
        }
    }
})
