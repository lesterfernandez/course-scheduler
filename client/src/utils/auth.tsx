import { AuthContextValue } from "../context/AuthContext";

export const authenticate = () =>
  new Promise<AuthContextValue>(resolve => {
    setTimeout(() => {
      resolve({ loggedIn: false, username: "" });
    }, 2000);
  });
