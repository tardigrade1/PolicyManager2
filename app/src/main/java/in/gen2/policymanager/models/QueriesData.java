package in.gen2.policymanager.models;

public class QueriesData {
    private  String applicationNo= null;
    private  String name= null;
    private String contactNo = null;
    private String srNo = null;

    private  String requirements= null;
    private  String Status= null;

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

    public String getStatus() {
        return Status;
    }

    public QueriesData(String applicationNo, String name, String contactNo, String srNo, String requirements, String status) {
        this.applicationNo = applicationNo;
        this.name = name;
        this.contactNo = contactNo;
        this.srNo = srNo;
        this.requirements = requirements;
        Status = status;
    }
}
