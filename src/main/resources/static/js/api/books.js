import Vue from 'vue'

const books = Vue.resource('/book{/id}')
const files = Vue.resource('/book{/id}/file')
const pdfFiles = Vue.resource('/book{/id}/pdfFile')

export default {
    add: book => books.save({}, book),
    update: book => books.update({id: book.id}, book),
    updateFile: (book, file) => files.update({id: book.id}, file),
    updatePdfFile: (book, file) => pdfFiles.update({id: book.id}, file),
    remove: id => books.remove({id}),
    page: page => Vue.http.get('/book', {params: { page }}),
    pageWithFilters: (filterAuthor, filterTitle, page) => books.get({page, filterAuthor, filterTitle})//Vue.http.get('/book', {params: { page }, filterAuthor, filterTitle})
}