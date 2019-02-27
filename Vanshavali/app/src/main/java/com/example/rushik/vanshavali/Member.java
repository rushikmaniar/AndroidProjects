package com.example.rushik.vanshavali;

public class Member {
    private String member_id;
    private String member_name;
    private String member_parent_id;

    Member(String member_id,String member_name,String member_parent_id){
        this.member_id = member_id;
        this.member_name = member_name;
        this.member_parent_id= member_parent_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getMember_parent_id() {
        return member_parent_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public void setMember_parent_id(String member_parent_id) {
        this.member_parent_id = member_parent_id;
    }
    @Override
    public String toString() {
        return member_name;
    }

}
