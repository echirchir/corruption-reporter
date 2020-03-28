package ke.co.corruptionreporter;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Corruption extends RealmObject {

    @Index
    @PrimaryKey
    private long id;

    private String location;
    private String corruption_type;
    private String date;
    private String title;
    private String description;
    private String image_path;

    public Corruption(){}

    public Corruption(long id, String location, String corruption_type, String date, String title, String description, String image_path) {
        this.id = id;
        this.location = location;
        this.corruption_type = corruption_type;
        this.date = date;
        this.title = title;
        this.description = description;
        this.image_path = image_path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCorruption_type() {
        return corruption_type;
    }

    public void setCorruption_type(String corruption_type) {
        this.corruption_type = corruption_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
