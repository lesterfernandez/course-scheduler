import { AddIcon, DeleteIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  FormControl,
  FormErrorMessage,
  FormLabel,
  Grid,
  Heading,
  HStack,
  IconButton,
  Input,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
} from "@chakra-ui/react";
import { useEffect, useRef } from "react";
import { SubmitHandler, useFieldArray, useForm } from "react-hook-form";
import { Schedule, useScheduleStore } from "./schedule-store";

interface Props {
  isModalOpen: boolean;
  toggleModal: () => void;
}

const EditScheduleModal = ({ isModalOpen, toggleModal }: Props) => {
  const { courses } = useScheduleStore();

  const {
    register,
    formState: { errors },
    handleSubmit,
    control,
    reset,
  } = useForm<Schedule>({ defaultValues: { courses } });

  const { fields, remove, append } = useFieldArray({
    name: "courses",
    control,
    rules: {
      minLength: {
        value: 1,
        message: "Schedule must have at least one course",
      },
    },
  });

  const modalBodyRef = useRef<HTMLDivElement | null>(null);
  useEffect(() => {
    if (!modalBodyRef.current) return;
    modalBodyRef.current.scroll(0, modalBodyRef.current.scrollHeight);
  }, [fields.length]);

  const onClose = () => {
    toggleModal();
    reset({ courses });
  };

  const onSubmit: SubmitHandler<{ courses: typeof courses }> = ({
    courses: newCourses,
  }) => {
    useScheduleStore.setState({ courses: newCourses });
    toggleModal();
  };

  return (
    <Modal
      isOpen={isModalOpen}
      onClose={onClose}
      size="2xl"
      scrollBehavior="inside"
    >
      <ModalOverlay />
      <ModalContent _dark={{ bg: "bg" }}>
        <ModalHeader fontSize="2xl">Edit Schedule</ModalHeader>
        <ModalCloseButton size="lg" />

        <ModalBody ref={modalBodyRef}>
          <Grid gap="2rem">
            {fields ? (
              fields.map((field, i) => (
                <Box key={field.id}>
                  <HStack justify="space-between" align="end">
                    <Heading size="md" py="2">
                      {field.letters + field.number || "New Course"}
                    </Heading>
                    <IconButton
                      icon={<DeleteIcon />}
                      aria-label="Delete course"
                      onClick={() => void remove(i)}
                      size="sm"
                    />
                  </HStack>
                  <FormControl
                    isInvalid={
                      !!(errors.courses && errors.courses[i]?.letters?.message)
                    }
                  >
                    <FormLabel>Course Subject Code</FormLabel>
                    <Input
                      {...register(`courses.${i}.letters` as const, {
                        required: "Course Subject Code is required",
                      })}
                    />
                    <FormErrorMessage>
                      {errors.courses && errors.courses[i]?.letters?.message}
                    </FormErrorMessage>
                  </FormControl>
                  <FormControl
                    isInvalid={
                      !!(errors.courses && errors.courses[i]?.number?.message)
                    }
                  >
                    <FormLabel>Course Number</FormLabel>
                    <Input
                      {...register(`courses.${i}.number` as const, {
                        required: "Course Number is required",
                      })}
                    />
                    <FormErrorMessage>
                      {errors.courses && errors.courses[i]?.number?.message}
                    </FormErrorMessage>
                  </FormControl>
                </Box>
              ))
            ) : (
              <Text textAlign="center">
                Empty schedule. Click 'Add Course' to begin creating a schedule.
              </Text>
            )}
          </Grid>
        </ModalBody>

        <ModalFooter gap="1">
          <Button
            rightIcon={<AddIcon />}
            onClick={() => {
              append(
                { letters: "", number: "", status: "AVAILABLE" },
                { shouldFocus: false }
              );
            }}
            mr="auto"
          >
            Add Course
          </Button>
          <Button variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button colorScheme="blue" mr={3} onClick={handleSubmit(onSubmit)}>
            Submit
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default EditScheduleModal;
