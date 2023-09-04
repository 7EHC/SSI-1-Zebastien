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

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/admin/user',
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
  ],
});

export default router;
