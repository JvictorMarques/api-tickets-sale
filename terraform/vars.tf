variable "do_token" {
  type        = string
  description = "Token de autenticação da DigitalOcean"
  sensitive   = true
}

variable "region" {
  type        = string
  description = "Região da infraestrutura"
  default     = "nyc3"

}

variable "database_name" {
  type        = string
  description = "Nome do banco de dados"
  default     = "ticket-sales-db"
}

variable "database_user" {
  type        = string
  description = "Usuário do banco de dados"
  default     = "ticket_sales_user"
}
