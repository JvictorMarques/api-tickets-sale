variable "do_token" {
}
variable "region" {
  type        = string
  description = "Região da infraestrutura"
  default     = "nyc3"
}

variable "k8s_cluster_name" {
  type        = string
  description = "Nome do cluster Kubernetes para o sistema de vendas de ingressos"
  default     = "k8s-ticket-sales-cluster"
}

variable "k8s_version" {
  type        = string
  description = "Versão do Kubernetes"
  default     = "1.33.1-do.2"
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