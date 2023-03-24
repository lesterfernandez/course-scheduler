import { z } from "zod";
import { scheduleSchema } from "../home/schedule-store";
import { authStoreSchema } from "./auth-store";

export const loginSchema = authStoreSchema
  .and(z.object({ schedule: scheduleSchema }))
  .or(z.object({ errorMessage: z.string() }));
