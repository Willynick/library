<template>
    <v-list-item>
        <v-list-item-avatar>
            <v-img :src="image"></v-img>
        </v-list-item-avatar>

        <v-list-item-content>
            <v-list-item-title>{{comment.text}}</v-list-item-title>
            <v-list-item-subtitle>{{comment.author.username}}</v-list-item-subtitle>
        </v-list-item-content>
    </v-list-item>
</template>

<script>
    export default {
        name: 'CommentItem',
        props: ['comment'],
        computed: {
            image() {
                return "/file/" + this.comment.author.userpic
            }
        }
    }
</script>

<style scoped>

</style>
