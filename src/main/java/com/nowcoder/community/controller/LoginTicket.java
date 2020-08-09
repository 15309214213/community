package com.nowcoder.community.controller;

import java.util.Date;

public class LoginTicket {
    private int id;
    private int uesrId;
    private String ticket;
    private int status;
    private Date expired;

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", uesrId=" + uesrId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUesrId(int uesrId) {
        this.uesrId = uesrId;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getId() {
        return id;
    }

    public int getUesrId() {
        return uesrId;
    }

    public String getTicket() {
        return ticket;
    }

    public int getStatus() {
        return status;
    }

    public Date getExpired() {
        return expired;
    }
}
