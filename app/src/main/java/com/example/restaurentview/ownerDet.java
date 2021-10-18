package com.example.restaurentview;

public class ownerDet {

        String email,pwd, nameOfOwn,mob,nameOfRest;

        public ownerDet(String email, String pwd, String nameOfOwn, String mob, String nameOfRest) {
            this.email = email;
            this.pwd = pwd;
            this.nameOfOwn = nameOfOwn;
            this.mob = mob;
            this.nameOfRest = nameOfRest;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getNameOfOwn() {
            return nameOfOwn;
        }

        public void setNameOfOwn(String nameOfOwn) {
            this.nameOfOwn = nameOfOwn;
        }

        public String getMob() {
            return mob;
        }

        public void setMob(String mob) {
            this.mob = mob;
        }

        public String getNameOfRest() {
            return nameOfRest;
        }

        public void setNameOfRest(String nameOfRest) {
            this.nameOfRest = nameOfRest;
        }
    }

