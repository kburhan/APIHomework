package homework3.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class HPHouse {
    @SerializedName("_id")
    @JsonProperty("_id")
    private String id;
    private String name;
    private String mascot;
    private String headOfHouse;
    private String houseGhost;
    private String founder;
    @SerializedName("__v")
    @JsonProperty("__v")
    private Integer v;
    private String school;
    private List<HPMember> members;
    private List<String>values;
    private List<String>colors;

    public HPHouse(String id, String name, String mascot, String headOfHouse, String houseGhost, String founder, Integer v, String school, List<HPMember> members, List<String> values, List<String> colors) {
        this.id = id;
        this.name = name;
        this.mascot = mascot;
        this.headOfHouse = headOfHouse;
        this.houseGhost = houseGhost;
        this.founder = founder;
        this.v = v;
        this.school = school;
        this.members = members;
        this.values = values;
        this.colors = colors;
    }

    @Override
    public String toString() {
        return "HPHouse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mascot='" + mascot + '\'' +
                ", headOfHouse='" + headOfHouse + '\'' +
                ", houseGhost='" + houseGhost + '\'' +
                ", founder='" + founder + '\'' +
                ", v=" + v +
                ", school='" + school + '\'' +
                ", members=" + members +
                ", values=" + values +
                ", colors=" + colors +
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

    public String getMascot() {
        return mascot;
    }

    public void setMascot(String mascot) {
        this.mascot = mascot;
    }

    public String getHeadOfHouse() {
        return headOfHouse;
    }

    public void setHeadOfHouse(String headOfHouse) {
        this.headOfHouse = headOfHouse;
    }

    public String getHouseGhost() {
        return houseGhost;
    }

    public void setHouseGhost(String houseGhost) {
        this.houseGhost = houseGhost;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public List<HPMember> getMembers() {
        return members;
    }

    public void setMembers(List<HPMember> members) {
        this.members = members;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
}