import { Center, Spinner } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { authStoreValueSchema, useAuthStore } from "./auth-store";
import { saveToken } from "./jwt";

export default function ProtectedRoutes() {
  const [loading, setLoading] = useState(true);
  const { token, loggedIn } = useAuthStore();

  useEffect(() => {
    if (loggedIn) return;
    fetch("http://localhost:8080/api/auth/login", {
      headers: { authorization: `Bearer ${token}` },
    })
      .then(response => response.json())
      .then(data => {
        const parsedData = authStoreValueSchema.parse(data);
        saveToken(parsedData.token);
        useAuthStore.setState(parsedData);
      })
      .catch(console.log)
      .finally(() => {
        setLoading(false);
      });
  }, [token, loggedIn]);

  if (loading && !loggedIn) {
    return (
      <Center h="100vh">
        <Spinner size="xl" speed="0.65s" variant="" color="blue.200" />
      </Center>
    );
  }

  return loggedIn ? <Outlet /> : <Navigate to="/signup" />;
}
