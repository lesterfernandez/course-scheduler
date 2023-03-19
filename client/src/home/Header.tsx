import { Button, Heading, useColorModeValue, VStack } from "@chakra-ui/react";
import { useAuthStore } from "../auth/auth-store";

const Header = () => {
  const headerBg = useColorModeValue("white", "bg");
  const { username } = useAuthStore();
  return (
    <VStack
      justify="space-evenly"
      gap="1rem"
      pos="sticky"
      top="0"
      pb="4"
      mb="4"
      shadow="lg"
      bgColor={headerBg}
    >
      <Heading textAlign="center" py="4">
        {username}'s School Schedule
      </Heading>
      <Button size="lg">Edit Schedule</Button>
    </VStack>
  );
};

export default Header;
