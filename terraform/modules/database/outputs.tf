output "database_user" {
  value = digitalocean_database_user.ticket-sales-db-user.name
  description = "Nome do usuário do banco de dados"
}

output "database_password" {
  value     = digitalocean_database_user.ticket-sales-db-user.password
  description = "Senha do usuário do banco de dados"
  sensitive = true
}
