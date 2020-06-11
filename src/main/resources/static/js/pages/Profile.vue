<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs7="!$vuetify.breakpoint.xsOnly">
                <div class="title mb-3">User profile</div>
                <v-layout row justify-space-between>
                    <v-flex class="px-1">
                        <v-layout column>
                            <image-input :typeOfFile="'image'" v-model="image">
                                <v-avatar size="250" slot="activator">
                                    <div v-if="!image">
                                        <img :src="userPhoto" alt="image">
                                    </div>
                                    <div v-else>
                                        <img :src="image.imageURL" alt="image">
                                    </div>
                                </v-avatar>
                            </image-input>

                            <v-slide-x-transition>
                                <div v-if="image">
                                    <v-btn class="mt-2 primary" @click="uploadImage">Save Image</v-btn>
                                </div>
                            </v-slide-x-transition>
                        </v-layout>
                    </v-flex>
                    <v-flex class="px-1">
                        <v-layout column>
                            <v-flex>{{profile.username}}</v-flex>
                            <v-flex>{{profile.locale}}</v-flex>
                            <v-flex>{{profile.gender}}</v-flex>
                            <v-flex>{{profile.email}}</v-flex>
                            <v-flex>{{profile.lastVisit}}</v-flex>

                        <!--<a target="_blank" alt="StackExchange Handbook" title="StackExchange Handbook"
                           href="img/Lukyanenko_S._Postyepidemiya._Dvesti_Pervyiyi_Shag.a4.pdf">StackExchange Handbook</a>-->

                        <v-menu offset-y :close-on-click="false" :close-on-content-click="false" v-model="menuOpen" :nudge-width="300">
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" class="mt-4 mr-10" @click="menuOpen=true">User Editor</v-btn>
                            </template>
                            <v-card>
                                <v-card-text>
                                    <v-text-field
                                            label="Change username"
                                            v-model="username"
                                            @keyup.enter="save"
                                    />
                                    <v-text-field
                                            label="Change email"
                                            v-model="email"
                                            @keyup.enter="save"
                                    />
                                    <v-text-field
                                            label="Change password"
                                            v-model="password"
                                            @keyup.enter="save"
                                    />
                                </v-card-text>

                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="grey darken-2" @click="menuOpen=false" dark>CANCEL</v-btn>
                                    <v-btn @click="save" >
                                        Save
                                    </v-btn>
                                </v-card-actions>

                            </v-card>
                        </v-menu>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>

    </v-container>
</template>

<script>
    import { mapActions } from 'vuex'
    import { mapState } from 'vuex'
    import ImageInput from '../components/ImageInput.vue'

    export default {
        name: 'Profile',
        computed: {
            ...mapState(['profile']),
            userPhoto() {
                return "/file/" + this.profile.userpic;
            }
        },
        components: {
            ImageInput: ImageInput
        },
        data() {
            return {
                username: "",
                email: "",
                password: "",

                image: null,
                menuOpen: false
            }
        },
        methods: {
            ...mapActions(['updateProfileAction','updateProfileFileAction']),
            uploadImage() {
                this.updateProfileFileAction(this.image.formData)

                setTimeout(() => {
                        this.image = null
                    }
                    , 500)
            },
            save() {
                const user = {
                    username: this.username,
                    email: this.email,
                    password: this.password
                }

                this.updateProfileAction(user);

                this.menuOpen = false;
            }
        },
    }
</script>

<style scoped>
    img {
        max-width: 100%;
        height: auto;
    }
</style>
