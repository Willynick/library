<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-data-table
                v-model="selected"
                :headers="headers"
                :items="sortedUsers"
                :items-per-page="5"
                :single-expand="singleExpand"
                :expanded.sync="expanded"
                item-key="username"
                :key="myKey"
                disable-page-reset
                show-select
                show-expand
                class="mt-4 elevation-1"
                :footer-props="{
      prevIcon: 'mdi-chevron-left',
      nextIcon: 'mdi-chevron-right'
    }"
        >
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>List of users</v-toolbar-title>
                </v-toolbar>
            </template>
            <template v-slot:expanded-item="{ headers, item }">
                <td :colspan="headers.length">
                    <span v-for="id in item.idBooks">
                        <span v-for="book in sortedAllBooks">
                            <span v-if="book.id === id">ID: {{id}}, Title: {{book.title}} <br> </span>
                        </span>
                    </span>
                </td>
            </template>
        </v-data-table>

        <v-card-actions class="mt-4">
            <v-btn @click="makeAdmin">
                Make/Remove admin
            </v-btn>
            <v-btn class="ml-4" @click="giveSubscription">
                Give/Pick up subscription
            </v-btn>
            <v-spacer class="ml-4">or</v-spacer>
            <v-btn class="ml-4" @click="giveAccessToBooks">
                Give/Pick up access to several books
            </v-btn>
                <v-container fluid>
                    <v-select
                            @input="onInput"
                            v-model="selectedBooks"
                            v-bind:items="sortedAllBooks"
                            item-text="`ID: ${data.item.id}: Title: ${data.item.title}`  "
                            item-value="`ID: ${data.item.id}: Title: ${data.item.title}`  "
                            autocomplete
                            return-object
                            label="Choose books"
                            multiple
                    >
                        <template slot="selection" slot-scope="data">
                            ID: {{ data.item.id}}
                            </v-list-item>
                        </template>
                        <template slot="item" slot-scope="data">
                            <v-list>
                            <v-list-item-content>
                                <v-list-item-title v-html="`ID: ${data.item.id}`">
                                </v-list-item-title>
                                <v-list-item-subtitle v-html="`Title: ${data.item.title}`"></v-list-item-subtitle>
                            </v-list-item-content>
                            </v-list>
                        </template>


                    </v-select>
                </v-container>
        </v-card-actions>

    </v-container>
</template>

<script>

    import {mapActions, mapGetters} from "vuex";

    export default {
        name: 'UserList',
        computed: mapGetters(['sortedAllBooks', 'sortedUsers']),
        data () {
            return {
                headers: [
                    {
                        text: 'Users',
                        align: 'start',
                        value: 'username',
                    },
                    { text: 'Roles', value: 'roles' },
                    { text: 'Has a subscription', value: 'hasSubscription'},
                    { text: 'Available books', value: 'data-table-expand' },
                ],
                selected: [],
                selectedBooks: [],
                idSelectedBooks: [],
                myKey: 1,
                expanded: [],
                singleExpand: false
            }
        },
        methods: {
            ...mapActions(['updateUserAction', 'setUserSubscriptionAction', 'setAccessToBookAction']),
            makeAdmin() {
                this.selected.forEach(usr => {
                    this.updateUserAction(usr);
                });

                this.setTimer()
            },
            giveSubscription() {
                this.selected.forEach(usr => {
                    this.setUserSubscriptionAction(usr)
                });

                this.setTimer()
            },
            giveAccessToBooks() {
                this.selected.forEach(usr => {

                    const info = {
                        usr: usr,
                        idBooks: this.idSelectedBooks
                    }

                    this.setAccessToBookAction(info)
                });

                this.setTimer()
            },
            onInput(selectedBooks) {
                let selBooks = []

                selectedBooks.forEach(function(item) {
                    selBooks.push(item.id)
                })

                this.idSelectedBooks = selBooks;
            },
            setTimer() {
                setTimeout(() => {
                        this.selectedBooks = []
                        this.idSelectedBooks = []
                        this.myKey++;
                    }
                    , 600)
            }
        }
    }
</script>

<style scoped>

</style>
