package assignment1.narispillai.com.assignment1;



public class AdminUserClass {

        private int id;
        private String username;
        private String password;
        private String gender;
        private String email;
        private String birthday;
        private String position;
        private String address;
        private String phonenumber;
        private String dmofbirth;
        private boolean ischeck;

        public AdminUserClass() {

                ischeck = false;

        }

        public boolean ischeck() {
                return ischeck;
        }

        public void setIscheck(boolean ischeck) {
                this.ischeck = ischeck;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
        return password;
        }

        public void setPassword(String password) {
        this.password = password;
        }

        public String getGender() {
        return gender;
        }

        public void setGender(String gender) {
        this.gender = gender;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getPosition() {

                return position;
        }

        public void setPosition(String position) {

                this.position = position;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getPhoneNumber() {
                return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
                this.phonenumber = phonenumber;
        }

        public String getDMofbirth() {
                return dmofbirth;
        }

        public void setDMofbirth(String dmofbirth) {
                this.dmofbirth = dmofbirth;
        }

}
