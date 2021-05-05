package process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guo Weize
 * @date 2021/5/5
 */
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    public static String userList() {
        LOGGER.info("Test log4j2 info");
        LOGGER.warn("Test log4j2 warn");
        LOGGER.error("Test log4j2 error");
        return "userList";
    }

    public static void main(String[] args) {
        userList();
    }
}
