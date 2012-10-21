import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import antlr.Utils;

import pt.esper.loader.Loader;


public class Kalman {

    @Test
    public void test() {
        Loader toTest = new Loader();
        
        HashMap<String,Object> bootstrap = new HashMap<String,Object>();
        bootstrap.put("trade_sym", "AAPL");
        bootstrap.put("error", 1000.0);
        bootstrap.put("x1", 12.8);
        bootstrap.put("x2", 12.9);
        bootstrap.put("x3", 13.0);

        HashMap<String,Object> err = new HashMap<String,Object>();
        err.put("trade_sym", "AAPL");
        err.put("error", 1000.0);
        
        HashMap<String,Object> one = new HashMap<String,Object>();
        one.put("trade_sym", "AAPL1");
        one.put("price", 13.1);
        one.put("puid", pt.esper.functions.Utils.genUniqueId(10000));

        HashMap<String,Object> two = new HashMap<String,Object>();
        two.put("trade_sym", "AAPL2");
        two.put("price", 13.2);
        two.put("puid", pt.esper.functions.Utils.genUniqueId(10000));

        HashMap<String,Object> three = new HashMap<String,Object>();
        three.put("trade_sym", "AAPL3");
        three.put("price", 13.3);
        three.put("puid", pt.esper.functions.Utils.genUniqueId(10000));
        
        HashMap<String,Object> four = new HashMap<String,Object>();
        four.put("trade_sym", "AAPL4");
        four.put("price", 13.4);
        four.put("puid", pt.esper.functions.Utils.genUniqueId(10000));
        
        HashMap<String,Object> five = new HashMap<String,Object>();
        five.put("trade_sym", "AAPL5");
        five.put("price", 13.5);
        five.put("puid", pt.esper.functions.Utils.genUniqueId(10000));
        
        HashMap<String,Object> six = new HashMap<String,Object>();
        six.put("trade_sym", "AAPL6");
        six.put("price", 13.6);
        six.put("puid", pt.esper.functions.Utils.genUniqueId(10000));
        
        HashMap<String,Object> seven = new HashMap<String,Object>();
        seven.put("trade_sym", "AAPL7");
        seven.put("price", 13.7);
        seven.put("puid", pt.esper.functions.Utils.genUniqueId(10000));
        
        ArrayList<String> epls = new ArrayList<String>();
        epls.add("epl/kalman.epl");
        toTest.setEplFiles(epls);
        toTest.initialize();
        toTest.sendEvent(bootstrap, "Bootstrap");
        toTest.sendEvent(err, "TradeSymPredictError");
        toTest.sendEvent(one, "TradeSymTick");
        toTest.sendEvent(two, "TradeSymTick");
        toTest.sendEvent(three, "TradeSymTick");
        toTest.sendEvent(four, "TradeSymTick");
        toTest.sendEvent(five, "TradeSymTick");
        toTest.sendEvent(six, "TradeSymTick");
        toTest.sendEvent(seven, "TradeSymTick");
        
        toTest.shutdown();
        
        org.junit.Assert.assertTrue(true);
        //fail("Not yet implemented");
    }

}
