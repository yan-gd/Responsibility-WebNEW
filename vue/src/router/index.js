import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import DriverManagement from '../views/DriverManagement.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/driver-management',
    name: 'DriverManagement',
    component: DriverManagement
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
