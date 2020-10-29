package com.nitant.uberapp;

import java.util.List;

public class ToolAddInfo_class {

    String equipmentName;
    String registerDetails;
    String spinner1;
    String spinner2;
    String date1,date2;



    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setRegisterDetails(String registerDetails) {
        this.registerDetails = registerDetails;
    }

    public void setSpinner1(String spinner1) {
        this.spinner1 = spinner1;
    }

    public void setSpinner2(String spinner2) {
        this.spinner2 = spinner2;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }



    public ToolAddInfo_class(){

    }

    public ToolAddInfo_class(ToolsListPage toolsListPage, List<ToolAddInfo_class> toolAddInfo_classList){

    }

    public ToolAddInfo_class(String equipmentName, String registerDetails, String spinner1, String spinner2, String date1, String date2) {

        this.equipmentName = equipmentName;
        this.registerDetails = registerDetails;
        this.spinner1 = spinner1;
        this.spinner2 = spinner2;
        this.date1=date1;
        this.date2=date2;

    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getRegisterDetails() {
        return registerDetails;
    }

    public String getSpinner1() {
        return spinner1;
    }

    public String getSpinner2() {
        return spinner2;
    }

    public String getDate1() {
        return date1;
    }

    public String getDate2() {
        return date2;
    }
}
