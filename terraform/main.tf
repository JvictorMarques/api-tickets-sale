terraform {
  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = "~> 2.0"
    }
  }
}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_kubernetes_cluster" "k8s-api-ticket-sales" {
  name   = "foo"
  region = "nyc1"
  version = "latest"

  node_pool {
    name       = "worker-pool"
    size       = "s-2vcpu-2gb"
    node_count = 3
  }
}

resource "digitalocean_database_cluster" "postgres" {
  name       = "example-postgres-cluster"
  engine     = "pg"
  version    =  15
  size       = "db-s-1vcpu-1gb"
  region     = "nyc1"
  node_count = 1
}