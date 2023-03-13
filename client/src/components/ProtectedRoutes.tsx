import { Center, Spinner } from "@chakra-ui/react";
import { useContext, useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { authenticate } from "../utils/auth";

export default function ProtectedRoutes() {
  const [loading, setLoading] = useState(true);
  const { authCtx, setAuthCtx } = useContext(AuthContext);

  useEffect(() => {
    console.log("effect", JSON.stringify(authCtx));
    if (authCtx.loggedIn) return;
    authenticate()
      .then(data => {
        if (authCtx.loggedIn) return;
        setAuthCtx(data);
        setLoading(false);
      })
      .catch(console.log);
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
