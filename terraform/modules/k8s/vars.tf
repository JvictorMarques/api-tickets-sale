variable "region" {
  type        = string
  description = "Região da infraestrutura"
}

variable "k8s_cluster_name" {
  type        = string
  description = "Nome do cluster Kubernetes para o sistema de vendas de ingressos"
  default     = "k8s-ticket-sales-cluster"
}

variable "k8s_version" {
  type        = string
  description = "Versão do Kubernetes"
  default     = "latest"
}