import { createRouter, createWebHistory } from 'vue-router'
import MainView from '../views/MainView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainView,
      children: [
        {
          path: 'empresa',
          name: 'empresa',
          component: () => import('../views/empresa/EmpresaView.vue'),
        },
        {
          path: 'fornecedor',
          name: 'fornecedor',
          component: () => import('../views/fornecedor/FornecedorView.vue'),
        },
      ]
    },
  ],
})

export default router
