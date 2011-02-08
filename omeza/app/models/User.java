package models;

import play.Logger;
import siena.Id;
import siena.Model;
import siena.Query;

public class User extends Model {
    @Id
    public Long id;

    public String email;

    public String locale;

    User(String email) {
        this.email = email;
    }

    public static User findOrCreate(String email) {
        Logger.info("Look for user: " + email);
        User user = all().filter("email", email).get();
        if (user == null) {
            user = new User(email);
            user.insert();
        }
        return user;
    }

    public static Query<User> all() {
        return Model.all(User.class);
    }

}
