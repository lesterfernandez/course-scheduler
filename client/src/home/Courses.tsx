import { Container, Text } from "@chakra-ui/react";
import { useScheduleStore } from "./schedule-store";

const Courses = () => {
  const { courses } = useScheduleStore();
  return (
    <Container>
      {courses.map((course, i) => (
        <Text
          key={i}
        >{`${course.letters}${course.number}: ${course.status}`}</Text>
      ))}
    </Container>
  );
};

export default Courses;
