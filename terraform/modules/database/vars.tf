variable "region" {
  type        = string
  description = "Região da infraestrutura"
}

variable "database_name" {
  type        = string
  description = "Nome do banco de dados para o sistema de vendas de ingressos"
  default     = "ticket-sales-db"
}

variable "database_version" {
  type        = number
  description = "Versão do banco de dados PostgreSQL"
  default     = "14"
}

variable "database_user" {
  type        = string
  description = "Usuário do banco de dados"
  default     = "ticket_sales_user"
}
