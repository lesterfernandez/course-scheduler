// module.exports = {
//   extends: [
//     "eslint:recommended",
//     "airbnb-base",
//     "airbnb-typescript/base",
//     "plugin:@typescript-eslint/recommended",
//     "prettier",
//   ],
//   plugins: ["@typescript-eslint"],
//   root: true,
//   parser: "@typescript-eslint/parser",
//   parserOptions: {
//     project: "./tsconfig.json",
//   },
//   rules: {
//     "react-hooks/rules-of-hooks": "error",
//     "react-hooks/exhaustive-deps": "warn",
//   },
// };

module.exports = {
  root: true,
  extends: ["eslint:recommended", "airbnb-base", "prettier"],
  plugins: ["react-hooks", "import"],
  rules: {
    semi: 2,
    "prefer-template": "off",
    "no-console": "off",
    "no-undef": "off",
    "no-plusplus": "off",
    camelcase: "off",
    "import/no-extraneous-dependencies": "off",
    "import/prefer-default-export": "off",
    "no-use-before-define": "off",
    "import/first": "error",
    "import/newline-after-import": "error",
    "import/no-duplicates": "error",
    "import/extensions": [
      "error",
      "ignorePackages",
      {
        js: "never",
        jsx: "never",
        ts: "never",
        tsx: "never",
      },
    ],
  },
  settings: {
    "import/resolver": {
      node: {
        extensions: [".js", ".jsx", ".ts", ".tsx"],
      },
    },
  },
  overrides: [
    {
      files: ["*.ts", "*.tsx"],
      extends: [
        "eslint:recommended",
        "plugin:@typescript-eslint/recommended",
        "airbnb-base",
        "airbnb-typescript/base",
        "prettier",
      ],
      plugins: ["@typescript-eslint", "react-hooks", "import"],
      parser: "@typescript-eslint/parser",
      parserOptions: {
        tsconfigRootDir: __dirname,
        ecmaVersion: "latest",
        project: ["./tsconfig.json"],
        warnOnUnsupportedTypeScriptVersion: true,
      },
      rules: {
        semi: 2,
        "prefer-template": "off",
        "no-console": "off",
        "no-undef": "off",
        "no-plusplus": "off",
        camelcase: "off",
        "import/no-extraneous-dependencies": "off",
        "import/prefer-default-export": "off",
        "no-use-before-define": "off",
        "default-case": "off",
        "@typescript-eslint/switch-exhaustiveness-check": "error",
        "@typescript-eslint/no-use-before-define": "off",
        "@typescript-eslint/no-var-requires": 0,
        "import/first": "error",
        "import/newline-after-import": "error",
        "import/no-duplicates": "error",
        "import/extensions": [
          "error",
          "ignorePackages",
          {
            js: "never",
            jsx: "never",
            ts: "never",
            tsx: "never",
          },
        ],
      },
      settings: {
        "import/resolver": {
          node: {
            extensions: [".js", ".jsx", ".ts", ".tsx"],
          },
        },
      },
    },
  ],
};
