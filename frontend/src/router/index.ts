import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: "/figma",
      name:"figma",
      component: () => import('../components/figma.vue')
    },
    {
      path: "/new-vote",
      name:"new-vote",
      component: () => import('../components/NewVote.vue')
    },
    {
      path: "/register",
      name:"register",
      component: () => import('../components/RegisterAccount.vue')
    },
    {
      path:"/login",
      name:"login",
      component: () => import('../components/LogIn.vue')
    },
    {
      path:"/swipe",
      name:"swipe",
      component: () => import('../components/SwipePage.vue')
    },
    {
      path:"/event-list",
      name:"event-list",
      component: () => import('../components/EventList.vue')
    },
    {
      path:"/event-page",
      name:"event-page",
      component: () => import('../components/EventPage.vue')
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue'),
    // },
  ],
})

export default router
