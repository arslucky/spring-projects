docker compose --file ../docker-compose-env.yml --env-file=../.env up -d
sleep 7
docker compose --file ../docker-compose-app.yml up -d
#docker compose --file ../docker-compose-app.yml stop