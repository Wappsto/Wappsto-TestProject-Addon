package wappsto.network;

import wappsto.network.model.*;
import wappsto.session.model.*;

import java.util.*;

public interface NetworkService {
    void claim(String id) throws Exception;

    CreatorResponse getCreator() throws Exception;

    NetworkMeta create() throws Exception;

    NetworkMeta fetch(String id) throws Exception;

    void share(NetworkMeta network, UserResponse friend) throws Exception;

    void share(String networkId, String friendUsername) throws Exception;

    void updateState(UUID id, String data) throws Exception;

    String getState(UUID id) throws Exception;
}