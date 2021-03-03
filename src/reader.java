import java.io.*;
import java.util.ArrayList;

public class reader {
    public static final double handling_doc_costs[] = {2.121, 0.512, 0.512, 1.916, 8.354, 0.497, 7.454, 1.916, 2.39, 0.223};
    public ArrayList<VIB> VIB_numbers = new ArrayList<VIB>();
    public ArrayList<String> VIB_numbers_S = new ArrayList<String>();
    public ArrayList<String> Handlingdocs = new ArrayList<String>();
    public ArrayList<ArrayList<Inbound>> inbounds = new ArrayList<>();
    public ArrayList<ArrayList<Outbound>> outbounds = new ArrayList<>();

    reader(String inbound, String outbound, String handling, String stock) throws IOException {
        for (int i = 0; i < 365; i++) {
            inbounds.add(new ArrayList<Inbound>());
            outbounds.add(new ArrayList<Outbound>());
        }
        read_inbound(inbound, VIB_numbers_S, VIB_numbers, inbounds, false);
        read_outbound(outbound, VIB_numbers_S, VIB_numbers, outbounds, false);
        read_stocks(stock, VIB_numbers_S, VIB_numbers, false);
        String output = write(VIB_numbers, "analiz.csv");
        analyse(output);
        read_inbound(inbound, VIB_numbers_S, VIB_numbers, inbounds, true);
        read_outbound(outbound, VIB_numbers_S, VIB_numbers, outbounds, true);
        read_handling(handling, VIB_numbers_S, VIB_numbers);
    }

    private void read_stocks(String path, ArrayList<String> S, ArrayList<VIB> V, boolean state) {
        BufferedReader brs = null;
        try {
            brs = new BufferedReader(new FileReader(path));
            String line = brs.readLine();
            while ((line = brs.readLine()) != null) {
                String[] token = line.split(";");
                if (!S.contains(token[0].substring(1, token[0].length() - 1))) {
                    S.add((token[0].substring(1, token[0].length() - 1)));
                    V.add(new VIB(token[0].substring(1, token[0].length() - 1)));
                    if (token[1].contains("-")) {
                        String str = token[1].substring(1);
                        V.get(S.indexOf(token[0].substring(1, token[0].length() - 1))).add_initial_storage(Integer.parseInt(str) * -1);
                    } else {
                        String str = token[1];
                        V.get(S.indexOf(token[0].substring(1, token[0].length() - 1))).add_initial_storage(Integer.parseInt(str));
                    }
                } else {
                    if (token[1].contains("-")) {
                        String str = token[1].substring(1);
                        V.get(S.indexOf(token[0].substring(1, token[0].length() - 1))).add_initial_storage(Integer.parseInt(str) * -1);
                    } else {
                        String str = token[1];
                        V.get(S.indexOf(token[0].substring(1, token[0].length() - 1))).add_initial_storage(Integer.parseInt(str));
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read_inbound(String path, ArrayList<String> S, ArrayList<VIB> V, ArrayList<ArrayList<Inbound>> in, boolean state) {
        if (!state) {
            BufferedReader bri = null;
            try {
                bri = new BufferedReader(new FileReader(path));
                String line = bri.readLine();
                while ((line = bri.readLine()) != null) {
                    String[] token = line.split(";");
                    String vibstr=token[1];
                    //if(Integer.parseInt(token[0])<=43472){
                    if (!S.contains(vibstr)) {
                        S.add((vibstr));
                        V.add(new VIB(vibstr));
                        String str = token[2];
                        V.get(S.indexOf(vibstr)).add_inbound(Integer.parseInt(str));
                    } else {
                        String str = token[2];
                        V.get(S.indexOf(vibstr)).add_inbound(Integer.parseInt(str));
                    }
                }//}
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BufferedReader bri = null;
            try {
                bri = new BufferedReader(new FileReader(path));
                String line = bri.readLine();
                while ((line = bri.readLine()) != null) {
                    String[] token = line.split(";");
                    String vibstr = token[1];
                    //if (Integer.parseInt(token[0]) <= 43472){
                        if (S.contains(vibstr)) {
                            String str = token[0];
                            in.get((Integer.parseInt(str) - 43466)).add(new Inbound(V.get(S.indexOf(vibstr)), Integer.parseInt(token[2]), Integer.parseInt(str) - 43466));
                            if (token[4].contains("Cerkezk�y")) {
                                V.get(S.indexOf(vibstr)).manufacturer = token[4];
                                V.get(S.indexOf(vibstr)).place = 0;
                            } else {
                                V.get(S.indexOf(vibstr)).manufacturer = token[4];
                                V.get(S.indexOf(vibstr)).place = 1;
                            }
                            String div = token[3];
                            if (div.equals("PDC") || div.equals("PLC") || div.equals("PRF")) {
                                V.get(S.indexOf(vibstr)).taşıma_cap = 12;
                            } else {
                                V.get(S.indexOf(vibstr)).taşıma_cap = 18;
                            }
                        }
                }
                //}
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void read_outbound(String path, ArrayList<String> S, ArrayList<VIB> V, ArrayList<ArrayList<Outbound>> out, boolean state) throws IOException {
        if (!state) {
            BufferedReader bro = null;
            bro = new BufferedReader(new FileReader(path));
            String line = bro.readLine();
            while ((line = bro.readLine()) != null) {
                String[] token = line.split(";");
                //if(Integer.parseInt(token[0])<=43472){
                if (!S.contains(token[1])) {
                    S.add((token[1]));
                    V.add(new VIB(token[1]));
                    String str = token[2];
                    V.get(S.indexOf(token[1])).add_outbound(Integer.parseInt(str));
                } else {
                    String str = token[2];
                    V.get(S.indexOf(token[1])).add_outbound(Integer.parseInt(str));
                }
            }//}
        } else {
            BufferedReader bro = null;
            try {
                bro = new BufferedReader(new FileReader(path));
                String line = bro.readLine();
                while ((line = bro.readLine()) != null) {
                    String[] token = line.split(";");
                    //if(Integer.parseInt(token[0])<=43472){
                    if (S.contains(token[1])) {
                        String str = token[0];
                        out.get((Integer.parseInt(str) - 43466)).add(new Outbound(V.get(S.indexOf(token[1])), Integer.parseInt(token[2]), Integer.parseInt(str) - 43466));
                    }
                }//}
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void read_handling(String path, ArrayList<String> S, ArrayList<VIB> V) throws IOException {
        BufferedReader brh = null;
        try {
            brh = new BufferedReader(new FileReader(path));
            String line = brh.readLine();
            String[] token = line.split(";");
            for (int i = 3; i < token.length; i++) {
                this.Handlingdocs.add(token[i]);
            }
            while ((line = brh.readLine()) != null) {
                token = line.split(";");
                if (S.contains(token[1])) {
                    V.get(S.indexOf(token[1])).documents.add(new ArrayList<Integer>());
                    V.get(S.indexOf(token[1])).countries.add(token[0]);
                    int size = V.get(S.indexOf(token[1])).documents.size();
                    for (int i = 3; i < token.length; i++) {
                        if (token[i] != null) {
                            V.get(S.indexOf(token[1])).documents.get(size-1).add(i);
                        }
                    }
                    V.get(S.indexOf(token[1])).pool_or_country = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String write(ArrayList<VIB> a, String path) throws IOException {
        String output = path;
        FileWriter csvWriter = new FileWriter(output);
        csvWriter.append("Vib number\t");
        csvWriter.append(";");
        csvWriter.append("Total Inbound\t");
        csvWriter.append(";");
        csvWriter.append("Number of Inbound\t");
        csvWriter.append(";");
        csvWriter.append("Total Outbound\t");
        csvWriter.append(";");
        csvWriter.append("Number of Outbound\t");
        csvWriter.append(";");
        csvWriter.append("Inbound-Outbound\t");
        csvWriter.append(";");
        csvWriter.append("Stock\t");
        csvWriter.append("\n");

        for (int i = 0; i < a.size(); i++) {
            csvWriter.append(this.VIB_numbers.get(i).vib_number);
            csvWriter.append(";");
            csvWriter.append(Integer.toString(this.VIB_numbers.get(i).total_inbound));
            csvWriter.append(";");
            csvWriter.append(Integer.toString(this.VIB_numbers.get(i).inbound_num));
            csvWriter.append(";");
            csvWriter.append(Integer.toString(this.VIB_numbers.get(i).total_outbound));
            csvWriter.append(";");
            csvWriter.append(Integer.toString(this.VIB_numbers.get(i).outbound_num));
            csvWriter.append(";");
            csvWriter.append(Integer.toString((int) (this.VIB_numbers.get(i).total_inbound) - this.VIB_numbers.get(i).total_outbound));
            csvWriter.append(";");
            csvWriter.append(Integer.toString(this.VIB_numbers.get(i).initial_storage));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        return output;
    }

    public void analyse(String a) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(a));
            String line = br.readLine();
            System.out.println("Total Vib numbers before elimination: " + VIB_numbers.size());
            int unavailable_cap = 0;
            while ((line = br.readLine()) != null) {
                String[] token = line.split(";");
                if (Integer.parseInt(token[5]) < 0) {
                    if (Math.abs(Integer.parseInt(token[5])) >= Integer.parseInt(token[6])) {
                    VIB_numbers.remove(VIB_numbers_S.indexOf(token[0]));
                    VIB_numbers_S.remove(token[0]);
                    }
                }

                if (Integer.parseInt(token[1]) == 0 && Integer.parseInt(token[3]) == 0) {
                    VIB_numbers.remove(VIB_numbers_S.indexOf(token[0]));
                    VIB_numbers_S.remove(token[0]);
                    unavailable_cap += Integer.parseInt(token[6]);
                }
                /*if(VIB_numbers_S.contains(token[0]) && Integer.parseInt(token[2])<4 && Integer.parseInt(token[4])<4){
                    VIB_numbers.remove(VIB_numbers_S.indexOf(token[0]));
                    VIB_numbers_S.remove(token[0]);
                }*/
            }
            System.out.println("Total Vib numbers after elimination: " + VIB_numbers.size());
            System.out.println("Unavailable capacity= " + unavailable_cap);
            write(VIB_numbers, "data_son.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
