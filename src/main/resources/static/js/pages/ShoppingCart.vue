<template>
    <v-container>
        <v-layout>
            <v-row>
                <v-col>
            <v-card v-for="book in userBooksInShoppingCart" :key="book.id"
                    max-width="800"
            >
                <v-row class="mt-4">
                    <v-col
                            cols="6"
                    >
                        <v-list>
                            <v-list-item @click="">
                                <v-list-item-content>
                                    <v-list-item-title>{{ book.title }}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                            <v-list-item @click="">
                                <v-list-item-content>
                                    <v-list-item-title>{{ book.price }}$</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                            <v-list-item @click="">
                                <v-list-item-content>
                                    <v-list-item-title>{{item(book).numberOfCopies}}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list>
                    </v-col>
                    <v-col>
                        <v-avatar
                                class="profile"
                                size="200"
                                tile
                        >
                            <v-img :src="image(book)"></v-img>
                        </v-avatar>
                    </v-col>
                </v-row>

                <v-card-actions>
                    <v-btn icon @click="del(book)">
                        <v-icon>mdi-delete</v-icon>
                    </v-btn>

                    <v-col cols="12" md="3">
                        <v-text-field
                                v-model="item(book).numberOfCopies"
                                label="Quantity"
                                max="100"
                                min="1"
                                step="1"
                                style="width: 50px"
                                type="number"
                                v-on:blur="setQuantity(book)"
                        ></v-text-field>
                    </v-col>

                </v-card-actions>

            </v-card>
                </v-col>
                <v-col>
                    <div v-if="userBooksInShoppingCart.length > 0" class="font-weight-black headline mt-4">Total price: {{ totalPrice }}$
                       <br> <v-btn class="mt-8" @click="buyBooks" color="success" dark large>Buy books</v-btn>
                    </div>
                </v-col>
            </v-row>
        </v-layout>
        <v-alert v-if="isBuying"
                color="deep-orange"
                dark
                icon="mdi-tag-faces"
                border="left"
                prominent
        >
            Thanks for buying our books
        </v-alert>
        <v-alert v-if="!isBuying && userBooksInShoppingCart.length === 0"
                 dense
                 type="info"
        >
            Sorry, but the cart is empty<br>Select books to <strong>buy</strong> :)
        </v-alert>

    </v-container>

</template>

<script>

    import {mapActions, mapGetters, mapState} from "vuex";
    import BookItem from "../components/books/BookItem.vue"

    export default {
        name: 'ShoppingCart',
        computed: {
            ...mapGetters(['userBooksInShoppingCart']),
            ...mapState(['shoppingCart', 'totalPrice'])
        },
        components: { BookItem },
        data () {
            return {
                isBuying: false
            }
        },
        beforeMount() {
            this.updateTotalPrice();
        },
        methods: {
            ...mapActions(['deleteFromShoppingCartAction', 'buyBooksAction', 'updateToShoppingCartAction', 'findTotalPriceAction']),
            del(book) {
                const item = this.item(book);

                this.deleteFromShoppingCartAction(item)
                this.updateTotalPrice();
            },
            image(book) {
                return "/file/" + book.filename
            },
            buyBooks() {
                this.buyBooksAction();
                this.updateTotalPrice();

                setTimeout(() => {
                    this.isBuying = true;
                }, 400)
                this.$confetti.start()
                setTimeout(() => {
                        this.$confetti.stop();
                        this.isBuying = false;
                    }
                    , 6000)

            },
            setQuantity(book) {
                const item = this.item(book);

                const info = {
                    itemCart: item,
                    quantity: this.item(book).numberOfCopies
                }

                this.updateToShoppingCartAction(info)
                setTimeout(() => {
                    this.updateTotalPrice();
                }, 400);
            },
            item(book) {
                return this.shoppingCart.items.find(item => item.idBook === book.id);
            },
            updateTotalPrice() {
                this.findTotalPriceAction();
            }
        }
    }
</script>

<style scoped>

</style>
