export const authenticate = () =>
  new Promise<{ loggedIn: boolean }>(resolve => {
    setTimeout(() => {
      resolve({ loggedIn: false });
    }, 2000);
  });
