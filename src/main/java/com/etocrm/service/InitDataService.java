package com.etocrm.service;

import java.util.List;
import java.util.Map;

public interface InitDataService {

    List<Map<String, String>> initDataConfig(String projectName);

    List<Map<String, String>> loadDataConfig(String projectName);
}
