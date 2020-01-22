import java.util.stream.Stream;

public class CustomObjectMapper {

    public <T> T convertValue(String value, Class<T> cls, String[] parserTypes) {
        T convertedClass = null;
        System.out.println(cls.getClass());
        if(cls == PairTuple.class) {
            String[] valueArray = value.replace("(","")
                                        .replace(")","")
                                        .trim()
                                        .split(",");
            convertedClass = (T) new PairTuple(getParsedValue(valueArray[0], parserTypes[0])
                            , getParsedValue(valueArray[1], parserTypes[1]));

        }
        System.out.println("convertedClass : " + convertedClass);
        return convertedClass;
    }

    protected Object getParsedValue(Object value, String parserType) {
        System.out.println("getParsedValue, value : " + value + ", parserType : " + parserType);
        Object parsedValue = value;
        if (parserType.equalsIgnoreCase("string")) {
            parsedValue = String.valueOf(value);
        } else if (parserType.equalsIgnoreCase("integer")
                || parserType.equalsIgnoreCase("int")) {
            parsedValue = Integer.valueOf(String.valueOf(value));
        }
        System.out.println("getParsedValue, parsedValue : " + parsedValue);

        return parsedValue;
    }
}
