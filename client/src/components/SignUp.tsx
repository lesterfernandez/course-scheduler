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
import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

function SignUp() {
  const [showPassword, setShowPassword] = useState(false);
  const toggleShowPassword = () => setShowPassword(!showPassword);
  const passwordRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();

  const focusOnPasswordField = () => {
    setTimeout(() => {
      if (!passwordRef.current) return;
      passwordRef.current.focus();
      const inputLength = passwordRef.current.value.length;
      passwordRef.current.setSelectionRange(inputLength, inputLength);
    }, 0);
  };

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
            ref={passwordRef}
          />
          <InputRightElement width="4rem" h="100%">
            <Button
              onClick={() => {
                toggleShowPassword();
                focusOnPasswordField();
              }}
              borderLeftRadius="0"
              autoFocus={false}
            >
              {showPassword ? "Hide" : "Show"}
            </Button>
          </InputRightElement>
        </InputGroup>
      </FormControl>
      <Box>
        <ButtonGroup mt="5">
          <Button colorScheme="blue" type="submit">
            Sign Up
          </Button>
          <Button onClick={() => void navigate("/login")}>Log In</Button>
        </ButtonGroup>
      </Box>
    </VStack>
  );
}

export default SignUp;
