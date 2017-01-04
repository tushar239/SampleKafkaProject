import com.google.common.io.Resources;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

/**
 * @author Tushar Chokshi @ 12/31/16.
 */
public class SimpleConsumer {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Enter topic name");
            return;
        }
        //Kafka consumer configuration settings
        String topicName = args[0].toString();

        KafkaConsumer<String, String> consumer;

        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            if (properties.getProperty("group.id") == null) {
                properties.setProperty("group.id", "group-" + new Random().nextInt(100000));
            }
            consumer = new KafkaConsumer<>(properties);
        }

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));
        // or
        // you can assign specific partitions from which consumer can read messages
        //consumer.assign(list of partitions);

        // This will give you all assigned partitions. If Topic subscription is used, then it will probably give you all partitions of that topic.
        // If you have assigned partitions using .assign method, then it will give you only those assigned partitions.
        // Set<TopicPartition> assignment = consumer.assignment();

        //print the topic name
        System.out.println("Subscribed to topic " + topicName);

        while (true) {

            // Consumer polls the topic every 100 ms to see whether there are any new messages
            // once the messages are read from a partition, offset is incremented for that partition and consumer group and saved in zookeeper
            // next time, any consumer from the same consumer group will start reading the messages from the next offset
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                // print the offset,key and value for the consumer records.
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            }
        }
    }
}
