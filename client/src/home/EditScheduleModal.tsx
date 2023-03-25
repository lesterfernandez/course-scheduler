import {
  Button,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
} from "@chakra-ui/react";
import EditScheduleForm from "./EditScheduleForm";

interface Props {
  isModalOpen: boolean;
  toggleModal: () => void;
}

const EditScheduleModal = ({ isModalOpen, toggleModal }: Props) => {
  const onClose = () => {
    toggleModal();
  };
  return (
    <Modal isOpen={isModalOpen} onClose={onClose} size="2xl">
      <ModalOverlay />
      <ModalContent _dark={{ bg: "bg" }}>
        <ModalHeader fontSize="2xl">Edit Schedule</ModalHeader>
        <ModalCloseButton size="lg" />
        <ModalBody>
          <EditScheduleForm />
        </ModalBody>
        <ModalFooter gap="1">
          <Button variant="outline" onClick={toggleModal}>
            Cancel
          </Button>
          <Button colorScheme="blue" mr={3} onClick={onClose}>
            Submit
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default EditScheduleModal;
