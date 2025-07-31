output "k8s_cluster_kube_config" {
  value     = digitalocean_kubernetes_cluster.k8s-ticket-sales.kube_config
  description = "Arquivo de Kubeconfig do cluster Kubernetes"
  sensitive = true
}
