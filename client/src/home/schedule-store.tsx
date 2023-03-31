import { nanoid } from "nanoid";
import { z } from "zod";
import { create } from "zustand";

export const scheduleSchema = z.object({
  courses: z.array(
    z.object({
      uuid: z.string(),
      letters: z.string(),
      number: z.string(),
      status: z.enum(["AVAILABLE", "IN_PROGRESS"]),
      courseIndex: z.number(),
      prerequisites: z.string().array(),
    })
  ),
});

export type Schedule = z.infer<typeof scheduleSchema>;
export type Course = Schedule["courses"][number];

export const generateEmptyCourse = (): Course => ({
  letters: "",
  number: "",
  status: "AVAILABLE",
  prerequisites: [],
  uuid: nanoid(12),
  courseIndex: -1,
});

export const useScheduleStore = create<Schedule>(() => ({
  courses: [],
}));
