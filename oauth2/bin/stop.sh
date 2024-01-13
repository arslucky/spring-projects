function kill_proc {
    for pid in $(ps -ax | grep "${1}" | awk '{print $1}')
    do
        #ps -ax
        echo "killing "$pid" "${1}
        kill $pid
    done
}
kill_proc "../order-service/target/order-service"
kill_proc "../customer-service/target/customer-service"
kill_proc "../resource/target/resource"
kill_proc "../ui/target/ui"
kill_proc "../gateway-zuul/target/gateway"
kill_proc "../authorization-server/target/authorization-server"
sleep 2
kill_proc "../eureka-server/target/eureka-server"
kill_proc "../config-server/target/config-server"