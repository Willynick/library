<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <div>
                <v-avatar
                        v-if="book.author && image"
                        size="48px"
                >
                    <img
                            :src="image"
                            :alt="book.authorOfPost.username"
                    >
                </v-avatar>

                <v-avatar
                        v-else
                        size="48px"
                        color="indigo"
                >
                    <v-icon dark>account_circle</v-icon>
                </v-avatar>
                <span class="pl-3">{{ authorName }}</span>
            </div>
        </v-card-text>

        <book-item :isShowLinks="false" :book="book"></book-item>

        <v-card-actions>
            <v-btn @click="addToCart" text rounded color="primary">Add to cart</v-btn>
            <v-btn v-if="isAdmin" value="Edit" @click="edit" text rounded>Edit</v-btn>
            <v-btn v-if="isAdmin" icon @click="del">
                <v-icon>delete</v-icon>
            </v-btn>
        </v-card-actions>
        <comment-list class="px-3"
                :comments="book.comments"
                :book-id="book.id"
        ></comment-list>
    </v-card>
</template>

<script>
    import {mapActions, mapMutations, mapState} from 'vuex'
    import Media from 'components/media/Media.vue'
    import CommentList from '../comment/CommentList.vue'
    import BookItem from './BookItem.vue'

    export default {
        props: ['book', 'editBook'],
        components: { CommentList, Media, BookItem },
        data() {
            return {
                menuOpen: false,
            }
        },
        computed: {
            ...mapState(['isAdmin', 'shoppingCart', 'numberOfItems']),
            authorName() {
                return this.book.authorOfPost ? this.book.authorOfPost.username : 'unknown'
            },
            image() {
                return "/file/" + this.book.authorOfPost.userpic
            }
        },
        methods: {
            ...mapActions(['removeBookAction', 'addToShoppingCartAction', 'updateToShoppingCartAction', 'setNumberOfItemsAction', 'findTotalPriceAction']),
            edit() {
                this.editBook(this.book)
            },
            del() {
                this.removeBookAction(this.book)
            },
            addToCart() {
                const item = this.shoppingCart.items.find(item => item.idBook === this.book.id)

                if (item === undefined) {
                    this.addToShoppingCartAction(this.book.id);
                    this.setNumberOfItemsAction(this.numberOfItems + 1);
                } else {
                    const info = {
                        itemCart: item,
                        quantity: item.numberOfCopies + 1
                    }
                    this.updateToShoppingCartAction(info)
                }

                setTimeout(() => {
                    this.findTotalPriceAction();
                }, 200)
            }
        }
    }
</script>

<style>

</style>