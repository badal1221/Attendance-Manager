package com.example.attandancemanager;

public class Model {
    String subject;
    int totalc,presc;
    public Model(){}
    public Model(String subject, int totalc, int presc) {
        this.subject = subject;
        this.totalc = totalc;
        this.presc = presc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalc() {
        return totalc;
    }

    public void setTotalc(int totalc) {
        this.totalc = totalc;
    }

    public int getPresc() {
        return presc;
    }

    public void setPresc(int presc) {
        this.presc = presc;
    }
}
