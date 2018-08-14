package demo.recycle.com.salforceapp.pojo;

public class PersonNames {
    private String personName;
    private String personId;
    public PersonNames(String personName,String personId) {
        this.personName = personName;
        this.personId = personId;
    }

    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}