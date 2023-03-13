import { Center, Spinner } from "@chakra-ui/react";
import { useContext, useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { AuthContext, AuthContextValueSchema } from "./AuthProvider";
import { saveToken } from "./jwt";

export default function ProtectedRoutes() {
  const [loading, setLoading] = useState(true);
  const { authCtx, setAuthCtx } = useContext(AuthContext);

  useEffect(() => {
    if (authCtx.loggedIn) return;
    fetch("http://localhost:8080/api/auth/login", {
      headers: { authorization: `Bearer ${authCtx.token}` },
    })
      .then(response => response.json())
      .then(data => {
        const parsedData = AuthContextValueSchema.parse(data);
        saveToken(parsedData.token);
        setAuthCtx(parsedData);
      })
      .catch(console.log)
      .finally(() => {
        setLoading(false);
      });
  }, [authCtx, setAuthCtx]);

  if (loading && !authCtx.loggedIn) {
    return (
      <Center h="100vh">
        <Spinner size="xl" speed="0.65s" variant="" color="blue.200" />
      </Center>
    );
  }

  return authCtx.loggedIn ? <Outlet /> : <Navigate to="/signup" />;
}
