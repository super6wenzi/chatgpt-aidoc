module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    'plugin:vue/essential',
    'eslint:recommended'
  ],
  parserOptions: {
    sourceType: 'module',
    ecmaVersion: 12,
    parser: '@typescript-eslint/parser',
  },
  plugins: ['vue', '@typescript-eslint'],
}