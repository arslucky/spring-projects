#set -a
#HOST_DEPLOY=192.168.1.82
#set +a

#get default .env file
docker compose --file ../docker-compose-env.yml up -d
sleep 7
docker compose --file ../docker-compose-app.yml up -d