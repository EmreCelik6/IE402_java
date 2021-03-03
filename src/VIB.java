import java.util.ArrayList;

public class VIB {
    public String vib_number;
    public int vib_no;
    public int total_inbound=0;
    public int inbound_num=0;
    public int outbound_num=0;
    public int total_outbound=0;
    public int initial_storage;
    public String manufacturer;
    public int place;
    public ArrayList<ArrayList<Integer>> documents=new ArrayList<ArrayList<Integer>>();
    public ArrayList<String> countries=new ArrayList<String>();
    public boolean pool_or_country;
    public int taşıma_cap;
    VIB(String vib_num){ vib_number=vib_num;this.initial_storage=0;this.pool_or_country=false; }
    public void add_initial_storage(int a){ this.initial_storage+=a; }
    public void add_inbound(int i){ total_inbound +=i;inbound_num++; }
    public void add_outbound(int i){ total_outbound +=i;outbound_num++; }
}
