import {
  createContext,
  ReactNode,
  useState,
  type Dispatch,
  type SetStateAction,
} from "react";
import { z } from "zod";

export const AuthContextValueSchema = z.object({
  loggedIn: z.boolean(),
  username: z.string(),
});

export type AuthContextValue = z.infer<typeof AuthContextValueSchema>;

const initialValue: AuthContextValue = {
  loggedIn: false,
  username: "",
};

interface Context {
  authCtx: AuthContextValue;
  setAuthCtx: Dispatch<SetStateAction<AuthContextValue>>;
}

export const AuthContext = createContext({} as Context);

export default function AuthProvider({ children }: { children: ReactNode }) {
  const [authCtx, setAuthCtx] = useState(initialValue);

  return (
    <AuthContext.Provider value={{ authCtx, setAuthCtx }}>
      {children}
    </AuthContext.Provider>
  );
}
