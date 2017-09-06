/**
 * Created by Roma on 17.05.2017.
 */
public class User {

    public String uid;
    public int sex; //0-unknown, 1-female, 2-male

    public User(String uid) {
        this.uid = new String(uid);
    }

    public User(String uid, int sex) {
        this.uid = new String(uid);
        this.sex = sex;
    }


    public User(User u) {
        this.uid = new String(u.uid);
        this.sex = u.sex;
    }

    @Override
    public boolean equals(Object u) {

        if (!(u instanceof User)) {
            return false;
        }
        User user = (User) u;
        return this.uid.equals(user.uid);

    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

}
