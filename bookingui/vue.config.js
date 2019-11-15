// vue.config.js
module.exports = {
    // options...
    publicPath: process.env.NODE_ENV === 'production'
        ? '/'
        : 'http://localhost:8080/', //development url
    outputDir: '../HotelDb/src/main/resources/static/'
}