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

const requireAccessToken = (to, from, next) => {
  if (localStorage.getItem('accessToken')) {
    next();
  } else {
    next('/login');
  }
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/announcement',
    },
    {
      path: '/admin/announcement/:id',
      name: 'View',
      component: View,
      beforeEnter: requireAccessToken
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
      beforeEnter: requireAccessToken
    },
    {
      path: '/admin/announcement',
      name: 'Announcement',
      component: Announcement,
      beforeEnter: requireAccessToken
    },
    {
      path: '/admin/announcement/add',
      name: 'AddAnnouncement',
      component: AddAnnouncement,
      beforeEnter: requireAccessToken
    },
    {
      path: '/announcement',
      name: 'User',
      component: User,
    },
    {
      path: '/admin/user',
      name: 'SasUser',
      component: SasUser,
      beforeEnter: requireAccessToken
    },
    {
      path: '/admin/user/add',
      name: 'SasAddUser',
      component: SasAddUser,
      beforeEnter: requireAccessToken
    },
    {
      path: '/admin/user/:id/edit',
      name: 'SasEditUser',
      component: SasEditUser,
      beforeEnter: requireAccessToken
    },
    {
      path: '/admin/user/match',
      name: 'Match',
      component: Match,
      beforeEnter: requireAccessToken
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    }
  ],
});

export default router;
