import { Button, Heading, Text, VStack } from "@chakra-ui/react";
import { useContext } from "react";
import { AuthContext } from "../auth/AuthProvider";

function Root() {
  const {
    authCtx: { username },
  } = useContext(AuthContext);
  return (
    <VStack align="center" maxW="container.md" w="90%" mx="auto">
      <VStack
        w="full"
        justify="space-evenly"
        gap="1rem"
        pos="sticky"
        top="0"
        backdropFilter="blur(5px)"
        blur="50"
        py="4"
      >
        <Heading>{username}'s School Schedule</Heading>
        <Button size="lg">Edit Schedule</Button>
      </VStack>
      <Text h="100vh">Welcome</Text>
    </VStack>
  );
}

export default Root;
