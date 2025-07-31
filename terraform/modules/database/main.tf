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