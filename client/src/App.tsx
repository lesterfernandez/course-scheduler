import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import ColorSwitch from "./components/ColorSwitch";
import Login from "./components/Login";
import ProtectedRoutes from "./components/ProtectedRoutes";
import Root from "./components/Root";
import SignUp from "./components/SignUp";
import AuthProvider from "./context/AuthContext";

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
