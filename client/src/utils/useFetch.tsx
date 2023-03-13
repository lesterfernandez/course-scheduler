// this file is not used
import { useCallback, useContext } from "react";
import { AuthContext } from "../auth/AuthProvider";

interface UseFetchArgs {
  url: string;
  opts?: RequestInit;
  onError?: () => void;
}

const useFetch = <T,>({ url, opts, onError }: UseFetchArgs) => {
  const { authCtx } = useContext(AuthContext);
  const fetchData = useCallback(
    (data?: string) =>
      fetch(url, {
        ...opts,
        headers: {
          ...opts?.headers,
          authorization: `Bearer ${authCtx.token}`,
          "Content-Type": "application/json",
        },
        body: data,
      }).then(response => response.json() as T),
    [opts, url, authCtx]
  );
  return { fetchData, onError };
};

export default useFetch;
