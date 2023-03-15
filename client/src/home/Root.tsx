import { VStack } from "@chakra-ui/react";
import Courses from "./Courses";
import Header from "./Header";
import ScheduleProvider, { Schedule } from "./ScheduleProvider";

const initialValue: Schedule = {
  workload: 5,
  courses: [],
};

const Root = () => (
  <VStack align="center" maxW="container.md" w="90%" mx="auto">
    <ScheduleProvider initialValue={initialValue}>
      <Header />
      <Courses />
    </ScheduleProvider>
  </VStack>
);

export default Root;
