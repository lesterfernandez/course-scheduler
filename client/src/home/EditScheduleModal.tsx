import { AddIcon, ArrowBackIcon, DeleteIcon } from "@chakra-ui/icons";
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
import { Select } from "chakra-react-select";
import { createContext, useContext, useEffect, useState } from "react";
import {
  Controller,
  SubmitHandler,
  useFieldArray,
  UseFieldArrayReturn,
  useForm,
  UseFormReturn,
  useWatch,
} from "react-hook-form";
import { useAuthStore } from "../auth/auth-store";
import {
  generateEmptyCourse,
  Schedule,
  scheduleSchema,
  useScheduleStore,
} from "./schedule-store";

interface Props {
  isModalOpen: boolean;
  toggleModal: () => void;
}

interface EditScheduleContext {
  methods: UseFormReturn<Schedule>;
  fieldArrayMethods: UseFieldArrayReturn<Schedule, "courses">;
  saveForm: SubmitHandler<Schedule>;
  setStep: React.Dispatch<React.SetStateAction<number>>;
  submitForm: (schedule: Schedule) => void;
  onClose: () => void;
  toggleModal: () => void;
}

const EditScheduleModalContext = createContext({} as EditScheduleContext);

const EditScheduleModal = ({ isModalOpen, toggleModal }: Props) => {
  const [step, setStep] = useState(0);

  const { courses } = useScheduleStore();
  const { token } = useAuthStore();

  const methods = useForm<Schedule>({ defaultValues: { courses } });
  const {
    formState: { isSubmitSuccessful },
    setError,
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

  const saveForm: SubmitHandler<Schedule> = ({ courses: newCourses }) => {
    const cleanedCourses = newCourses.map(({ letters, number, ...props }) => ({
      letters: letters.trim().toUpperCase().replaceAll(" ", ""),
      number: number.trim().toUpperCase().replaceAll(" ", ""),
      ...props,
    }));
    useScheduleStore.setState({ courses: cleanedCourses });
  };

  const submitForm = async (schedule: Schedule) => {
    try {
      const response = await fetch("http://localhost:8080/api/schedule", {
        method: "POST",
        body: JSON.stringify(schedule),
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      const responseData = await response.json();
      const newSchedule = scheduleSchema.parse(responseData);
      useScheduleStore.setState(newSchedule);
      console.log(newSchedule);
    } catch (error) {
      if (error instanceof Error) {
        setError("root", error);
      }
      console.log(error);
    }
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
            saveForm,
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
    saveForm,
    submitForm,
    setStep,
    toggleModal,
  } = useContext(EditScheduleModalContext);

  return (
    <>
      <ModalHeader fontSize="2xl">Add Courses</ModalHeader>
      <ModalCloseButton size="lg" />
      <ModalBody>
        <>
          <Text color="red.200">{errors?.root?.message}</Text>
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
        </>
      </ModalBody>

      <ModalFooter gap="1">
        <Button
          rightIcon={<AddIcon />}
          onClick={() => {
            append(generateEmptyCourse());
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
              saveForm(data);
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
              saveForm(data);
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
    methods: {
      handleSubmit,
      control,
      formState: { errors },
    },
    onClose,
    saveForm,
    submitForm,
    setStep,
    toggleModal,
  } = useContext(EditScheduleModalContext);

  const courses = useWatch({
    control,
    name: "courses",
  });

  return (
    <>
      <ModalHeader fontSize="2xl">Edit Prerequisites</ModalHeader>
      <ModalCloseButton size="lg" />

      <ModalBody>
        <>
          <Text color="red.200">{errors?.root?.message}</Text>
          <Grid gap="2rem">
            {courses.map((field, i) => (
              <Box key={field.uuid}>
                <Heading size="md" py="2">
                  {field.letters + field.number}
                </Heading>

                <Controller
                  name={`courses.${i}.prerequisites` as const}
                  control={control}
                  render={({ field: { onChange, value, ref, name } }) => {
                    const options = courses
                      .filter(otherCourse => otherCourse.uuid !== field.uuid)
                      .map(({ uuid, letters, number }) => ({
                        value: uuid,
                        label: letters + number,
                      }));

                    const selectValues = value.map(courseId => {
                      const selfValue = courses.find(
                        otherCourse => otherCourse.uuid === courseId
                      );
                      return selfValue
                        ? {
                            value: selfValue.uuid,
                            label: selfValue.letters + selfValue.number,
                          }
                        : selfValue;
                    });

                    return (
                      <Select
                        name={name}
                        ref={ref}
                        isMulti
                        isClearable={false}
                        options={options}
                        value={selectValues}
                        onChange={selections =>
                          void onChange(
                            selections.map(selection => selection?.value)
                          )
                        }
                      />
                    );
                  }}
                />
              </Box>
            ))}
          </Grid>
        </>
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
            saveForm(data);
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
