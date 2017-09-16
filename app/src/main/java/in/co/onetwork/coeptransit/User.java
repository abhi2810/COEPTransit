package in.co.onetwork.coeptransit;

/**
 * Created by abhi on 16/9/17.
 */

public class User {
    String name,pass,collid,year,email,vowned;
    User(String collid,String name, String pass, String year,String email,String vowned){
        this.name=name;
        this.collid=collid;
        this.pass=pass;
        this.year=year;
        this.email=email;
        this.vowned=vowned;
    }

    public String getName() {
        return name;
    }

    public String getCollid() {
        return collid;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }

    public String getVowned() {
        return vowned;
    }

    public String getYear() {
        return year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCollid(String collid) {
        this.collid = collid;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVowned(String vowned) {
        this.vowned = vowned;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
