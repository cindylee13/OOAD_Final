// vue.config.js
module.exports = {
    // options...
    publicPath: process.env.NODE_ENV === 'production'
        ? '/'
        : '/', //development url
    outputDir: '../HotelDb/src/main/resources/static/'
}