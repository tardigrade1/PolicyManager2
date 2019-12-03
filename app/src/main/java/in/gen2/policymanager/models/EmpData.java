package in.gen2.policymanager.models;

public class EmpData {
    private String sno=null;
    private String grade=null;
    private String date=null;
    private String Srno=null;
    private String name=null;
    private String AgencyNo=null;
    private String branch=null;
    private String branchNo=null;
    private String residence=null;
    private String mobNo=null;
    private String email=null;

    public String getSno() {
        return sno;
    }

    public String getGrade() {
        return grade;
    }

    public String getDate() {
        return date;
    }

    public String getSrno() {
        return Srno;
    }

    public String getName() {
        return name;
    }

    public String getAgencyNo() {
        return AgencyNo;
    }

    public String getBranch() {
        return branch;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public String getResidence() {
        return residence;
    }

    public String getMobNo() {
        return mobNo;
    }

    public String getEmail() {
        return email;
    }

    public EmpData(String sno, String grade, String date, String srno, String name, String agencyNo, String branch, String branchNo, String residence, String mobNo, String email) {
        this.sno = sno;
        this.grade = grade;
        this.date = date;
        Srno = srno;
        this.name = name;
        AgencyNo = agencyNo;
        this.branch = branch;
        this.branchNo = branchNo;
        this.residence = residence;
        this.mobNo = mobNo;
        this.email = email;
    }
}
