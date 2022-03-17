module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: ['airbnb-typescript', 'plugin:@typescript-eslint/recommended', 'plugin:import/recommended'],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
    project: './tsconfig.json',
  },
  plugins: ['@typescript-eslint'],
  rules: {
    'react/jsx-filename-extension': [0],
  },
  files: ['*.ts'],
  ignorePatterns: ['webpack.*', 'client', '.eslintrc.js'],
};
