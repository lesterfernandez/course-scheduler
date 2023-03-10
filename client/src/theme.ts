import { extendTheme, ThemeOverride } from "@chakra-ui/react";

const themeOverride: ThemeOverride = {
  config: {
    initialColorMode: "dark",
    useSystemColorMode: false,
  },
  styles: {
    global: props => ({
      "html, body": {
        backgroundColor: props.colorMode === "dark" ? "#242424" : "white",
      },
    }),
  },
};

const theme = extendTheme(themeOverride);
export default theme;
