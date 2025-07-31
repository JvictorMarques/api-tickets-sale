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

module "kubernetes" {
  source = "./modules/kubernetes"
  region = var.region
}

module "database" {
  source        = "./modules/database"
  region        = var.region
  database_name = var.database_name
  database_user = var.database_user
}
