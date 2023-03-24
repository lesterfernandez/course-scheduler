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

interface Props {
  modalOpen: boolean;
  toggleModal: () => void;
}

const ScheduleModal = ({ modalOpen, toggleModal }: Props) => {
  const onClose = () => {
    toggleModal();
  };
  return (
    <Modal isOpen={modalOpen} onClose={onClose} closeOnOverlayClick={false}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Edit Schedule</ModalHeader>
        <ModalCloseButton />
        <ModalBody>Hello World</ModalBody>

        <ModalFooter>
          <Button variant="ghost">Cancel</Button>
          <Button colorScheme="blue" mr={3} onClick={onClose}>
            Submit
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default ScheduleModal;
