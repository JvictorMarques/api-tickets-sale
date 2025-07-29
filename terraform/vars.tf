variable "do_token" {
}

variable "sonarqube_name" {
  type        =  string
  description = "Nome do Droplet SonarQube"
  default     = "sonarqube"
}

variable "region" {
  type        = string
  description = "Região da infraestrutura"
  default     = "nyc1"
}
