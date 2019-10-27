package auction;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import java.util.ArrayList;


public class artifact extends Artifact {
    
    private ArrayList<String> bidders = new ArrayList<String>();

    void init() {
        this.defineObsProperty("numBidders", bidders.size());
        this.defineObsProperty("winner", "");
    }
    
    @OPERATION void startAuction(int value) {
        this.defineObsProperty("price",value);
    }

    @OPERATION void inc(int value){
        ObsProperty prop = getObsProperty("price");
        prop.updateValue(prop.intValue()+value);
    }

    @OPERATION void join(String name) {
        bidders.add(name);
        ObsProperty prop = getObsProperty("numBidders");
        prop.updateValue(bidders.size());
        ObsProperty prop2 = getObsProperty("winner");
        prop2.updateValue(bidders.get(0));
    }

    @OPERATION void exit(String name) {
        if(bidders.size() > 1)
            bidders.remove(name);
        ObsProperty prop1 = getObsProperty("numBidders");
        prop1.updateValue(bidders.size());

        if(bidders.size() == 1) {
            ObsProperty prop2 = getObsProperty("winner");
            prop2.updateValue(bidders.get(0));
        }
    }

}