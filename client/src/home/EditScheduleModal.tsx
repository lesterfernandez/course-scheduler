import { AddIcon, ArrowBackIcon, DeleteIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  FormControl,
  FormErrorMessage,
  FormLabel,
  Grid,
  HStack,
  Heading,
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
import { Select } from "chakra-react-select";
import { createContext, useContext, useEffect, useState } from "react";
import {
  SubmitHandler,
  UseFieldArrayReturn,
  UseFormReturn,
  useFieldArray,
  useForm,
} from "react-hook-form";
import {
  Schedule,
  defaultCourseValue,
  useScheduleStore,
} from "./schedule-store";

interface Props {
  isModalOpen: boolean;
  toggleModal: () => void;
}

interface EditScheduleContext {
  methods: UseFormReturn<Schedule>;
  fieldArrayMethods: UseFieldArrayReturn<Schedule, "courses">;
  submitForm: SubmitHandler<Schedule>;
  setStep: React.Dispatch<React.SetStateAction<number>>;
  onClose: () => void;
  toggleModal: () => void;
}

const EditScheduleModalContext = createContext({} as EditScheduleContext);

const EditScheduleModal = ({ isModalOpen, toggleModal }: Props) => {
  const [step, setStep] = useState(0);

  const { courses } = useScheduleStore();

  const methods = useForm<Schedule>({ defaultValues: { courses } });
  const {
    formState: { isSubmitSuccessful },
    control,
    reset,
  } = methods;

  const fieldArrayMethods = useFieldArray({
    name: "courses",
    control,
  });

  const onClose = () => {
    setStep(0);
    toggleModal();
    reset({ courses });
  };

  const submitForm: SubmitHandler<Schedule> = ({ courses: newCourses }) => {
    const cleanedCourses = newCourses.map(({ letters, number, ...props }) => ({
      letters: letters.trim().toUpperCase().replaceAll(" ", ""),
      number: number.trim().toUpperCase().replaceAll(" ", ""),
      ...props,
    }));
    useScheduleStore.setState({ courses: cleanedCourses });
  };

  useEffect(() => {
    reset({ courses });
  }, [courses, reset, isSubmitSuccessful]);

  return (
    <Modal
      isOpen={isModalOpen}
      onClose={onClose}
      size="2xl"
      scrollBehavior="inside"
    >
      <ModalOverlay />
      <ModalContent _dark={{ bg: "bg" }} h="90vh">
        <EditScheduleModalContext.Provider
          value={{
            methods,
            fieldArrayMethods,
            onClose,
            setStep,
            submitForm,
            toggleModal,
          }}
        >
          {step === 0 ? <CoursesForm /> : <PrerequisitesForm />}
        </EditScheduleModalContext.Provider>
      </ModalContent>
    </Modal>
  );
};

export default EditScheduleModal;

const CoursesForm = () => {
  const {
    fieldArrayMethods: { fields, remove, append },
    methods: {
      handleSubmit,
      register,
      formState: { errors },
    },
    onClose,
    submitForm,
    setStep,
    toggleModal,
  } = useContext(EditScheduleModalContext);

  return (
    <>
      <ModalHeader fontSize="2xl">Add Courses</ModalHeader>
      <ModalCloseButton size="lg" />

      <ModalBody>
        <Grid gap="2rem">
          {fields.length ? (
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
            append(structuredClone(defaultCourseValue));
          }}
          mr="auto"
        >
          Add Course
        </Button>
        <Button variant="outline" onClick={onClose}>
          Cancel
        </Button>
        {fields.length ? (
          <Button
            colorScheme="blue"
            mr={3}
            onClick={handleSubmit(data => {
              submitForm(data);
              setStep(1);
            })}
            isDisabled={fields.length === 0}
          >
            Next
          </Button>
        ) : (
          <Button
            colorScheme="blue"
            mr={3}
            onClick={handleSubmit(data => {
              submitForm(data);
              toggleModal();
            })}
          >
            Submit
          </Button>
        )}
      </ModalFooter>
    </>
  );
};

const PrerequisitesForm = () => {
  const {
    methods: { handleSubmit },
    fieldArrayMethods: { fields },
    onClose,
    submitForm,
    setStep,
    toggleModal,
  } = useContext(EditScheduleModalContext);

  return (
    <>
      <ModalHeader fontSize="2xl">Edit Prerequisites</ModalHeader>
      <ModalCloseButton size="lg" />

      <ModalBody>
        <Grid gap="2rem">
          {fields.map(field => (
            <Box key={field.id}>
              <Heading size="md" py="2">
                {field.letters + field.number}
              </Heading>
              <Select
                options={fields
                  .filter(f => f.id !== field.id)
                  .map(f => ({
                    value: f.letters + f.number,
                    label: f.letters + f.number,
                  }))}
                isMulti
                isClearable={false}
              />
            </Box>
          ))}
        </Grid>
      </ModalBody>

      <ModalFooter gap="1">
        <Button
          rightIcon={<ArrowBackIcon />}
          onClick={() => void setStep(0)}
          mr="auto"
        >
          Back
        </Button>
        <Button variant="outline" onClick={onClose}>
          Cancel
        </Button>
        <Button
          colorScheme="blue"
          mr={3}
          onClick={handleSubmit(data => {
            submitForm(data);
            toggleModal();
            setStep(0);
          })}
        >
          Submit
        </Button>
      </ModalFooter>
    </>
  );
};
