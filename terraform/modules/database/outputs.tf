output "database_user" {
  value = digitalocean_database_user.ticket-sales-db-user.name
}

output "database_password" {
  value     = digitalocean_database_user.ticket-sales-db-user.password
  sensitive = true
}