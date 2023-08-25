const API_ROOT = import.meta.env.VITE_ROOT_API
// console.log(API_ROOT)

const getAnnouncement = async () => {
    try {
      const res = await fetch(`${API_ROOT}/announcements`);
      // const res = await fetch("http://localhost:8080/api/announcements");
      if(res.ok) {
      const ann = await res.json();
      return ann;
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };

  const getPageAnn = async (page,category) => {
    if(category === undefined || null){
      category = 0
    }
    try {
      const res = await fetch(`${API_ROOT}/announcements/pages?page=${page}&mode=active`);
      // const res = await fetch(`http://localhost:8080/api/announcements/pages?page=${page}&mode=active&category=${category}`);
      if(res.ok) {
      const ann = await res.json();
      return ann;
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };

  const getClosePageAnn = async (page,category) => {
    if(category === undefined || null){
      category = 0
    }
    if(page === undefined || null){
      page = 0
    }
    try {
      const res = await fetch(`${API_ROOT}/announcements/pages?page=${page}&mode=close`);
      // const res = await fetch(`http://localhost:8080/api/announcements/pages?page=${page}&mode=close&category=${category}`);
      if(res.ok) {
      const ann = await res.json();
      return ann;
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };
  
  const targetId = async (id) => {
    try {
      const res = await fetch(`${API_ROOT}/announcements/${id}`);
      // const res = await fetch(`http://localhost:8080/api/announcements/${id}`);
      if(res.ok) {
          const annDe = await res.json()
          return annDe
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };

  const getAllUsers = async () => {
    try {
      const res = await fetch(`${API_ROOT}/users`);
      // const res = await fetch("http://localhost:8080/api/users");
      if(res.ok) {
      const user = await res.json();
      return user;
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };

  const getUserById = async (id) => {
    try {
      const res = await fetch(`${API_ROOT}/users/${id}`);
      // const res = await fetch(`http://localhost:8080/api/users/${id}`);
      if(res.ok) {
          const userView = await res.json()
          return userView
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };
  
  export { getAnnouncement, targetId, getPageAnn, getClosePageAnn, getAllUsers, getUserById };
