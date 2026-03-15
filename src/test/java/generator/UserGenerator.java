package generator;

import org.apache.commons.lang3.RandomStringUtils;
import user.User;

public class UserGenerator {

    public static User generateValidUser() {
        String raw = "user_" + RandomStringUtils.randomAlphanumeric(6) + "@yandex.ru";
        String email = raw.toLowerCase();
        return User.builder()
                .email(email)
                .password(RandomStringUtils.randomAlphanumeric(8))
                .name(RandomStringUtils.randomAlphabetic(6))
                .build();
    }
}
