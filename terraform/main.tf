terraform {
  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = "~> 2.0"
    }
  }
  backend "s3" {
    endpoints = {
      s3 = "https://nyc3.digitaloceanspaces.com"
    }
    bucket                      = "tf-jv"
    key                         = "terraform.tfstate"
    skip_credentials_validation = true
    skip_requesting_account_id  = true
    skip_metadata_api_check     = true
    skip_region_validation      = true
    skip_s3_checksum            = true
    region                      = "us-east-1"
    use_lockfile                = true
  }
}

provider "digitalocean" {
  token = var.do_token
}

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

resource "digitalocean_database_cluster" "ticket-sales-db" {
  name       = var.database_name
  engine     = "pg"
  version    = var.database_version
  size       = "db-s-1vcpu-1gb"
  region     = var.region
  node_count = 1
}

resource "digitalocean_database_user" "ticket-sales-db-user" {
  cluster_id = digitalocean_database_cluster.ticket-sales-db.id
  name       = var.database_user
}