<template>
    <v-card>
        <v-flex v-if="type === 'href'" xs12 sm6 offset-sm3>
            <v-img v-if="book.linkCover" :src="book.linkCover" aspect-ratio="2.75"></v-img>
            <v-card-title>
                <div>
                    <h3>
                        <a :href="book.link">{{book.linkTitle || book.link}}</a>
                    </h3>
                    <div v-if="book.linkDescription">{{book.linkDescription}}</div>
                </div>
            </v-card-title>
        </v-flex>
        <v-flex v-if="type === 'image'" xs12 sm6 offset-sm3>
            <a :href="book.link">
                <v-img v-if="book.linkCover" :src="book.linkCover" aspect-ratio="2.75"></v-img>
                {{book.link}}
            </a>
        </v-flex>
        <v-flex v-if="type === 'youtube'" xs12 sm6 offset-sm3>
            <you-tube :src="book.link"></you-tube>
        </v-flex>
    </v-card>
</template>

<script>
    import YouTube from './YouTube.vue'

    export default {
        name: 'Media',
        components: { YouTube },
        props: ['book'],
        data() {
            return {
                type: 'href'
            }
        },
        beforeMount() {
            if (this.book.link.indexOf('youtu') > -1) {
                this.type = 'youtube'
            } else if (this.book.link.match(/\.(jpeg|jpg|gif|png)$/) !== null) {
                this.type = 'image'
            } else {
                this.type = 'href'
            }
        }
    }
</script>

<style scoped>

</style>
