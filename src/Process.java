import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Process {
    public int[][] inbound_data;
    public int[][] outbound_data;
    int dates = 365;
    public int[][] places;
    public Adres[][] adresler = new Adres[1][2];

    Process(ArrayList<ArrayList<Inbound>> in, ArrayList<ArrayList<Outbound>> out, ArrayList<VIB> vibs) throws IOException {
        //DATA BASMAK İÇİN
        /*this.inbound_data = new int[dates][vibs.size()];
        this.outbound_data = new int[dates][vibs.size()];
        this.places = new int[dates][vibs.size()];
        for (int k = 0; k < vibs.size(); k++) {
            vibs.get(k).vib_no = k;
        }
        for (int d = 0; d < dates; d++) {
            for (int i = 0; i < vibs.size(); i++) {
                inbound_data[d][i] = 0;
                outbound_data[d][i] = 0;
            }
        }
        for (int i = 0; i < in.size(); i++) {
            for (int k = 0; k < in.get(i).size(); k++) {
                inbound_data[i][in.get(i).get(k).vib.vib_no] = in.get(i).get(k).order_qnt;
            }
        }

        for (int i = 0; i < out.size(); i++) {
            for (int k = 0; k < out.get(i).size(); k++) {
                outbound_data[i][out.get(i).get(k).vib.vib_no] = out.get(i).get(k).out_qnt;
            }
        }
        for(int d=0;d<dates;d++){
            for (int i=0;i<vibs.size();i++){
                places[d][i]=vibs.get(i).place;
            }
        }
        //12 AYLIK DATA İÇİN

        /*ArrayList<Integer> günler= new ArrayList<Integer>();
        günler.add(31);günler.add(28);günler.add(31);günler.add(30);günler.add(31);günler.add(30);
        günler.add(31);günler.add(31);günler.add(30);günler.add(31);günler.add(30);günler.add(31);
        int gün=0;
        for (int i=0;i<günler.size();i++){
            for (int j=0;j<günler.get(i);j++){
                for (int v=0;v<in.get(gün+j).size();v++){
                    inbound_data[i][in.get(gün+j).get(v).vib.vib_no]+=in.get(gün+j).get(v).order_qnt;
                }
            }
            gün+=günler.get(i);
        }
        int gün2=0;
        for (int i=0;i<günler.size();i++){
            for (int j=0;j<günler.get(i);j++){
                for (int v=0;v<out.get(gün2+j).size();v++){
                    outbound_data[i][out.get(gün2+j).get(v).vib.vib_no]+=out.get(gün2+j).get(v).out_qnt;
                }
            }
            gün2+=günler.get(i);
        }

        writedatas(inbound_data,"inbound_data.csv");
        writedatas(outbound_data,"outbound_data.csv");
        writeplaces(vibs,"places.csv");*/

        int inbound7=0;
        for (int d=0;d<7;d++){
            for (int i=0;i<in.get(d).size();i++){
                inbound7+=in.get(d).get(i).order_qnt;
            }
        }
        System.out.println("7 day total inbound: "+inbound7);
    }

    void writedatas(int[][] data,String path) throws IOException {
        FileWriter csvWriter = new FileWriter(path);
        for (int i=0;i<data.length;i++){
            for (int j=0;j<data[i].length;j++){
                csvWriter.append(Integer.toString(data[i][j]));
                csvWriter.append(";");
            }
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
    }
    void writeplaces(ArrayList<VIB> vibs,String path) throws IOException {
        FileWriter csv = new FileWriter(path);
        for(int d=0;d<365;d++){
            for (int i=0;i<vibs.size();i++){
                csv.append(Integer.toString(vibs.get(i).place));
                csv.append(";");
            }
            csv.append("\n");
        }
        csv.flush();
        csv.close();
    }

}
