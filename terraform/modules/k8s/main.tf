resource "digitalocean_kubernetes_cluster" "k8s-ticket-sales" {
  name    = var.k8s_cluster_name
  region  = var.region
  version = var.k8s_version

  node_pool {
    name       = "pool-ticket-sales"
    size       = "s-2vcpu-2gb"
    node_count = 3
  }
}