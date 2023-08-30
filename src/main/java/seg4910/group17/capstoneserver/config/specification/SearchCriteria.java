package seg4910.group17.capstoneserver.config.specification;

public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;
    private String type;

    public SearchCriteria(String key, String operation, Object value, String type) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public boolean isOrPredicate() {
        return type.equalsIgnoreCase("or");
    }
}
