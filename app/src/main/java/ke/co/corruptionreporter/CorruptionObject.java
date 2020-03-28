package ke.co.corruptionreporter;

public class CorruptionObject {

    private long id;

    private String county;

    private String date;

    private String description;

    public CorruptionObject(){}

    public CorruptionObject(long id, String county, String date, String description) {
        this.id = id;
        this.county = county;
        this.date = date;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
