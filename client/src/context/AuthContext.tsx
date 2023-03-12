import {
  createContext,
  ReactNode,
  useState,
  type Dispatch,
  type SetStateAction,
} from "react";

type Context = {
  authCtx: {
    loggedIn: boolean;
  };
  setAuthCtx: Dispatch<SetStateAction<Context["authCtx"]>>;
};

const initialValue: Context["authCtx"] = {
  loggedIn: false,
};

export const AuthContext = createContext({} as Context);

export default function AuthProvider({ children }: { children: ReactNode }) {
  const [authCtx, setAuthCtx] = useState(initialValue);

  return (
    <AuthContext.Provider value={{ authCtx, setAuthCtx }}>
      {children}
    </AuthContext.Provider>
  );
}
