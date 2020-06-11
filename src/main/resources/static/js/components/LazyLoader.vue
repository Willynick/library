<template>
    <span></span>
</template>

<script>
    import { mapActions } from 'vuex'

    export default {
        name: 'LazyLoader',
        props: ['filterAuthor', 'filterTitle'],
        methods: mapActions(['loadPageAction']),
        mounted() {
            window.onscroll = () => {
                const el = document.documentElement
                const isBottomOfScreen = el.scrollTop + window.innerHeight > el.offsetHeight - 10

                const filters = {
                    filterAuthor: this.filterAuthor,
                    filterTitle: this.filterTitle
                }

                if (isBottomOfScreen) {
                    this.loadPageAction(filters)
                }
            }
        },
        beforeDestroy() {
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>
