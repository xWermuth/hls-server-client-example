const path = require('path');
const TerserPlugin = require('terser-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

const OUT_DIR = path.resolve(__dirname, '../server/src/main/resources/static/js');

module.exports = (env, options) => {
  const isDev = options.mode === 'development';

  let config = {};

  if (!isDev) {
    config = {
      optimization: {
        minimizer: [
          new TerserPlugin({
            extractComments: true,
            parallel: true,
            terserOptions: {
              compress: {
                drop_console: true,
              },
            },
          }),
        ],
      },
    };
  }

  return {
    target: 'web',
    entry: './src/main.ts',
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: ['ts-loader'],
          exclude: /node_modules/,
        },
      ],
    },
    resolve: {
      extensions: ['.ts', '.js'],
    },
    output: {
      filename: 'bundle.js',
      path: OUT_DIR,
      libraryExport: 'default',
      library: 'IX_Hls',
    },
    plugins: [
      new CleanWebpackPlugin({
        dangerouslyAllowCleanPatternsOutsideProject: true,
        cleanStaleWebpackAssets: false,
        cleanOnceBeforeBuildPatterns: [OUT_DIR],
      }),
    ],
    ...config,
  };
};
