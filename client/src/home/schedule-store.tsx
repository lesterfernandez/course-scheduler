import { z } from "zod";
import { create } from "zustand";

export const scheduleSchema = z.object({
  courses: z.array(
    z.object({
      letters: z.string(),
      number: z.string(),
      status: z.enum(["AVAILABLE", "IN_PROGRESS"]),
    })
  ),
});

export type Schedule = z.infer<typeof scheduleSchema>;
export type Course = Schedule["courses"][number];

const initialValue = [
  { letters: "cop", number: "2800", status: "AVAILABLE" as const },
];

export const useScheduleStore = create<Schedule>(() => ({
  courses: [...initialValue],
}));