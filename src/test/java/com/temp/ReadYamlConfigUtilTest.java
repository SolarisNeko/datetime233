package com.temp;

import com.neko233.sql.lightrail.util.ReadYamlConfigUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReadYamlConfigUtilTest {

    private ReadYamlConfigUtil readYamlConfigUtil;

    @Before
    public void init() {
        readYamlConfigUtil = new ReadYamlConfigUtil();
    }

    @Test
    public void baseTest() {
        Boolean enableSlowSql = readYamlConfigUtil.getBoolean("railPlatform.slowSql.enable");
        Assert.assertTrue(enableSlowSql);
    }

}
