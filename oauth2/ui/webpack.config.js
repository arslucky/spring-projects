var path = require("path");
var webpack = require('webpack');
const { env } = require('node:process');

module.exports = () => {
    console.log('env.GTW_HOST=' + env.GTW_HOST);
    console.log('env.GTW_PORT=' + env.GTW_PORT);

    var GTW_HOST = env.GTW_HOST;
    if(!GTW_HOST) {GTW_HOST = 'localhost';}
    var GTW_PORT = env.GTW_PORT;
    if(!GTW_PORT) {GTW_PORT = '8081';}

    console.log('GTW_HOST=' + GTW_HOST);
    console.log('GTW_PORT=' + GTW_PORT);

    return {
        plugins: [
            new webpack.DefinePlugin({
              'process.env':{
                'GTW_HOST': "'" + GTW_HOST + "'",
                'GTW_PORT': "'" + GTW_PORT + "'"
              }
            })
        ],
        entry: './src/main/js/app.js',
        devtool: 'source-map',
        mode: 'development',
        //cache: true,
        output: {
            path: __dirname,
            filename: './src/main/resources/static/built/bundle.js'
        },
        module: {
            rules: [
                {
                    test: path.join(__dirname, '.'),
                    exclude: /node_modules/,
                    use: [{
                        loader: 'babel-loader',
                        options: {
                            presets: ["@babel/preset-env", "@babel/preset-react"]
                        }
                    }]
                }
            ]
        }
    }
}