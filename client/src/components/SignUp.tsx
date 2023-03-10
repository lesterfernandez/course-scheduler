import {
  Box,
  Button,
  ButtonGroup,
  FormControl,
  FormLabel,
  Heading,
  Input,
  InputGroup,
  InputRightElement,
  VStack,
} from "@chakra-ui/react";
import { useState } from "react";

function SignUp() {
  const [showPassword, setShowPassword] = useState(false);
  const toggleShowPassword = () => setShowPassword(!showPassword);

  return (
    <VStack
      m="auto"
      w={{ base: "90%", sm: "400px" }}
      h="100vh"
      justify="center"
      gap="1rem"
    >
      <Heading>Sign Up</Heading>
      <FormControl>
        <FormLabel>Username</FormLabel>
        <Input autoComplete="false" />
      </FormControl>
      <FormControl>
        <FormLabel>Password</FormLabel>
        <InputGroup>
          <Input
            type={showPassword ? "text" : "password"}
            autoComplete="false"
          />
          <InputRightElement width="4rem" h="100%">
            <Button
              onClick={toggleShowPassword}
              borderLeftRadius="0"
              autoFocus={false}
            >
              {showPassword ? "Hide" : "Show"}
            </Button>
          </InputRightElement>
        </InputGroup>
      </FormControl>
      <Box>
        <ButtonGroup gap="1rem" mt="5">
          <Button colorScheme="blue" type="submit">
            Sign Up
          </Button>
          <Button variant="link" textDecor="underline">
            Log In
          </Button>
        </ButtonGroup>
      </Box>
    </VStack>
  );
}

export default SignUp;
