package org.demo.ars.log;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author arsen.ibragimov
 *
 */
@SpringBootApplication( scanBasePackages = { "org.demo.ars.log", "org.demo.ars.commons" })
public class LogServerApplication {

    private static Logger log = LoggerFactory.getLogger( LogServerApplication.class);

    private static final String topic = "log"; // TODO: replace

    public static void main( String[] args) {
        SpringApplication.run( LogServerApplication.class, args);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name( topic).build();
    }

    @KafkaListener( id = "logId", topics = topic)
    public void listen( String in) {
        log.info( in);
    }
}
