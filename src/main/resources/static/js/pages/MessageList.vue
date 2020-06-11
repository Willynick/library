<template>
    <v-container>
        <v-spacer>Filters:</v-spacer>
        <v-row>
            <v-col>
                <v-text-field class="mt-4"
                        label="Write author of book"
                        v-model="filterAuthor"
                        outlined
                        clearable
                        @keyup.enter="find"
                        height="10"
                />
            </v-col>
            <v-col>
                <v-text-field class="mt-4"
                        label="Write title of book"
                        v-model="filterTitle"
                        outlined
                        clearable
                        @keyup.enter="find"
                />
            </v-col>

            <v-card-actions>
                <v-btn @click="find">
                    Find books
                </v-btn>
            </v-card-actions>
        </v-row>
        <v-layout align-space-around justify-start column>
            <book-form v-if="isAdmin" :bookAttr="book" :menuOpen="menuOpen" :editMenuOpen="editMenuOpen"/>
            <book-row v-for="book in sortedBooks"
                         :key="book.id"
                         :book="book"
                         :editBook="editBook"/>
            <lazy-loader
                    :filterAuthor="filterAuthor"
                    :filterTitle="filterTitle"
            ></lazy-loader>
        </v-layout>
    </v-container>
</template>

<script>
    import {mapGetters, mapMutations, mapActions, mapState} from 'vuex'
    import BookRow from 'components/books/BookRow.vue'
    import BookForm from 'components/books/BookForm.vue'
    import LazyLoader from '../components/LazyLoader.vue'

    export default {
        components: {
            LazyLoader,
            BookRow,
            BookForm
        },
        data() {
            return {
                book: null,
                menuOpen: false,
                filterAuthor: null,
                filterTitle: null
            }
        },
        computed: {
            ...mapGetters(['sortedBooks']),
            ...mapState(['isAdmin'])
        },
        methods: {
            ...mapMutations(['setFilteredBooksMutation', 'updateCurrentPageMutation']),
            ...mapActions(['loadPageAction']),
            editBook(book) {
                this.book = book;
                this.menuOpen = true;
            },
            editMenuOpen(menuOpen) {
                this.menuOpen = menuOpen
            },
            find() {
                const filters = {
                    filterAuthor: this.filterAuthor,
                    filterTitle: this.filterTitle
                }

                this.setFilteredBooksMutation(filters)
                this.updateCurrentPageMutation(-1)
                this.loadPageAction(filters)
            }
        }
    }
</script>

<style>

</style>
