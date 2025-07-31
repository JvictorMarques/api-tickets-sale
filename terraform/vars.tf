variable "do_token" {
  sensitive = true
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

variable "database_version" {
  type        = string
  description = "Versão do banco de dados"
  default     = "14"
}

variable "database_user" {
  type        = string
  description = "Usuário do banco de dados"
  default     = "ticket_sales_user"
}
