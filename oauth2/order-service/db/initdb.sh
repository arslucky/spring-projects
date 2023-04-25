i=1
max_attempts=10
waiting=10

until mongosh $MONGO_HOST:$MONGO_PORT --username $MONGO_ROOT --password $MONGO_ROOT_PASSWORD -f ./check-connect.js
do
  if [ $i -gt $max_attempts ]
  then
    exit
  fi    
  echo "Waiting "$waiting" sec for database connection, attempt: "$i", max: "$max_attempts
  # wait for 10 seconds before check again
  sleep $waiting
  i=`expr $i + 1`
done

mongosh $MONGO_HOST:$MONGO_PORT --username $MONGO_ROOT --password $MONGO_ROOT_PASSWORD -f ./connect-and-insert-dev.js