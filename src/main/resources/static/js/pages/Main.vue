<template>
    <v-container>
        <v-card
                class="mx-auto"
                max-width="1000"
        >
            <v-img
                    src="/file/library.jpeg"
                    height="300px"
            ></v-img>

            <v-card-title>
                Hello {{profile.username}}<br>We are glad to see you in our online library
            </v-card-title>

            <v-card-text class="text--primary">
                <strong>What is an online library?</strong><br>
                The OU Library provides you with access to an amazing range of online resources to explore the subjects you are passionate about.  We will also help you to develop skills that will support you as you study, at work or in everyday life.<br><br>

                <strong>Why use the online library?</strong><br>
                The online library gives you access to quality-assured and trusted resources that are selected and assessed by subject specialists and academics.<br><br>

                <strong>Janet Morris</strong> is a level 1 student studying for a Bachelor of Law degree and she uses the library to find legal articles and journals which are relevant for her course. She didn’t feel ready to go to University after studying law at college and is studying with the OU alongside her current job in risk governance.  She uses the library at least once a week to find relevant and trustworthy legal articles to help her with her assignments:<br>

                <strong>“The credible source allowed me to write an informative answer to my TMA and ensured the information was correct and relevant.”</strong><br><br>


                There is an extensive range of journals and magazines, academic books, newspapers, images, dictionaries and encyclopaedias which you can use to:<br>

                Find information for your assignments or projects<br>
                Explore further reading for your module<br>
                Carry out in-depth research or explore specific subjects<br>
            </v-card-text>

        </v-card>
    </v-container>
</template>

<script>

    import {mapState} from "vuex";

    export default {
        name: 'Main',
        computed: {
            ...mapState(['profile'])
        },
    }
</script>

<style scoped>

</style>
