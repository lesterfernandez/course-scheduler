import { createContext, ReactNode, useState } from "react";

export interface Schedule {
  workload: number;
  courses: Course[];
}

export interface Course {
  letters: string;
  number: string;
}

interface Context {
  schedule: Schedule;
  setSchedule: React.Dispatch<React.SetStateAction<Schedule>>;
}

export const ScheduleContext = createContext({} as Context);

interface Props {
  children: ReactNode;
  initialValue: Schedule;
}

const ScheduleProvider = ({ children, initialValue }: Props) => {
  const [schedule, setSchedule] = useState<Schedule>(initialValue);

  return (
    <ScheduleContext.Provider value={{ schedule, setSchedule }}>
      {children}
    </ScheduleContext.Provider>
  );
};

export default ScheduleProvider;
