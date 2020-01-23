package in.gen2.policymanager.models;

/**
 * Created by User on 1/21/2017.
 */

public class XYValue {

    private String id=null;
    private String appNo=null;
    private String name=null;
    private String contact=null;
    private String pan=null;
    private String srNo=null;

    public XYValue(String id, String appNo, String name, String contact, String pan, String srNo) {
        this.id = id;
        this.appNo = appNo;
        this.name = name;
        this.contact = contact;
        this.pan = pan;
        this.srNo = srNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }
}
