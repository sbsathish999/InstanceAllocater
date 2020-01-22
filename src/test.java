import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class test
{
    public static void main(String[] args) throws Throwable {
        final Tuple tuple = new PairTuple("bang",11);
        System.out.println(tuple.getClass());
        System.out.println(tuple.get(1));
        System.out.println("raja");

        String tuplestring = tuple.toString();
        System.out.println("tuplestring : " + tuplestring);

        Tuple pairtuple = new CustomObjectMapper().convertValue(tuplestring, PairTuple.class, new String[]{"string", "int"});
        System.out.println("pairtuple : " + pairtuple);
    }
}
