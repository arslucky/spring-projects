. ./docker-env.sh

docker compose --file ../docker-compose.yml up -d

#docker compose --file ../docker-compose.yml stop zoo kafka1 kafka2 kafka3
#docker compose --file ../docker-compose.yml rm -f zoo kafka1 kafka2 kafka3
#docker compose --file ../docker-compose.yml create zoo kafka1 kafka2 kafka3
#docker compose --file ../docker-compose.yml start zoo kafka1 kafka2 kafka3