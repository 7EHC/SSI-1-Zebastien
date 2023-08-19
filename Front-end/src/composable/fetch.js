// const getAnnouncement= async () => {
//     try {
//             // const res = await fetch('http://localhost:8080/api/announcements')
//             const res = await fetch('http://ip22ssi1.sit.kmutt.ac.th:8080/api/announcements')
//             const ann = await res.json()
//             return ann
//     } catch (error) {
//         console.log(`ERROR cannot read data: ${error}`);
//     }
// }

// const targetId = async (id) =>{
//     // console.log(announById.value)
//         // return fetch(`http://localhost:8080/api/announcements/${id}`)
//         return fetch(`http://ip22ssi1.sit.kmutt.ac.th:8080/api/announcements/${id}`)
//         .then(resp => resp.json())
//         .catch(error => `ERROR cannot read data: ${error}`);      
//   }

// export {getAnnouncement, targetId}

const API_ROOT = import.meta.env.VITE_ROOT_API
// console.log(API_ROOT)

const getAnnouncement = async () => {
    try {
      const res = await fetch(`${API_ROOT}`);
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
      const res = await fetch(`${API_ROOT}/pages?page=${page}&mode=active&category=${category}`);
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
      const res = await fetch(`${API_ROOT}/pages?page=${page}&mode=close&category=${category}`);
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
      const res = await fetch(`${API_ROOT}/${id}`);
      // const res = await fetch(`http://localhost:8080/api/announcements/${id}`);
      if(res.ok) {
          const annDe = await res.json()
          return annDe
      }
    } catch (error) {
      console.log(`ERROR cannot read data: ${error}`);
    }
  };
  
  export { getAnnouncement, targetId, getPageAnn, getClosePageAnn };