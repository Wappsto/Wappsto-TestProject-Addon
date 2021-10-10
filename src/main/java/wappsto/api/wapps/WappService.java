package wappsto.api.wapps;

import wappsto.api.wapps.model.*;

import java.util.*;

public interface WappService {
    void install(String name) throws Exception;

    Collection<String> fetchInstalled() throws Exception;

    Collection<Wapp> fetchAllFromStore() throws Exception;
}
