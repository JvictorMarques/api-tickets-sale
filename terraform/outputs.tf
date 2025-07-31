output "database_username" {
  value = module.database.database_user
}

output "database_password" {
  value     = module.database.database_password
  sensitive = true
}

output "kube_config" {
  value     = module.kubernetes.k8s_cluster_kube_config
  sensitive = true
}
