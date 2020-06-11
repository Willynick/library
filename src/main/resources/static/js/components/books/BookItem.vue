<template>
    <v-container>
    <v-row>
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
                        <v-list-item-group>{{ book.description }}</v-list-item-group>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item @click="">
                    <v-list-item-content>
                        <v-list-item-title>{{ book.author }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item @click="">
                    <v-list-item-content>
                        <v-list-item-title>Year of publishing: {{ book.yearOfPublishing }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item @click="">
                    <v-list-item-content>
                        <v-list-item-title>Price: {{ book.price }}$</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item @click="">
                    <v-list-item-content>
                        <v-list-item-title>Number of copies: {{ book.numberOfCopies }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item @click="">
                    <v-list-item-content>
                        <v-list-item-title>Number of unoccupied: {{ book.numberOfUnoccupied }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item v-if="isShowLinks" @click="">
                    <v-list-item-content>
                        <a target="_blank" alt="Added pdf file" title="Added pdf file"
                           :href="pdfFile">{{ book.title }}</a>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-col>
        <v-col
                cols="6"
                v-if="book.filename"
        >
            <v-list>
                <v-img v-ripple>
                    <img :src="image" size="200px" alt="image">
                </v-img>
            </v-list>
        </v-col>
    </v-row>

    <media v-if="book.link" :book="book"></media>
    </v-container>
</template>

<script>
    import Media from 'components/media/Media.vue'

    export default {
        props: ['book', 'isShowLinks'],
        components: { Media },
        computed: {
            image() {
                return "/file/" + this.book.filename
            },
            pdfFile() {
                return "/file/" + this.book.pdfFilename
            }
        }
    }
</script>

<style>

</style>