#!/bin/bash

# Exit script on error
set -e

# Variables
IMAGE_NAME="order-service-image"
DOCKER_COMPOSE_FILE="docker-compose.yml"
MYSQL_ROOT_PASSWORD="Q7%xF@b9$2tH!kLm"
GOOGLE_MAPS_API_KEY="AIzaSyAbqbMx0HfUf7jyGhlupIHRr9fSyS8Pskc"

# Export the database password and API key as environment variables
export DB_PASSWORD=$MYSQL_ROOT_PASSWORD
export GOOGLE_MAPS_API_KEY=$GOOGLE_MAPS_API_KEY

# 1. Build the Docker image for the Spring Boot application
echo "Building the Docker image..."
docker build -t $IMAGE_NAME .

# 2. Start Docker Compose to create and run containers
echo "Starting Docker containers..."
docker-compose up -d

# Wait for MySQL to start up
echo "Waiting for MySQL to initialize... (30s)"
sleep 30

# 3. Create tables using a DDL script
# Assuming you have a file named schema.sql in the same directory
if [ -f "schema.sql" ]; then
  echo "Initializing the database..."
  docker exec -i mysql-db mysql -u root -p$MYSQL_ROOT_PASSWORD orders_db < schema.sql
else
  echo "DDL script schema.sql not found. Skipping database initialization."
fi

echo "Deployment completed successfully!"
