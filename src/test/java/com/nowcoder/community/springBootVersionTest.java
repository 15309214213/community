//import org.junit.Test;

import com.nowcoder.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.ContextConfiguration;

///import org.junit.runner.RunWith;

/**
 * @author ---
 * @date 3/6/2020
 */
@ContextConfiguration(classes = CommunityApplication.class)
class UserTest {

    @Test
    public void Test1(){
        String version = SpringVersion.getVersion();
        String version1 = SpringBootVersion.getVersion();
        System.out.println(version);
        System.out.println(version1);
    }
}

