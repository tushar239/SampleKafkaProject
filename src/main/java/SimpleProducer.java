import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Tushar Chokshi @ 12/31/16.
 */
public class SimpleProducer {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        // Check arguments length value
        if (args.length == 0) {
            System.out.println("Enter topic name");
            return;
        }

        //Assign topicName to string variable
        String topicName = args[0].toString();

        // set up the producer
        KafkaProducer<String, String> producer;
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }

        // gives partitions information of that topic
        // List<PartitionInfo> partitionInfos = producer.partitionsFor(topicName);

        // .metrics() returns the internal metrics mantained by the producer
        // Map<MetricName, ? extends Metric> metrics = producer.metrics();

        for (int i = 0; i < 10; i++) {
            // producer needs
            // - a list of brokers which is is provided in producer.properties
            // - topic name
            // - key of the message
            // - message (value)
            // - serializer of key and value, which is provided in producer.properties

            // Future<RecordMetadata> send(ProducerRecord<K,V> record)
            // Future<RecordMetadata> send(ProducerRecord<K,V> record, Callback callback)
            // The send() method is asynchronous. When called it adds the record to a buffer of pending record sends and immediately returns. This allows the producer to batch together individual records for efficiency.
            RecordMetadata recordMetadata = producer.send(new ProducerRecord<>(topicName, Integer.toString(i), Integer.toString(i))).get();
            //System.out.println(recordMetadata.partition());

            // Invoking this method makes all buffered records immediately available to send (even if linger.ms is greater than 0) and blocks on the completion of the requests associated with these records.
            //producer.flush();

        }
        System.out.println("Message sent successfully");
        producer.close();

    }
}