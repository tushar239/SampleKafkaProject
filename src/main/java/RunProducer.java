import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Tushar Chokshi @ 12/31/16.
 */
public class RunProducer {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        SimpleProducer.main(new  String[]{"testmultitopic"});

    }
}