package homework3.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
public class HPMember {
    @SerializedName("_id")
    @JsonProperty("_id")
    private String id;
    private String name;

    public HPMember(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "HPMember{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}