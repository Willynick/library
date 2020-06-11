<template>
    <v-container>

        <v-alert
                v-if="profile.hasSubscription === true"
                dismissible
                text
                outlined
                color="deep-orange"
                icon="mdi-fire"
        >
            Congratulations! You have a subscription to all books :)
        </v-alert>
        <v-alert v-if="userBooks.length === 0"
                 dense
                 type="info"
        >
            Sorry, no books are available for you<br>Go to the <strong>shopping cart</strong> to buy the book :)
        </v-alert>

        <v-layout align-space-around justify-start column>
            <v-card v-for="book in userBooks" :key="book.id" class="my-2">
                <book-item
                        :book="book"
                        :isShowLinks="true"
                ></book-item>
            </v-card>
        </v-layout>
    </v-container>
</template>

<script>

    import {mapGetters, mapState} from "vuex";
    import BookItem from "../components/books/BookItem.vue"

    export default {
        name: 'UserBooks',
        computed: {
            ...mapGetters(['userBooks']),
            ...mapState(['profile'])
        },
        components: { BookItem },
        data () {
            return {

            }
        }
    }
</script>

<style scoped>

</style>
