package in.gen2.policymanager.models;

import androidx.annotation.Keep;

@Keep
public class EmpData {

    private String doj=null;
    private String srNo=null;
    private String name=null;
    private String branch=null;
    private String residence=null;
    private String mobileNo=null;
    private String email=null;
    private String supervisorCode=null;
    private boolean active=false;

    public EmpData(){

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmpData(String doj, String srNo, String name, String branch, String residence, String mobileNo, String email) {
        this.doj = doj;
        this.srNo = srNo;
        this.name = name;
        this.branch = branch;
        this.residence = residence;
        this.mobileNo = mobileNo;
        this.email = email;
    }
}
