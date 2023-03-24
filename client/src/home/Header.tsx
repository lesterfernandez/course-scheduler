import { Button, Heading, useColorModeValue, VStack } from "@chakra-ui/react";
import { useState } from "react";
import { useAuthStore } from "../auth/auth-store";
import ScheduleModal from "./ScheduleModal";

const Header = () => {
  const headerBg = useColorModeValue("white", "bg");
  const { username } = useAuthStore();
  const [modalOpen, setModalOpen] = useState(false);
  return (
    <>
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
        <Button size="lg" onClick={() => void setModalOpen(true)}>
          Edit Schedule
        </Button>
      </VStack>

      {modalOpen && (
        <ScheduleModal
          modalOpen={modalOpen}
          toggleModal={() => void setModalOpen(prev => !prev)}
        />
      )}
    </>
  );
};

export default Header;
