package vip.jokor.im.model.bean;

//@Entity
public class CategorysBean  {
    /**
     * Id : 1
     * UserId : 1
     * Name : 亲人
     */
//    @Id(assignable = true)
    private long Id;
    private long UserId;
    private String Name;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
