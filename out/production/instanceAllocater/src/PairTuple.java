public final class PairTuple implements Tuple{

    final Object firstIndexValue;
    final Object secondIndexValue;

    public PairTuple(Object firstIndexValue, Object secondIndexValue) {
        this.firstIndexValue = firstIndexValue;
        this.secondIndexValue= secondIndexValue;
    }

    public Object get(int index) {
        Object resultObject = null;
        switch (index) {
            case 1: resultObject = firstIndexValue;
            case 2: resultObject = secondIndexValue;
        }
        return resultObject;
    }

    @Override
    public String toString(){
        return "(" + firstIndexValue +"," + secondIndexValue + ")";
    }
}
