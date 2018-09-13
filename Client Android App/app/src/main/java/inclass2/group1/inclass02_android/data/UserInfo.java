package inclass2.group1.inclass02_android.data;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private String _id,userName,email,password, _v;

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "_id='" + _id + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", _v='" + _v + '\'' +
                '}';
    }

    public String get_v() {
        return _v;
    }

    public void set_v(String _v) {
        this._v = _v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
