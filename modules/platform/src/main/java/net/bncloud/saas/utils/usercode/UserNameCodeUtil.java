package net.bncloud.saas.utils.usercode;

import cn.hutool.core.util.RandomUtil;
import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.rnkrsoft.bopomofo4j.ToneType;

public class UserNameCodeUtil {

    public static String userNameCode(String userName) {
        return Bopomofo4j.pinyin(userName, ToneType.WITHOUT_TONE, false, false, "") + RandomUtil.randomNumbers(4);
    }

    public static String userNameCode(String userName, int randomNumLength) {
        return Bopomofo4j.pinyin(userName, ToneType.WITHOUT_TONE, false, false, "") + RandomUtil.randomNumbers(randomNumLength);
    }
}
