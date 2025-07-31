output "k8s_cluster_id" {
  value     = digitalocean_kubernetes_cluster.k8s-ticket-sales.kube_config
  sensitive = true
}