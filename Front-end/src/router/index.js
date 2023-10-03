import { createRouter, createWebHistory } from "vue-router";
import Announcement from "../components/Announcement.vue";
import View from "../components/View.vue";
import AddAnnouncement from "../components/AddAnnouncement.vue"
import EditAnnouncement from "../components/EditAnnouncement.vue"
import User from "../components/User.vue"
import UserView from "../components/UserView.vue"
import SasUser from "../components/SasUser.vue"
import SasAddUser from "../components/SasAddUser.vue"
import SasEditUser from "../components/SasEditUser.vue"
import Match from "../components/Match.vue"
import Login from "../components/Login.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/admin/announcement',
    },
    {
      path: '/admin/announcement',
      beforeEnter: (to, from, next) => {
        if (localStorage.getItem('accessToken').length === 0) {
          // Redirect to the login page if accessToken is empty
          next('/login');
        } else {
          // Allow access to the admin page if accessToken is not empty
          next();
        }
      },
      component: Announcement, // Replace with your actual admin component
      children: [
        {
          path: '/admin/announcement',
          component: Announcement, // Replace with your announcement component
        }]
      },
      {
        path: '/admin/user',
        beforeEnter: (to, from, next) => {
          if (localStorage.getItem('accessToken').length === 0) {
            // Redirect to the login page if accessToken is empty
            next('/login');
          } else {
            // Allow access to the admin page if accessToken is not empty
            next();
          }
        },
        component: SasUser, // Replace with your actual admin component
        children: [
          {
            path: '/admin/user',
            component: SasUser, // Replace with your announcement component
          }]
        },
    {
      path: '/admin/announcement/:id',
      name: 'View',
      component: View,
    },
    {
      path: '/announcement/:id',
      name: 'UserView',
      component: UserView,
    },
    {
      path: '/admin/announcement/:id/edit',
      name: 'Edit',
      component: EditAnnouncement,
    },
    {
      path: '/admin/announcement',
      name: 'Announcement',
      component: Announcement,
    },
    {
      path: '/admin/announcement/add',
      name: 'AddAnnouncement',
      component: AddAnnouncement,
    },
    {
      path: '/announcement',
      name: 'User',
      component: User,
    },
    {
      path: '/admin/user',
      name: 'SasUser',
      component: SasUser
    },
    {
      path: '/admin/user/add',
      name: 'SasAddUser',
      component: SasAddUser
    },
    {
      path: '/admin/user/:id/edit',
      name: 'SasEditUser',
      component: SasEditUser
    },
    {
      path: '/admin/user/match',
      name: 'Match',
      component: Match
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    }
  ],
});

export default router;
