import { z } from "zod";
import { create } from "zustand";

export const scheduleSchema = z.array(
  z.object({
    letters: z.string(),
    number: z.string(),
    status: z.enum(["AVAILABLE", "IN_PROGRESS"]),
  })
);

export type Schedule = z.infer<typeof scheduleSchema>;
export type Course = Schedule[number];

export interface ScheduleStore {
  schedule: Schedule;
}

export const useScheduleStore = create<ScheduleStore>(() => ({
  schedule: [],
}));
