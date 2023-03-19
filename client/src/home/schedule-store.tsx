import { create } from "zustand";

export interface Course {
  letters: string;
  number: string;
}

interface ScheduleStore {
  schedule: Course[];
}

const useScheduleStore = create<ScheduleStore>(() => ({
  schedule: [],
}));

export default useScheduleStore;
