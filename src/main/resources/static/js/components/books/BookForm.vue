<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-content>
        <v-menu offset-y :close-on-click="false" :close-on-content-click="false" v-model="menuOpenAttr" :nudge-width="600">
            <template v-slot:activator="{ on }">
                <v-btn v-on="on" class="ma-4" @click="editMenuOpen(true)">Book Editor</v-btn>
            </template>
            <v-card>
                <v-card-text>
                    <v-text-field
                            label="Write title"
                            v-model="title"
                            :rules="[rules.required, rules.counterTitle]"
                            maxlength="100"
                            @keyup.enter="save"
                    />
                    <v-text-field
                            label="Write description"
                            v-model="description"
                            :rules="[rules.required, rules.counterDescription]"
                            maxlength="2048"
                            @keyup.enter="save"
                    />
                    <v-text-field
                            label="Write author"
                            v-model="author"
                            :rules="[rules.required, rules.counterAuthor]"
                            maxlength="50"
                            @keyup.enter="save"
                    />
                    <v-text-field
                            label="Write year of publishing"
                            v-model="yearOfPublishing"
                            @keyup.enter="save"
                    />
                    <v-text-field
                            label="Write price"
                            v-model="price"
                            prefix="$"
                            @keyup.enter="save"
                    />
                    <v-text-field
                            label="Write number of copies"
                            v-model="numberOfCopies"
                            @keyup.enter="save"
                    />
                    <v-text-field
                            label="Write number of unoccupied"
                            v-model="numberOfUnoccupied"
                            @keyup.enter="save"
                    />
                </v-card-text>

                <v-container grid-list-xl>
                    <image-input :typeOfFile="'image'" v-model="image">
                        <div slot="activator">
                            <v-img v-ripple v-if="!image">
                                <v-btn class="mx-4 my-4">Click to add image</v-btn>
                            </v-img>
                            <v-img v-ripple v-else class="mb-3">
                                <img :src="image.imageURL" alt="image">
                            </v-img>
                        </div>
                    </image-input>
                </v-container>

                <v-container grid-list-xl>
                    <image-input :typeOfFile="'pdf'" v-model="pdfFile">
                        <div class="mx-4 my-4" slot="activator">
                            <span v-ripple v-if="!pdfFile">
                                <v-btn>Click to add pdf file</v-btn>
                            </span>
                            <span v-ripple v-else class="mb-3">
                                <v-btn>Edit pdf file</v-btn>
                            </span>
                        </div>
                    </image-input>

                    <span v-if="pdfFile">
                                <a class="mt-4 ml-4" target="_blank" alt="Added pdf file" title="Added pdf file"
                                   :href="pdfFile.imageURL">Added file</a>
                    </span>
                </v-container>


                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="grey darken-2" @click="editMenuOpen(false)" dark>CANCEL</v-btn>
                    <v-btn @click="save" >
                       Save
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-menu>
    </v-content>
</template>

<script>
    import { mapActions } from 'vuex'
    import ImageInput from '../ImageInput.vue'

    export default {
        props: ['bookAttr', 'menuOpen', 'editMenuOpen'],
        data() {
            return {
                title: '',
                description: '',
                author: '',
                yearOfPublishing: null,
                price: null,
                numberOfCopies: null,
                numberOfUnoccupied: null,
                id: null,

                menuOpenAttr: false,

                image: null,
                pdfFile: null,

                rules: {
                    required: value => !!value || 'Required.',
                    counterTitle: value => value.length <= 100 || 'Max 100 characters',
                    counterDescription: value => value.length <= 2048 || 'Max 2048 characters',
                    counterAuthor: value => value.length <= 50 || 'Max 50 characters',
                }
            }
        },
        components: {
            ImageInput: ImageInput
        },
        watch: {
            bookAttr(newVal, oldVal) {
                this.title = newVal.title
                this.description = newVal.description
                this.author = newVal.author
                this.yearOfPublishing = newVal.yearOfPublishing
                this.price = newVal.price
                this.numberOfCopies = newVal.numberOfCopies
                this.numberOfUnoccupied = newVal.numberOfUnoccupied
                this.id = newVal.id

                this.filename = newVal.filename
                this.pdfFilename = newVal.pdfFilename
            },
            menuOpen(newVal, oldVal) {
                this.menuOpenAttr = newVal;

                if (newVal === false) {
                    this.title = ''
                    this.description = ''
                    this.author = ''
                    this.yearOfPublishing = null
                    this.price = null
                    this.numberOfCopies = null
                    this.numberOfUnoccupied = null
                    this.id = null
                    this.filename = null
                    this.image = null
                    this.pdfFilename = null
                }
            }
        },
        methods: {
            ...mapActions(['addBookAction', 'updateBookAction', 'updateBookFileAction', 'updateBookPdfFileAction']),
            save() {
                const book = {
                    title: this.title,
                    description: this.description,
                    author: this.author,
                    yearOfPublishing: this.yearOfPublishing,
                    price: this.price,
                    numberOfCopies: this.numberOfCopies,
                    numberOfUnoccupied: this.numberOfUnoccupied,
                    filename: this.filename,
                    pdfFilename: this.pdfFilename,
                    id: this.id,
                }

                if (this.id) {
                    this.updateBookAction(book)
                } else {
                    this.addBookAction(book)
                }

                if (this.image != null || this.pdfFile != null) {

                    setTimeout(() => {
                        if (this.image != null) {
                            this.updateBookFileAction(this.image.formData)
                        }
                        if (this.pdfFile != null) {
                            this.updateBookPdfFileAction(this.pdfFile.formData)
                        }
                            this.editMenuOpen(false)
                        }
                        , 500)
                } else {
                    this.editMenuOpen(false)
                }

            }
        }
    }
</script>

<style>

</style>