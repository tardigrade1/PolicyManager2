package in.gen2.policymanager.models;
import androidx.annotation.Keep;

@Keep
public class QueriesData {
    private  String applicationNo= null;
    private  String name= null;
    private String contactNo = null;
    private String srNo = null;

    private  String requirements= null;


    public String getApplicationNo() {
        return applicationNo;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getSrNo() {
        return srNo;
    }

    public String getRequirements() {
        return requirements;
    }

    public QueriesData(String applicationNo, String name, String contactNo, String srNo, String requirements) {
        this.applicationNo = applicationNo;
        this.name = name;
        this.contactNo = contactNo;
        this.srNo = srNo;
        this.requirements = requirements;

    }
}
