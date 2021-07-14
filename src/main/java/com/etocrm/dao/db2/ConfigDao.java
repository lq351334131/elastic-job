package com.etocrm.dao.db2;

import java.util.List;
import java.util.Map;

public interface ConfigDao {

    List<Map<String, String>> queryConfig(String projectName);

}
