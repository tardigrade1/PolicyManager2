package in.gen2.policymanager.models;

import androidx.annotation.Keep;

@Keep
public class SalesRepsDatamodel {
    private String id=null;
    private String srNo=null;
    private String name=null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
