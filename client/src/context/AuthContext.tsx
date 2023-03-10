import { createContext, ReactNode, useState } from "react";

export type AuthContext = {
  authCtx: {
    loggedIn: boolean;
  };
  setAuthCtx: React.Dispatch<React.SetStateAction<AuthContext["authCtx"]>>;
};

const initialValue: AuthContext["authCtx"] = {
  loggedIn: false,
};

export const AuthContext = createContext({} as AuthContext);

export default function AuthProvider({ children }: { children: ReactNode }) {
  const [authCtx, setAuthCtx] = useState(initialValue);

  return (
    <AuthContext.Provider value={{ authCtx, setAuthCtx }}>
      {children}
    </AuthContext.Provider>
  );
}
