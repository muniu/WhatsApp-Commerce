#!/bin/bash

# Set default value for with_kafka
with_kafka=false

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case "$1" in
    --with-kafka)
      with_kafka=true
      shift
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Start MongoDB and related services
docker-compose up -d mongodb mongo-express grafana

if [ "$with_kafka" == "true" ]; then
  # Start Kafka and related services
  docker-compose -f docker-compose.yml -f docker-compose.kafka.yml up -d
else
  # Start the application without Kafka
  docker-compose up -d app
fi