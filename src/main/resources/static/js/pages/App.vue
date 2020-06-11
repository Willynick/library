<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Online Library</v-toolbar-title>
            <v-btn class="ml-2" text
                   v-if="profile"
                   :disabled="$route.path === '/'"
                   @click="showMain">
                Main
            </v-btn>
            <v-btn class="ml-2" text
                   v-if="profile"
                   :disabled="$route.path === '/books'"
                   @click="showBooks">
                Books
            </v-btn>
            <v-btn text
                   v-if="profile && isAdmin == true"
                   :disabled="$route.path === '/users/list'"
                   @click="showUsers">
                   Users List

            </v-btn>
            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/user/books'"
                   @click="showMyBooks">
                My available books

            </v-btn>
            <v-spacer></v-spacer>
            <v-btn icon
                   v-if="profile"
                   :disabled="$route.path === '/shopping_cart'"
                   @click="showShoppingCart">
                <v-badge
                        :content="numberOfItems"
                        :value="numberOfItems"
                        color="red"
                        overlap
                >

                    <v-icon large>shopping_cart</v-icon>
                </v-badge>
            </v-btn>
            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/profile'"
                   @click="showProfile">
                {{profile.username}}
            </v-btn>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {mapState, mapMutations, mapActions} from 'vuex'
    import { addHandler } from "util/ws";

    export default {
        computed: mapState(['profile', 'isAdmin', 'numberOfItems']),
        methods: {
            ...mapMutations([
                'addBookMutation',
                'updateBookMutation',
                'removeBookMutation',
                'addCommentMutation',
                'updateProfileMutation',
                'updateUserMutation'
            ]),
            ...mapActions(['setNumberOfItemsAction']),
            showBooks() {
                this.$router.push('/books')
            },
            showProfile() {
                this.$router.push('/profile')
            },
            showUsers() {
                this.$router.push('/users/list')
            },
            showMyBooks() {
                this.$router.push('/user/books')
            },
            showShoppingCart() {
                this.setNumberOfItemsAction(0);

                this.$router.push('/shopping_cart')
            },
            showMain()
            {
                this.$router.push('/')
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'BOOK') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addBookMutation(data.body)
                            break
                        case 'UPDATE':
                            this.updateBookMutation(data.body)
                            break
                        case 'REMOVE':
                            this.removeBookMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }
                } else if (data.objectType === 'COMMENT') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addCommentMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }
                } else if (data.objectType === 'PROFILE') {
                    switch (data.eventType) {
                        case 'UPDATE':
                            this.updateProfileMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }
                } else if (data.objectType === 'USER') {
                    switch (data.eventType) {
                        case 'UPDATE':
                            this.updateUserMutation(data.body)
                            break
                        default:
                            console.error(`Looks like the event type if unknown "${data.eventType}"`)
                    }
                }
            })
        },
        beforeMount() {
            if (!this.profile) {
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style>

</style>