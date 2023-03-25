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
  modalOpen: boolean;
  toggleModal: () => void;
}

const EditScheduleModal = ({ modalOpen, toggleModal }: Props) => {
  const onClose = () => {
    toggleModal();
  };
  return (
    <Modal isOpen={modalOpen} onClose={onClose} size="2xl">
      <ModalOverlay />
      <ModalContent _dark={{ bg: "bg" }}>
        <ModalHeader>Edit Schedule</ModalHeader>
        <ModalCloseButton />
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
