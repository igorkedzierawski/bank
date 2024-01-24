package zet.kedzieri.bank.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ServerPasswordEncoder extends BCryptPasswordEncoder {

    //~500ms/hash
    private static final int STRENGTH = 13;

    public ServerPasswordEncoder() {
        super(STRENGTH);
    }

}
