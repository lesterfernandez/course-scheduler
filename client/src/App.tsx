import { MoonIcon, SunIcon } from "@chakra-ui/icons";
import { IconButton, useColorMode } from "@chakra-ui/react";
import { useState } from "react";

function App() {
  const [count, setCount] = useState(0);
  const { colorMode, toggleColorMode } = useColorMode();

  return (
    <div className="App">
      <button onClick={() => setCount(count + 1)}>{count}</button>
      <IconButton
        pos="absolute"
        top="0"
        right="0"
        m="3"
        aria-label="Button to toggle the color mode"
        onClick={toggleColorMode}
        icon={
          colorMode == "light" ? (
            <MoonIcon color="blue.700" />
          ) : (
            <SunIcon color="orange.200" />
          )
        }
      />
    </div>
  );
}

export default App;
