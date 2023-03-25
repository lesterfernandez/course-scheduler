import { DeleteIcon } from "@chakra-ui/icons";
import { IconButton, Text } from "@chakra-ui/react";
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
      <Text>Hello world</Text>
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
