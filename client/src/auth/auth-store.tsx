import { z } from "zod";
import { create } from "zustand";

export const useAuthStore = create(() => ({
  loggedIn: false,
  username: "",
  token: localStorage.getItem("token") || "",
}));

export const authStoreValueSchema = z.object({
  loggedIn: z.boolean(),
  username: z.string(),
  token: z.string(),
});

export type AuthStoreValue = z.infer<typeof authStoreValueSchema>;
