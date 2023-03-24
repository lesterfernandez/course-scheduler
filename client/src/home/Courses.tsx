import { Container, Text } from "@chakra-ui/react";
import { useScheduleStore } from "./schedule-store";

const Courses = () => {
  const { schedule } = useScheduleStore();
  return (
    <Container>
      {schedule.map((course, i) => (
        <Text
          key={i}
        >{`${course.letters}${course.number}: ${course.status}`}</Text>
      ))}
      <Text h="300vh">
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
        tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
        veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
        commodo consequat. Duis aute irure dolor in reprehenderit in voluptate
        velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
        occaecat cupidatat non proident, sunt in culpa qui officia deserunt
        mollit anim id est laborum.
      </Text>
    </Container>
  );
};

export default Courses;
