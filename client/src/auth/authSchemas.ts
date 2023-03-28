import { z } from "zod";
import { scheduleSchema } from "../home/schedule-store";
import { authStoreSchema } from "./auth-store";

const errorSchema = z.object({ errorMessage: z.string() });

export const loginSchema = authStoreSchema.and(scheduleSchema).or(errorSchema);

export const signUpSchema = authStoreSchema.or(errorSchema);
