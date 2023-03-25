import { AddIcon, DeleteIcon } from "@chakra-ui/icons";
import { Button, Heading, HStack, IconButton } from "@chakra-ui/react";
import { useFieldArray, useForm } from "react-hook-form";
import { useScheduleStore } from "./schedule-store";

const EditScheduleForm = () => {
  const { courses } = useScheduleStore();

  const {
    // register,
    // formState: { errors },
    control,
  } = useForm({ defaultValues: { courses } });

  const { fields, remove } = useFieldArray({
    name: "courses",
    control,
  });

  return (
    <>
      <HStack mx="auto" justify="space-around" align="center">
        <Heading size="md">Courses</Heading>
        <Button rightIcon={<AddIcon />}>Add Course</Button>
      </HStack>
      {fields.map((field, i) => (
        <>
          <IconButton
            icon={<DeleteIcon />}
            aria-label="Delete course"
            onClick={() => void remove(i)}
          />
          <pre key={field.id}>{JSON.stringify(field, null, 2)}</pre>
        </>
      ))}
    </>
  );
};

export default EditScheduleForm;
