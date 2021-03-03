import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class main2 {
    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        System.out.println("Time starts");
        int[][]inbound=null;
        int[][]outbound=null;
        int[][]places=null;
        BufferedReader br=null;
        try{
            br=new BufferedReader(new FileReader("inbound_data.csv"));
            String line=br.readLine();
            String[] lines=line.split(";");
            inbound=new int[365][lines.length];
            for(int i=0;i<lines.length;i++){
                inbound[0][i]=Integer.parseInt(lines[i]);
            }
            int d=1;
            while ((line = br.readLine()) != null) {
                lines=line.split(";");
                for(int i=0;i<lines.length;i++){
                    inbound[d][i]=Integer.parseInt(lines[i]);
                }
                d++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bri=null;
        try{
            bri=new BufferedReader(new FileReader("outbound_data.csv"));
            String line=bri.readLine();
            String[] lines=line.split(";");
            outbound=new int[365][lines.length];
            for(int i=0;i<lines.length;i++){
                outbound[0][i]=Integer.parseInt(lines[i]);
            }
            int d=1;
            while ((line = bri.readLine()) != null) {
                lines=line.split(";");
                for(int i=0;i<lines.length;i++){
                    outbound[d][i]=Integer.parseInt(lines[i]);
                }
                d++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader brp=null;
        try{
            brp=new BufferedReader(new FileReader("places.csv"));
            String line=brp.readLine();
            String[] lines=line.split(";");
            places=new int[365][lines.length];
            for(int i=0;i<lines.length;i++){
                places[0][i]=Integer.parseInt(lines[i]);
            }
            int d=1;
            while ((line = brp.readLine()) != null) {
                lines=line.split(";");
                for(int i=0;i<lines.length;i++){
                    places[d][i]=Integer.parseInt(lines[i]);
                }
                d++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("No problem so far");
        cplex model = new cplex(inbound,outbound,places);
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        long sec= TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        System.out.println();
        System.out.println("Time elapsed: "+ elapsedTime+" milliseconds");
        System.out.println("Time elapsed: "+ sec+" seconds");
    }


}
