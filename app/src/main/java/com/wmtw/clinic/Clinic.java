package com.wmtw.clinic;


import java.util.List;

public class Clinic {

    /**
     * address : 474 Campus Place, Hobucken, Delaware, 6041
     * phone : +1 (917) 552-2283
     * email : hung@kogi.ws
     * company : GEOLOGIX
     * picture : http://placehold.it/32x32
     * balance : $3,455.24
     * isActive : true
     * guid : e4f4e890-b8b0-4fe9-815e-35a9458a5b4f
     * index : 0
     * _id : 565312c86308814888cf9771
     * lat : 22.716149
     * lng : 120.283207
     */

    private List<ClinicEntity> clinic;

    public void setClinic(List<ClinicEntity> clinic) {
        this.clinic = clinic;
    }

    public List<ClinicEntity> getClinic() {
        return clinic;
    }

    public static class ClinicEntity {
        private String address;
        private String phone;
        private String email;
        private String company;
        private String picture;
        private String balance;
        private boolean isActive;
        private String guid;
        private int index;
        private String _id;
        private String lat;
        private String lng;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getAddress() {
            return address;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getCompany() {
            return company;
        }

        public String getPicture() {
            return picture;
        }

        public String getBalance() {
            return balance;
        }

        public boolean isIsActive() {
            return isActive;
        }

        public String getGuid() {
            return guid;
        }

        public int getIndex() {
            return index;
        }

        public String get_id() {
            return _id;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }
}
