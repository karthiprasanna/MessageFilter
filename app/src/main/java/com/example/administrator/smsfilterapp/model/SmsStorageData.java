package com.example.administrator.smsfilterapp.model;

/**
 * Created by administrator on 1/25/18.
 */

public class SmsStorageData {
    public String _id;
    public String  address;
    public String  date;
    public String body;

    @Override
    public String toString() {
        return "SmsStorageData{" +
                "_id='" + _id + '\'' +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", body='" + body + '\'' +
                '}';
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String  getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
