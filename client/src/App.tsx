import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import AuthProvider from "./auth/AuthProvider";
import Login from "./auth/Login";
import ProtectedRoutes from "./auth/ProtectedRoutes";
import SignUp from "./auth/SignUp";
import ColorSwitch from "./components/ColorSwitch";
import Root from "./components/Root";

const browserRouter = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route element={<ProtectedRoutes />}>
        <Route path="/" element={<Root />} />
      </Route>
      <Route path="/signup" element={<SignUp />} />
      <Route path="/login" element={<Login />} />
    </>
  )
);

function App() {
  return (
    <div className="App">
      <ColorSwitch />
      <AuthProvider>
        <RouterProvider router={browserRouter} />
      </AuthProvider>
    </div>
  );
}

export default App;
