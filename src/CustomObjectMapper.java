public class CustomObjectMapper {

    public <T> T convertValue(String value, Class<T> cls, String[] parserTypes) {
        if(cls == PairTuple.class) {
            String[] valueArray = value.replace("(","")
                                        .replace(")","")
                                        .trim()
                                        .split(",");
            return (T) new PairTuple(getParsedValue(valueArray[0], parserTypes[0])
                            , getParsedValue(valueArray[1], parserTypes[1]));

        }
        return null;
    }

    protected Object getParsedValue(Object value, String parserType) {
        Object parsedValue = value;
        if (parserType.equalsIgnoreCase("string")) {
            parsedValue = String.valueOf(value);
        } else if (parserType.equalsIgnoreCase("integer")
                || parserType.equalsIgnoreCase("int")) {
            parsedValue = Integer.valueOf(String.valueOf(value));
        }
        return parsedValue;
    }
}
