i=1
max_attempts=30
waiting=10

until mysql -u$MYSQL_ROOT -p$MYSQL_ROOT_PASSWORD --host=$MYSQL_HOST --port=$MYSQL_PORT -e "select 1 from dual"
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
    
mysql -u$MYSQL_ROOT -p$MYSQL_ROOT_PASSWORD --host=$MYSQL_HOST --port=$MYSQL_PORT < ./dev-database.sql