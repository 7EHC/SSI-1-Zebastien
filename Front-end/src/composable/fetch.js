import { useTokenStore } from '../stores/tokenStore.js'

const API_ROOT = import.meta.env.VITE_ROOT_API;

// const getAnnouncement = async () => {
//   try {
//     const tokenStore = useTokenStore()
//     const accessToken = tokenStore.accessToken
//     const { userRole } = useTokenStore()
//     const { userLogin } = useTokenStore()
//     const res = await fetch(
//       `${API_ROOT}/announcements`,
//       // const res = await fetch("http://localhost:8080/api/announcements");
//       {
//         method: "GET",
//         headers: {
//           Authorization: `Bearer ${accessToken}`,
//         },
//       }
//     );
//     if (res.ok) {
//       const ann = await res.json();
//       if(userRole === 'announcer') {
//         const filteredAnnouncementsByOwner = [];
//         for (let i = 0; i < ann.length; i++) {
//           if (ann[i].announcementOwner === userLogin) {
//             filteredAnnouncementsByOwner.push(ann[i])
//           }
//         }
//         console.log(filteredAnnouncementsByOwner);
//         return filteredAnnouncementsByOwner
//       } else {
//         return ann;
//       }
//     } else if (res.status === 401) {
//       const chekky = await reqAccessToken()
//       return chekky
//     }
//   } catch (error) {
//     console.log(`ERROR cannot read data: ${error}`);
//   }
// }

// const getAnnouncement = async () => {
//   try {
//     const tokenStore = useTokenStore()
//     const accessToken = tokenStore.accessToken
//     const { userRole } = useTokenStore()
//     const { userLogin } = useTokenStore()
//     const res = await fetch(
//       `${API_ROOT}/announcements`,
//       // const res = await fetch("http://localhost:8080/api/announcements");
//       {
//         method: "GET",
//         headers: {
//           Authorization: `Bearer ${accessToken}`,
//         },
//       }
//     );
//     if (res.ok) {
//       const ann = await res.json();
//       if(userRole === 'announcer') {
//         const filteredAnnouncementsByOwner = [];
//         for (let i = 0; i < ann.length; i++) {
//           if (ann[i].announcementOwner === userLogin) {
//             filteredAnnouncementsByOwner.push(ann[i])
//           }
//         }
//         console.log(filteredAnnouncementsByOwner);
//         return filteredAnnouncementsByOwner
//       } else {
//         return ann;
//       }
//     } else if (res.status === 401) {
//       const chekky = await reqAccessToken()
//       return chekky
//     }
//   } catch (error) {
//     console.log(`ERROR cannot read data: ${error}`);
//   }
// }

const getAnnouncement = async () => {
  try {
    const tokenStore = useTokenStore()
    const accessToken = tokenStore.accessToken
    // const { userRole } = useTokenStore()
    // const { userLogin } = useTokenStore()
    const res = await fetch(
      `${API_ROOT}/announcements`,
      // const res = await fetch("http://localhost:8080/api/announcements");
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if(res.ok) {
      const ann = await res.json();
      return ann;
      }
  } catch (error) {
    console.log(`ERROR cannot read data: ${error}`);
  }
}
// const getAnnouncement = async () => {
//   try {
//     const res = await fetch(`${API_ROOT}/announcements`);
//     // const res = await fetch("http://localhost:8080/api/announcements");
//     if(res.ok) {
//     const ann = await res.json();
//     return ann;
//     }
//   } catch (error) {
//     console.log(`ERROR cannot read data: ${error}`);
//   }
// };

const getPageAnn = async (page, category) => {
  if (category === undefined || null) {
    category = 0;
  }
  try {
    const tokenStore = useTokenStore()
    const accessToken = tokenStore.accessToken
    const res = await fetch(
      `${API_ROOT}/announcements/pages?page=${page}&mode=active&category=${category}`,
      // const res = await fetch(`http://localhost:8080/api/announcements/pages?page=${page}&mode=active&category=${category}`);
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (res.ok) {
      const ann = await res.json();
      return ann;
    } else if (res.status === 401) {
      const chekky = await reqAccessToken()
      return chekky
    }
  } catch (error) {
    console.log(`ERROR cannot read data: ${error}`);
  }
};

const getClosePageAnn = async (page, category) => {
  if (category === undefined || null) {
    category = 0;
  }
  if (page === undefined || null) {
    page = 0;
  }
  try {
    const tokenStore = useTokenStore()
    const accessToken = tokenStore.accessToken
    const res = await fetch(
      `${API_ROOT}/announcements/pages?page=${page}&mode=close&category=${category}`,
      // const res = await fetch(`http://localhost:8080/api/announcements/pages?page=${page}&mode=close&category=${category}`);
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (res.ok) {
      const ann = await res.json();
      return ann;
    } else if (res.status === 401) {
      const chekky = await reqAccessToken()
      return chekky
    }
  } catch (error) {
    console.log(`ERROR cannot read data: ${error}`);
  }
};

const targetId = async (id) => {
  try {
    const tokenStore = useTokenStore()
    const accessToken = tokenStore.accessToken
    const res = await fetch(
      `${API_ROOT}/announcements/${id}`,
      // const res = await fetch(`http://localhost:8080/api/announcements/${id}`);
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (res.ok) {
      const annDe = await res.json();
      return annDe;
    } else if (res.status === 401) {
      const chekky = await reqAccessToken()
      return chekky
    }
  } catch (error) {
    console.log(`ERROR cannot read data: ${error}`);
  }
};

const getAllUsers = async () => {
  try {
    const tokenStore = useTokenStore()
    const accessToken = tokenStore.accessToken
    const res = await fetch(
      `${API_ROOT}/users`,
      // const res = await fetch("http://localhost:8080/api/users");
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (res.ok) {
      const user = await res.json();
      return user;
    } else if (res.status === 401) {
      const chekky = await reqAccessToken()
      return chekky
    }
  } catch (error) {
    console.log(`ERROR cannot read data: ${error}`);
  }
};

const reqAccessToken = async () => {
  try {
    const tokenStore = useTokenStore()
    const refreshToken = tokenStore.refreshToken
    const res = await fetch(
      `${API_ROOT}/token`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${refreshToken}`,
        },
      }
    );
    if (res.ok) {
      const response = await res.json();
      tokenStore.setAccessToken(response.token)
      return 'new token success'
    } else if (res.status === 401) {
      tokenStore.setAccessToken('')
      tokenStore.setRefreshToken('')
      return 'refresh expried'
    }
  } catch (error) {
    console.log(`ERROR ${error}`);
  }
};

const getUserById = async (id) => {
  try {
    const tokenStore = useTokenStore()
    const accessToken = tokenStore.accessToken
    const res = await fetch(
      `${API_ROOT}/users/${id}`,
      // const res = await fetch(`http://localhost:8080/api/users/${id}`);
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (res.ok) {
      const userView = await res.json();
      return userView;
    } else if (res.status === 401) {
      const chekky = await reqAccessToken()
      return chekky
    }
  } catch (error) {
    console.log(`ERROR cannot read data: ${error}`);
  }
};

export {
  getAnnouncement,
  targetId,
  getPageAnn,
  getClosePageAnn,
  getAllUsers,
  getUserById,
  reqAccessToken
};
