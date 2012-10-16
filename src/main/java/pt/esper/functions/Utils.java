package pt.esper.functions;

public class Utils {
    
    public static long genUniqueId(long seed)
    {
        return new String(""+seed+""+Math.random()).hashCode();
    }

}
