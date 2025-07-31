output "database_user" {
  value = digitalocean_database_user.ticket-sales-db-user.name
}

output "database_password" {
  value     = digitalocean_database_user.ticket-sales-db-user.password
  sensitive = true
}

output "k8s_cluster_id" {
  value     = digitalocean_kubernetes_cluster.k8s-ticket-sales.kube_config
  sensitive = true
}