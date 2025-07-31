terraform {
  required_version = ">= 1.12.2"
  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = "~> 2.0"
    }
  }
}

resource "digitalocean_kubernetes_cluster" "k8s-ticket-sales" {
  name          = var.k8s_cluster_name
  region        = var.region
  version       = var.k8s_version
  surge_upgrade = true
  auto_upgrade  = true

  node_pool {
    name       = "pool-${var.k8s_cluster_name}"
    size       = "s-2vcpu-2gb"
    node_count = 3

  }
}
