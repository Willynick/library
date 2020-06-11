<template>
    <v-layout row class="px-3">
        <v-text-field
                label="Add comment"
                placeholder="Write something"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn @click="save">
            Add
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex'

    export default {
        name: 'CommentForm',
        props: ['bookId'],
        data() {
            return {
                text: ''
            }
        },
        methods: {
            ...mapActions(['addCommentAction']),
            async save() {
                await this.addCommentAction({
                    text: this.text,
                    book: {
                        id: this.bookId
                    }
                })

                this.text = ''
            }
        }
    }
</script>

<style scoped>

</style>
