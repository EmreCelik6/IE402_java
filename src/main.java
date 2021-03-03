import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String filepathInbound = "Inbound.csv";
        String filepathOutbound = "Outbound.csv";
        String filepathHandling = "Handling_docs.csv";
        String filepathStocks = "stocks.csv";
        reader read = new reader(filepathInbound, filepathOutbound,filepathHandling,filepathStocks);
        Process pr = new Process(read.inbounds,read.outbounds,read.VIB_numbers);
        cplex model= new cplex(pr.inbound_data,pr.outbound_data,pr.places);
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        long sec= TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        System.out.println();
        System.out.println("Total Time elapsed: "+ elapsedTime+" milliseconds");
        System.out.println("Total Time elapsed: "+ sec+" seconds");

    }
}